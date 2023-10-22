package org.example.services.Funkos;

import org.example.models.Funko;
import org.example.models.Notificacion;
import reactor.core.publisher.Flux;


public interface FunkoNotificaciones {
    Flux<Notificacion<Funko>> getNotificationAsFlux();

    void notify(Notificacion<Funko> notificacion);
}