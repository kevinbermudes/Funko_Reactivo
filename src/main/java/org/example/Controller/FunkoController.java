package org.example.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import org.example.models.Funko;
import org.example.models.IdGenerator;
import org.example.util.LocalDateAdapter;
import org.example.util.LocalDateTimeAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class FunkoController {
    List<Funko> funko = new ArrayList<>();

    private Mono<Void> loadData() {
        String dataPath = "data" + File.separator + "funkos.csv";
        String appPath = System.getProperty("user.dir");
        Path filePath = Paths.get(appPath + File.separator + dataPath);
        System.out.println("Loading data from: " + filePath);

        return Mono.fromRunnable(() -> {
            if (!Files.exists(filePath)) {
                System.out.println("File data does not exist");
                return;
            }

            try {
                List<Funko> funkoList = Files.lines(filePath)
                        .skip(1)
                        .map(this::getFunkoFromLine)
                        .collect(Collectors.toList());

                funko.addAll(funkoList);

            } catch (Exception e) {
                throw new RuntimeException("Error reading file: " + e.getMessage(), e);
            }
        });
    }

    private Funko getFunkoFromLine(String line) {
        String[] parts = line.split(",");
        UUID cod = UUID.fromString(line.substring(0, 34).trim());
        String nombre = parts[1];
        Funko.Modelo modelo = Funko.Modelo.valueOf(parts[2]);
        Double precio = Double.valueOf(parts[3]);
        LocalDate fecha = LocalDate.parse(parts[4]);
        Long id = IdGenerator.getInstance().generateId();
        return new Funko(id, cod, nombre, modelo, precio, fecha, LocalDateTime.now(), LocalDateTime.now());
    }

    public Mono<Void> createJson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        return Mono.fromRunnable(() -> {
            try (FileWriter writer = new FileWriter(System.getProperty("user.dir") + File.separator + "data" + File.separator + "funkos.json")) {
                gson.toJson(funko, writer);
            } catch (IOException e) {
                throw new RuntimeException("Error writing JSON file: " + e.getMessage(), e);
            }
        });
    }

    public void run() {
        loadData()
                .doOnSuccess(v -> funko.forEach(System.out::println))
                .then(createJson())
                .block();
    }
}
