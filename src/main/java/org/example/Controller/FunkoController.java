package org.example.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.Data;
import lombok.val;
import org.example.models.Funko;
import org.example.models.IdGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Data
public class FunkoController {
     List<Funko> funko  = new ArrayList<>();

    private CompletableFuture<Void> loaddata() {
        return CompletableFuture.runAsync(() -> {
            @val
            String dataPath = "data" + File.separator + "funkos.csv";
            @val String appPath = System.getProperty("user.dir");
            @val
            Path filePath = Paths.get(appPath + File.separator + dataPath);
            System.out.println("Loading data from: " + filePath);

            if (!Files.exists(filePath)) {
                System.out.println("File data does not exist");
                return;
            }

            try {
                List<CompletableFuture<Funko>> futures = Files.lines(filePath)
                        .skip(1)
                        .map(this::getFunko)
                        .collect(Collectors.toList());

                CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

                // Espera a que todos los futures se completen
                allOf.join();

                funko = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());

            } catch (Exception e) {
                throw new RuntimeException("Error reading file: " + e.getMessage(), e);
            }
        });
    }



    private CompletableFuture<Funko> getFunko(String line) {
        return CompletableFuture.supplyAsync(() -> {
            String[] parts = line.split(",");
            String cod = line.substring(0, 36).trim();
            String nombre = parts[1];
            String modelo = parts[2];
            Double precio = Double.valueOf(parts[3]);
            LocalDate fecha = LocalDate.parse(parts[4]);
            Long id = IdGenerator.getInstance().generateNewId();
            return new Funko(id, cod, nombre, modelo, precio, fecha);
        });
    }
    public CompletableFuture<Void> createJson() {
        return CompletableFuture.runAsync(() -> {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();
            try (FileWriter writer = new FileWriter(System.getProperty("user.dir") + File.separator + "data" + File.separator + "funkos.json")) {
                gson.toJson(funko, writer);
            } catch (IOException e) {
                throw new RuntimeException("Error writing JSON file: " + e.getMessage(), e);
            }
        });
    }
    public void run() {
        loaddata()
                .thenRun(() -> {
                    funko.forEach(System.out::println);
                    createJson().join();
                }).join();
    }
    public static class LocalDateAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(JsonWriter jsonWriter, LocalDate localDate) throws IOException {
            jsonWriter.value(localDate.toString());
        }

        @Override
        public LocalDate read(JsonReader jsonReader) throws IOException {
            return LocalDate.parse(jsonReader.nextString());
        }
    }
}
