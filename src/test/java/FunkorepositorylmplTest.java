import org.example.models.Funko;
import org.example.repositories.Funkos.FunkoRepository;
import org.example.repositories.Funkos.Funkorepositorylmpl;
import org.example.services.database.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FunkorepositorylmplTest {
    private FunkoRepository funkoRepository;
    @BeforeEach
    void setUp() throws SQLException {
        funkoRepository = Funkorepositorylmpl.getInstance(DatabaseManager.getInstance());
        DatabaseManager.getInstance().initTables();

    }


    @Test
    void testFindById() {
        var funko = getFunko();
        Funko savedFunko = funkoRepository.save(funko).block();
        assertNotNull(savedFunko);
        assertNotNull(savedFunko.getId());

        StepVerifier.create(funkoRepository.findById(savedFunko.getId()))
                .assertNext(funko1 -> {
                    assertNotNull(funko1);
                    assertNotNull(funko1.getId());
                    assertEquals(funko.getNombre(), funko1.getNombre());
                    assertEquals(funko.getPrecio(), funko1.getPrecio());
                    assertEquals(funko.getCod(), funko1.getCod());
                    assertEquals(funko.getFechaLanzamiento(), funko1.getFechaLanzamiento());
                    assertEquals(funko.getModelo(), funko1.getModelo());
                    assertEquals(funko.getCreatedAt(), funko1.getCreatedAt());
                    assertEquals(funko.getUpdatedAt(), funko1.getUpdatedAt());
                })
                .verifyComplete();
    }

    @Test
    void testSave() {
        var funko = getFunko();
        Funko savedFunko = funkoRepository.save(funko).block();

        assertAll(
                () -> assertNotNull(savedFunko),
                () -> assertNotNull(savedFunko.getId()),
                () -> assertEquals(funko.getNombre(), savedFunko.getNombre()),
                () -> assertEquals(funko.getPrecio(), savedFunko.getPrecio()),
                () -> assertNotNull(savedFunko.getCod()),
                () -> assertNotNull(savedFunko.getCreatedAt()), // asumiendo que el método se llama getCreatedAt
                () -> assertNotNull(savedFunko.getUpdatedAt())  // asumiendo que el método se llama getUpdatedAt
        );

    }
    private static Funko getFunko() {
        var funko = Funko.builder()
                .id(1L)
                .cod(UUID.randomUUID())
                .nombre("Test")
                .id(1L)
                .modelo(Funko.Modelo.MARVEL)
                .precio(10.0)
                .fechaLanzamiento(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return funko;
    }
}
