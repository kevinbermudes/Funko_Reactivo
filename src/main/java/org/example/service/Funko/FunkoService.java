package org.example.service.Funko;

import org.example.FunkosEx.FunkoException;
import org.example.FunkosEx.FunkoNotFoundException;
import org.example.models.Funko;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

 public interface FunkoService {

    // Retorna todos los Funkos
    List<Funko> findAll() throws SQLException, ExecutionException, InterruptedException;

    // Encuentra el Funko más caro
    Optional<Funko> findMostExpensive() throws SQLException, ExecutionException, InterruptedException;

    // Retorna la media de precio de todos los Funkos
    double averagePrice() throws SQLException, ExecutionException, InterruptedException;

    // Agrupa los Funkos por modelo
    Map<String, List<Funko>> groupByModel() throws SQLException, ExecutionException, InterruptedException;

    // Cuenta cuántos Funkos hay por cada modelo
    Map<String, Long> countByModel() throws SQLException, ExecutionException, InterruptedException;

    // Encuentra los Funkos lanzados en un año específico
    List<Funko> findByReleaseYear(int year) throws SQLException, ExecutionException, InterruptedException;

    // Cuenta cuántos Funkos hay con un nombre específico
    long countByName(String name) throws SQLException, ExecutionException, InterruptedException;

    // Retorna una lista de Funkos basada en el nombre
    List<Funko> findByName(String name) throws SQLException, ExecutionException, InterruptedException;

    // Guarda un nuevo Funko en la base de datos
    Funko save(Funko funko) throws SQLException, ExecutionException, InterruptedException;

    // Actualiza un Funko existente
    Funko update(Funko funko) throws SQLException, FunkoException, ExecutionException, InterruptedException;

    // Elimina un Funko basado en su ID
    boolean deleteById(long id) throws SQLException, ExecutionException, InterruptedException;

    // Elimina todos los Funkos
    void deleteAll() throws SQLException, ExecutionException, InterruptedException;

    // [Otras funciones que puedas necesitar...]
}