package org.example.repositories.Crud;

import org.example.models.Funko;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CrudRepository <T,ID>{
    Flux<Funko> findAll();
    Mono<Funko> findById(ID id);
    Mono<Funko> save(T t);
    Mono<Funko> update(T t);
    Mono<Boolean> deleteById(ID id);
    Mono<Void> deleteAll();

}
