package org.example.repositories.Crud;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


public interface CrudRepository<T, ID> {
    // Métodos que vamos a usar

    // Guardar
    CompletableFuture<T> save(T t) throws SQLException;

    // Actualizar
    CompletableFuture<T> update(T t) throws SQLException;

    // Buscar por ID
    CompletableFuture<Optional<T>> findById(ID id) throws SQLException;

    // Buscar todos
    CompletableFuture<List<T>> findAll() throws SQLException;

    // Borrar por ID
    CompletableFuture<Boolean> deleteById(ID id) throws SQLException;

    // Borrar todos
    CompletableFuture<Void> deleteAll() throws SQLException;
}
