package org.example.repositories;




import org.example.DataBase.DatabaseManager;
import org.example.models.Funko;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class FunkosRepositoryImpl implements FunkosRepository {
    private static FunkosRepositoryImpl instance;
    private final Logger logger = LoggerFactory.getLogger(FunkosRepositoryImpl.class);
    private final DatabaseManager db;

    private FunkosRepositoryImpl(DatabaseManager db) {
        this.db = db;
    }

    public   static FunkosRepositoryImpl getInstance(DatabaseManager db) {
        if (instance == null) {
            instance = new FunkosRepositoryImpl(db);
        }
        return instance;
    }

    @Override
    public CompletableFuture<Funko> save(Funko funko) {
        return CompletableFuture.supplyAsync(() -> {
            String query = "INSERT INTO FUNKOS (id,cod, nombre, modelo, precio, fecha_lanzamiento) VALUES (?,?, ?, ?, ?, ?)";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
            ) {
                stmt.setLong(1, funko.getId());
                stmt.setString(2, funko.getCod());
                stmt.setString(3, funko.getNombre());
                stmt.setString(4, funko.getModelo());
                stmt.setDouble(5, funko.getPrecio());
                stmt.setDate(6, java.sql.Date.valueOf(funko.getFecha()));

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("No se pudo guardar el Funko");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        funko.setId(generatedKeys.getLong(1));  // Establece el ID generado automáticamente
                    } else {
                        throw new SQLException("No se pudo obtener el ID del Funko guardado.");
                    }
                }
            } catch (SQLException e) {
                logger.error("Error al guardar el Funko", e);
                throw new CompletionException(e);
            }
            return funko;
        });
    }


    @Override
    public CompletableFuture<Funko> update(Funko funko) throws SQLException {
        return CompletableFuture.supplyAsync(() -> {
            String query = "UPDATE FUNKOS SET cod = ?, nombre = ?, modelo = ?, precio = ?, fecha_lanzamiento = ? WHERE ID = ?";

            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)) {

                stmt.setString(1, funko.getCod());
                stmt.setString(2, funko.getNombre());
                stmt.setString(3, funko.getModelo());
                stmt.setDouble(4, funko.getPrecio());
                stmt.setDate(5, java.sql.Date.valueOf(funko.getFecha()));
                stmt.setLong(6, funko.getId());

                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("No se pudo actualizar el Funko. Ninguna fila fue afectada.");
                }

            } catch (SQLException e) {
                logger.error("Error al actualizar el Funko", e);
                throw new CompletionException(e);
            }

            return funko;
        });
    }

    @Override
    public CompletableFuture<Optional<Funko>> findById(Long aLong) throws SQLException {
        return CompletableFuture.supplyAsync(() -> {
            String query = "SELECT * FROM FUNKOS WHERE ID = ?";
            Funko funko = null;

            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)) {

                stmt.setLong(1, aLong);

                var rs = stmt.executeQuery();

                if (rs.next()) {
                    funko = new Funko(
                            rs.getLong("ID"),
                            rs.getString("cod"),
                            rs.getString("nombre"),
                            rs.getString("modelo"),
                            rs.getDouble("precio"),
                            rs.getDate("fecha_lanzamiento").toLocalDate()
                    );
                }

            } catch (SQLException e) {
                logger.error("Error al buscar el Funko por ID", e);
                throw new CompletionException(e);
            }

            return Optional.ofNullable(funko);
        });
    }

    @Override
    public CompletableFuture<List<Funko>> findAll() {
        return CompletableFuture.supplyAsync(() -> {
            List<Funko> lista = new ArrayList<>();
            String query = "SELECT * FROM FUNKOS";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)
            ) {
                var rs = stmt.executeQuery();
                while (rs.next()) {
                    Funko funko = new Funko(
                            rs.getLong("ID"),
                            rs.getString("COD"),
                            //  rs.getLong("MYID"),
                            rs.getString("NOMBRE"),
                            rs.getString("MODELO"),
                            rs.getDouble("PRECIO"),
                            rs.getDate("FECHA_LANZAMIENTO").toLocalDate()
                            //    rs.getTimestamp("CREATED_AT").toLocalDateTime(),
                            //     rs.getTimestamp("UPDATED_AT").toLocalDateTime()
                    );
                    lista.add(funko);
                }
            } catch (SQLException e) {
                logger.error("Error al buscar todos los Funkos", e);
                throw new CompletionException(e);
            }
            return lista;
        });
    }

    @Override
    public CompletableFuture<Boolean> deleteById(Long aLong) throws SQLException {
        return CompletableFuture.supplyAsync(() -> {
            String query = "DELETE FROM FUNKOS WHERE ID = ?";
            int affectedRows = 0;

            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)) {

                stmt.setLong(1, aLong);
                affectedRows = stmt.executeUpdate();

            } catch (SQLException e) {
                logger.error("Error al eliminar el Funko por ID", e);
                throw new CompletionException(e);
            }

            return affectedRows > 0;
        });
    }

    @Override
    public CompletableFuture<Void> deleteAll() throws SQLException {
        return CompletableFuture.runAsync(() -> {
            String query = "DELETE FROM FUNKOS";

            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)) {

                stmt.executeUpdate();

            } catch (SQLException e) {
                logger.error("Error al eliminar todos los Funkos", e);
                throw new CompletionException(e);
            }
        });
    }

    @Override
    public CompletableFuture<List<Funko>> findByNombre(String nombre) {
        return CompletableFuture.supplyAsync(() -> {
            var lista = new ArrayList<Funko>();
            String query = "SELECT * FROM FUNKOS WHERE nombre LIKE ?";
            try (var connection = db.getConnection();
                 var stmt = connection.prepareStatement(query)
            ) {
                stmt.setString(1, "%" + nombre + "%");
                var rs = stmt.executeQuery();
                while (rs.next()) {
                    Funko funko = new Funko(
                            rs.getLong("ID"),
                            rs.getString("COD"),
                          //  rs.getLong("MYID"),
                            rs.getString("NOMBRE"),
                            rs.getString("MODELO"),
                            rs.getDouble("PRECIO"),
                            rs.getDate("FECHA_LANZAMIENTO").toLocalDate()
                        //    rs.getTimestamp("CREATED_AT").toLocalDateTime(),
                       //     rs.getTimestamp("UPDATED_AT").toLocalDateTime()
                    );
                    lista.add(funko);
                }
            } catch (SQLException e) {
                logger.error("Error al buscar Funkos por nombre", e);
                throw new CompletionException(e);
            }
            return lista;
        });
    }


}
