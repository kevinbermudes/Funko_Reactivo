package org.example;

import org.example.Controller.FunkoController;
import org.example.models.Funko;
import org.example.repositories.Funkos.Funkorepositorylmpl;
import org.example.services.Funkos.FunkoNotificacionlmpl;
import org.example.services.Funkos.FunkoServicelmpl;
import org.example.services.database.DatabaseManager;

import static org.example.models.Notificacion.Tipo.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Funkos");
        System.out.println("======");
        FunkoController controller = new FunkoController();
        controller.run();
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        Funkorepositorylmpl funkoRepository = (Funkorepositorylmpl) Funkorepositorylmpl.getInstance(DatabaseManager.getInstance());
        var funkosNotification = FunkoNotificacionlmpl.getInstance();
        databaseManager.initTables();
        System.out.println("Notificaciones");
        funkosNotification.getNotificationAsFlux().subscribe(
                notificacion -> {
                    switch (notificacion.getTipo()) {
                        case NEW:
                            System.out.println("🟢 Funko insertado: " + notificacion.getContenido());
                            break;
                        case UPDATED:
                            System.out.println("🟠 Funko actualizado: " + notificacion.getContenido());
                            break;
                        case DELETED:
                            System.out.println("🔴 Funko eliminado: " + notificacion.getContenido());
                            break;
                    }
                },
                error -> System.err.println("Se ha producido un error: " + error),
                () -> {
                    System.out.println("Completado");

                    // Después de recibir las notificaciones, realizamos la consulta y guardamos los Funkos
                    System.out.println("Obtenemos todos los funkos");
                    funkoRepository.findAll().collectList().subscribe(
                            funkos -> {
                                System.out.println("Funkos: " + funkos);

                                // Aquí puedes realizar cualquier acción adicional con los Funkos obtenidos

                                //funko mas caro
                                funkoRepository.findAll()
                                        .reduce((f1, f2) -> f1.getPrecio() > f2.getPrecio() ? f1 : f2)
                                        .subscribe(funko -> System.out.println("Funko más caro: " + funko));
                                //media de funkos
                                funkoRepository.findAll()
                                        .map(Funko::getPrecio)
                                        .reduce((p1, p2) -> p1 + p2)
                                        .flatMap(total -> funkoRepository.findAll().count()
                                                .map(count -> total / count))
                                        .subscribe(media -> System.out.println("Media de precios de Funkos: " + media));

                                //funko por modelo
                                funkoRepository.findAll()
                                        .groupBy(Funko::getModelo)
                                        .subscribe(groupedFunkos -> {
                                            Funko.Modelo modelo = groupedFunkos.key();
                                            System.out.println("Modelo: " + modelo);
                                            groupedFunkos.collectList().subscribe(funkosGrupo -> {
                                                System.out.println("Funkos del modelo " + modelo + ": " + funkosGrupo);
                                            });
                                        });
                                //funko lanzado en el 2023
                                funkoRepository.findAll()
                                        .filter(funko -> funko.getFechaLanzamiento().getYear() == 2023)
                                        .subscribe(funko -> System.out.println("Funko lanzado en 2023: " + funko));
                                //numero de funkos de stich
                                funkoRepository.findByNombrel("Stitch")
                                        .count()
                                        .subscribe(count -> System.out.println("Número de Funkos de Stitch: " + count));
                                //listado de stitch
                                funkoRepository.findByNombrel("Stitch")
                                        .subscribe(funko -> System.out.println("Funko de Stitch: " + funko));
                            },
                            error -> System.err.println("Error al obtener todos los funkos: " + error.getMessage()),
                            () -> System.out.println("Obtención de funkos completada")
                    );
                }
        );
        System.out.printf("fin");
    }
}
