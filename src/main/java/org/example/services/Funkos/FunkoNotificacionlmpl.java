package org.example.services.Funkos;

import org.example.models.Funko;
import org.example.models.Notificacion;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

public class FunkoNotificacionlmpl implements FunkoNotificaciones{
private static FunkoNotificacionlmpl Instance = new FunkoNotificacionlmpl();
private final Flux<Notificacion<Funko>> funkoNotificacionFlux;
private FluxSink<Notificacion<Funko>> funkoNotificacion;
private FunkoNotificacionlmpl(){
    this.funkoNotificacionFlux = Flux.<Notificacion<Funko>>create(emitter->this.funkoNotificacion=emitter).share();
}
public static FunkoNotificacionlmpl getInstance(){
    if(Instance ==null){
        Instance =new FunkoNotificacionlmpl();
    }
    return Instance;
}
    @Override
    public Flux<Notificacion<Funko>> getNotificationAsFlux() {
        return funkoNotificacionFlux;
    }

    @Override
    public void notify(Notificacion<Funko> notificacion) {
funkoNotificacion.next(notificacion);
    }
}
