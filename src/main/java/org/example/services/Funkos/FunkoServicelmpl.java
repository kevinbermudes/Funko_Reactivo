package org.example.services.Funkos;

import org.example.exceptions.FunkoNoEncontrado;
import org.example.models.Funko;
import org.example.models.Notificacion;
import org.example.repositories.Funkos.FunkoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class FunkoServicelmpl implements FunkoServices{
    private static final int CacheSize = 15;
    private static FunkoServicelmpl Instance ;
    private final FunkoCache cache;
    private final FunkoNotificaciones notificaciones;

    private final Logger logger = LoggerFactory.getLogger(FunkoServicelmpl.class);
    private final FunkoRepository funkorepository;
    private FunkoServicelmpl(FunkoRepository funkorepository, FunkoNotificaciones notificaciones){
        this.funkorepository = funkorepository;
        this.cache=new FunkoCachelmpl(CacheSize);
        this.notificaciones=notificaciones;

    }
    public static FunkoServicelmpl getInstance(FunkoRepository funkorepository, FunkoNotificaciones notificaciones){
        if(Instance ==null){
            Instance =new FunkoServicelmpl(funkorepository,notificaciones);
        }
        return Instance;
    }


    @Override
    public Flux<Funko> findAll() {
        logger.debug("Buscando todos los funkos");
        return funkorepository.findAll();
    }

    @Override
    public Flux<Funko> findAllByNombre(String nombre) {
        logger.debug("Buscando todos los funko por nombre");
        return funkorepository.findByNombrel(nombre);
    }

    @Override
    public Mono<Funko> findById(long id) {
        // Ojo con la excepcion si no existe que debemos lanzarla
        logger.debug("Buscando Funko por id: " + id);
        // Lo buscamos en la cache
        return cache.get(id)
                // Si no está en cache lo buscamos en la base de datos
                .switchIfEmpty(funkorepository.findById(id)
                        // Si lo encontramos lo añadimos a la cache
                        .flatMap(alumno -> cache.put(alumno.getId(), alumno)
                                // Y lo devolvemos
                                .then(Mono.just(alumno)))
                        // lanzamos nuestra excepción
                        .switchIfEmpty(Mono.error(new FunkoNoEncontrado("Funko = id " + id + " no encontrado"))));
    }


    @Override
    public Mono<Funko> findByUuid(UUID uuid) {
        logger.debug("Buscando alumno por uuid: " + uuid);
        return funkorepository.findByUuId(uuid)
                .flatMap(alumno -> cache.put(alumno.getId(), alumno)
                        .then(Mono.just(alumno)))
                // lanzamos nuestra ecepcion
                .switchIfEmpty(Mono.error(new FunkoNoEncontrado("Funko con uuid " + uuid + " no encontrado")));
    }

    private Mono<Funko> saveWithoutNotification(Funko funko) {
        logger.debug("Guardando funko sin notificación: " + funko);
        return funkorepository.save(funko)
                .flatMap(saved -> findByUuid(saved.getCod()));
    }
    @Override
    public Mono<Funko> save(Funko funko) {
        logger.debug("Guardando Funko sin la notificacion " + funko);
        return saveWithoutNotification(funko)
                .doOnSuccess(saved -> notificaciones.notify(new Notificacion<>(Notificacion.Tipo.NEW, saved)));
    }
    private Mono<Funko> updateWithoutNotification(Funko funko) {
        // Hacemos esto para testar solo este método y no el update con notificaciones por los problemas que da el doOnSuccess
        // y porque nos falta "base" para testearlo
        logger.debug("Actualizando alumno sin notificación: " + funko);
        return funkorepository.findById(funko.getId())
                .switchIfEmpty(Mono.error(new FunkoNoEncontrado("funko con id " + funko.getId() + " no encontrado")))
                .flatMap(existing -> funkorepository.update(funko)
                        .flatMap(updated -> cache.put(updated.getId(), updated)
                                .thenReturn(updated)));
    }

    @Override
    public Mono<Funko> update(Funko funko) {
        logger.debug("Actualizando funko: " + funko);
        return updateWithoutNotification(funko)
                .doOnSuccess(updated -> notificaciones.notify(new Notificacion<>(Notificacion.Tipo.UPDATED, updated)));

    }
    private Mono<Funko> deleteByIdWithoutNotification(long id) {
        // Hacemos esto para testar solo este método y no el delete con notificaciones por los problemas que da el doOnSuccess
        // y porque nos falta "base" para testearlo
        logger.debug("Borrando Funko sin notificación con id: " + id);
        return funkorepository.findById(id)
                .switchIfEmpty(Mono.error(new FunkoNoEncontrado("Alumno con id " + id + " no encontrado")))
                .flatMap(alumno -> cache.remove(alumno.getId())
                        .then(funkorepository.deleteById(alumno.getId()))
                        .thenReturn(alumno));
    }
    @Override
    public Mono<Funko> deleteById(long id) {
        logger.debug("Borrando Funko por id: " + id);
        return deleteByIdWithoutNotification(id)
                .doOnSuccess(deleted -> notificaciones.notify(new Notificacion<>(Notificacion.Tipo.DELETED, deleted)));

    }

    @Override
    public Mono<Void> deleteAll() {

        logger.debug("Borrando todos los Funko");
        cache.clear();
        return funkorepository.deleteAll()
                .then(Mono.empty());
    }

}
