package org.example.service.Funko;

import org.example.FunkosEx.FunkoException;
import org.example.models.Funko;
import org.example.repositories.FunkosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FunkoServiceImpl implements FunkoService {
    // Asumiendo que haya una CACHE_SIZE para los Funkos
    private static final int CACHE_SIZE = 10;

    private static FunkoServiceImpl instance;
    private final FunkoCache cache; // Asumiendo que tengas un cache para Funkos
    private final Logger logger = LoggerFactory.getLogger(FunkoServiceImpl.class);
    private final FunkosRepository funkoRepository; // Asumiendo que tengas un repositorio para Funkos

    private FunkoServiceImpl(FunkosRepository funkoRepository) {
        this.funkoRepository = funkoRepository;
        this.cache = new FunkoCachempl(CACHE_SIZE); // Asumiendo que tengas un cache impl para Funkos
    }

    public static FunkoServiceImpl getInstance(FunkosRepository funkoRepository) {
        if (instance == null) {
            instance = new FunkoServiceImpl(funkoRepository);
        }
        return instance;
    }

    // Implementa los métodos de FunkoService aquí...

    // Por ejemplo, aquí está el método para obtener todos los Funkos:
    @Override
    public List<Funko> findAll() throws SQLException, ExecutionException, InterruptedException {
        logger.debug("Obteniendo todos los funkos");
        return funkoRepository.findAll().get();
    }

    @Override
    public Optional<Funko> findMostExpensive() throws SQLException, ExecutionException, InterruptedException {
        return Optional.empty();
    }

    @Override
    public double averagePrice() throws SQLException, ExecutionException, InterruptedException {
        return 0;
    }

    @Override
    public Map<String, List<Funko>> groupByModel() throws SQLException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Map<String, Long> countByModel() throws SQLException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public List<Funko> findByReleaseYear(int year) throws SQLException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public long countByName(String name) throws SQLException, ExecutionException, InterruptedException {
        return 0;
    }

    @Override
    public List<Funko> findByName(String name) throws SQLException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Funko save(Funko funko) throws SQLException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public Funko update(Funko funko) throws SQLException, FunkoException, ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public boolean deleteById(long id) throws SQLException, ExecutionException, InterruptedException {
        return false;
    }

    @Override
    public void deleteAll() throws SQLException, ExecutionException, InterruptedException {

    }

    // Y así sucesivamente para los otros métodos...
}

