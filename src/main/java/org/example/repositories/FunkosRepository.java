package org.example.repositories;

import org.example.models.Funko;
import org.example.repositories.Crud.CrudRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FunkosRepository extends CrudRepository<Funko, Long> {
    CompletableFuture<List<Funko>> findByNombre(String nombre) throws SQLException;

}