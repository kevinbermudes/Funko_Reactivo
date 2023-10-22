package org.example.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.models.Funko;
import org.example.models.IdGenerator;

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
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FunkoController {
    private List<Funko> funkos = new ArrayList<>();

    private CompletableFuture<Void> loadData() {
        final String dataPath = "data" + File.separator + "funkos.csv";
        final String appPath = System.getProperty("user.dir");
        final Path filePath = Paths.get(appPath + File.separator + dataPath);
        System.out.println("Loading data from: " + filePath);

        return CompletableFuture.runAsync(() -> {
            if (!Files.exists(filePath)) {
                System.out.println("File data does not exist");
                return;
            }
            try {
                funkos = Files.lines(filePath)
                        .skip(1)
                        .map(this::getFunko)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            } catch (Exception e) {
                throw new RuntimeException("Error reading file: " + e.getMessage(), e);
            }
        });
    }

    private Funko getFunko(String line) {
        try {
            String[] parts = line.split(",");
            UUID cod = UUID.fromString(line.substring(0, 34).trim());
            String nombre = parts[1];
            Funko.Modelo modelo = Funko.Modelo.valueOf(parts[2]);
            Double precio = Double.valueOf(parts[3]);
            LocalDate fecha = LocalDate.parse(parts[4]);
            Long id = IdGenerator.getInstance().generateId();
            return new Funko(id, cod, nombre, modelo, precio, fecha, LocalDateTime.now(), LocalDateTime.now());
        } catch (Exception e) {
            System.err.println("Error processing line: " + line);
            e.printStackTrace();
            return null;
        }
    }
/*
    public CompletableFuture<Void> createJson() {
        return CompletableFuture.runAsync(() -> {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter("funkos.json")) {
                gson.toJson(funkos, writer);
                System.out.println("JSON file created successfully");
            } catch (IOException e) {
                throw new RuntimeException("Error creating JSON file: " + e.getMessage(), e);
            }
        });
    }
*/
    public void run() {
        loadData()
                .thenRun(() -> {
                    funkos.forEach(System.out::println);
                    //createJson().join();
                }).join();
    }
}
