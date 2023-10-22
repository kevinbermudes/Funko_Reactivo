package org.example.repositories.Funkos;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.spi.Result;
import org.example.models.Funko;
import org.example.models.IdGenerator;
import org.example.services.Funkos.FunkoServicelmpl;
import org.example.services.database.DatabaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.r2dbc.spi.Connection;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Funkorepositorylmpl implements FunkoRepository {

   private static Funkorepositorylmpl instance;
   private final Logger logger = LoggerFactory.getLogger(Funkorepositorylmpl.class);
   private final ConnectionPool connectionFactory;


    private Funkorepositorylmpl(DatabaseManager databaseManager){
        this.connectionFactory = databaseManager.getConnectionPool();
    }

    public static FunkoRepository getInstance(DatabaseManager db){
        if (instance == null){
            instance = new Funkorepositorylmpl(db);
        }
        return instance;
    }
    @Override
    public Flux<Funko> findAll() {
    logger.debug("Buscando todos los funkos");
    String sql = "SELECT * FROM FUNKOS";
    return Flux.usingWhen(
            connectionFactory.create(),
            connection -> Flux.from(connection.createStatement(sql).execute())
                    .flatMap(result -> result.map((row, rowMetadata) ->
                            Funko.builder()
                                    .cod(row.get("Codigo", UUID.class))
                                    .nombre(row.get("Nombre", String.class))
                                    .modelo(Funko.Modelo.valueOf(row.get("Modelo", String.class)))
                                    .precio(row.get("Precio", Double.class))
                                    .fechaLanzamiento(row.get("FechaLanzamiento", LocalDate.class))
                                    .createdAt(row.get("created_at", LocalDateTime.class))
                                    .updatedAt(row.get("updated_at", LocalDateTime.class))
                            .build()
                    )),
            Connection::close
    );
    }

    @Override
    public Mono<Funko> findById(Long id) {
        logger.debug("Buscando funkos por id: " + id);
        String sql = "SELECT * FROM FUNKOS WHERE id = ?";
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.from(connection.createStatement(sql)
                        .bind(0, id)
                        .execute()
                ).flatMap(result -> Mono.from(result.map((row, rowMetadata) ->
                        Funko.builder()
                                .cod(row.get("Codigo", UUID.class))
                                .nombre(row.get("Nombre", String.class))
                                .modelo(Funko.Modelo.valueOf(row.get("Modelo", String.class)))
                                .precio(row.get("Precio", Double.class))
                                .fechaLanzamiento(row.get("FechaLanzamiento", LocalDate.class))
                                .createdAt(row.get("created_at", LocalDateTime.class))
                                .updatedAt(row.get("updated_at", LocalDateTime.class))
                                .build()
                ))),
                Connection::close
        );
    }



    @Override
    public Mono<Funko> save(Funko funko) {
        logger.debug("Guardando funko: " + funko);
        String sql = "INSERT INTO FUNKOS (cod, nombre, modelo, precio, fecha_lanzamiento, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.from(connection.createStatement(sql)
                        .bind(0, funko.getCod().toString())
                        .bind(1, funko.getNombre())
                        .bind(2, funko.getModelo().toString())
                        .bind(3, funko.getPrecio())
                        .bind(4, funko.getFechaLanzamiento())
                        .bind(5, funko.getCreatedAt())
                        .bind(6, funko.getUpdatedAt())
                        .execute()
                ).then(Mono.just(funko)),
                Connection::close
        );
    }


    @Override
    public Mono<Funko> update(Funko funko) {
        logger.debug("Actualizando funko: " + funko);
        String query = "UPDATE FUNKOS SET nombre = ?, modelo = ?, precio = ?, fecha_lanzamiento = ?, updated_at = ? WHERE cod = ?";
        funko.setUpdatedAt(LocalDateTime.now());
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.from(connection.createStatement(query)
                        .bind(0, funko.getNombre())
                        .bind(1, funko.getModelo().toString())
                        .bind(2, funko.getPrecio())
                        .bind(3, funko.getFechaLanzamiento())
                        .bind(4, funko.getUpdatedAt())
                        .bind(5, funko.getCod().toString())
                        .execute()
                ).then(Mono.just(funko)), // Devolvemos el objeto 'funko' después de la actualización
                Connection::close
        );
    }

    @Override
    public Mono<Boolean> deleteById(Long uuid) {
        logger.debug("Borrando Funkos por uuid: " + uuid);
        String sql = "DELETE FROM FUNKOS WHERE id = ?";
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.from(connection.createStatement(sql)
                                .bind(0, uuid)
                                .execute()
                        ).flatMapMany(Result::getRowsUpdated)
                        .hasElements(),
                Connection::close
        );
    }

    @Override
    public Mono<Void> deleteAll() {
        logger.debug("Borrando todos los funkos");
        String sql = "DELETE FROM FUNKOS";
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.from(connection.createStatement(sql)
                        .execute()
                ).then(),
                Connection::close
        );
    }

    @Override
    public Flux<Funko> findByNombrel(String nombre) {
        logger.debug("Buscando todos los funkos por nombre");
        String sql = "SELECT * FROM FUNKOS WHERE nombre LIKE ?";
        return Flux.usingWhen(
                connectionFactory.create(),
                connection -> Flux.from(connection.createStatement(sql)
                        .bind(0, "%" + nombre + "%")
                        .execute()
                ).flatMap(result -> result.map((row, rowMetadata) ->
                        Funko.builder()
                                .cod(row.get("cod", UUID.class))
                                .nombre(row.get("nombre", String.class))
                                .modelo(Funko.Modelo.valueOf(row.get("modelo", String.class)))
                                .precio(row.get("precio", Double.class))
                                .fechaLanzamiento(row.get("fecha_lanzamiento", java.time.LocalDate.class))
                                .createdAt(row.get("created_at", java.time.LocalDateTime.class))
                                .updatedAt(row.get("updated_at", java.time.LocalDateTime.class))
                                .build()
                )),
                Connection::close
        );
    }

    @Override
    public Mono<Funko> findByUuId(UUID uuid) {
        logger.debug("Buscando funko por UUID: " + uuid);
        String sql = "SELECT * FROM FUNKOS WHERE cod = ?";
        return Mono.usingWhen(
                connectionFactory.create(),
                connection -> Mono.from(connection.createStatement(sql)
                        .bind(0, uuid)
                        .execute()
                ).flatMap(result -> Mono.from(result.map((row, rowMetadata) ->
                        Funko.builder()
                                .cod(row.get("cod", UUID.class))
                                .nombre(row.get("nombre", String.class))
                                .modelo(Funko.Modelo.valueOf(row.get("modelo", String.class)))
                                .precio(row.get("precio", Double.class))
                                .fechaLanzamiento(row.get("fecha_lanzamiento", java.time.LocalDate.class))
                                .createdAt(row.get("created_at", java.time.LocalDateTime.class))
                                .updatedAt(row.get("updated_at", java.time.LocalDateTime.class))
                                .build()
                ))),
                Connection::close
        );
    }
}
