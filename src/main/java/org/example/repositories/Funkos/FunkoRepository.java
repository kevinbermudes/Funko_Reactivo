package org.example.repositories.Funkos;

import org.example.models.Funko;
import org.example.repositories.Crud.CrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface FunkoRepository extends CrudRepository<Funko , Long>  {

    Flux<Funko> findByNombrel(String nombre);
    Mono<Funko> findByUuId(UUID uuid);
}
