package org.example;

import jdk.internal.icu.util.VersionInfo;
import org.example.Controller.FunkoController;
import org.example.DataBase.DatabaseManager;
import org.example.models.Funko;
import org.example.repositories.FunkosRepositoryImpl;
import org.example.service.Funko.FunkoService;
import org.example.service.Funko.FunkoServiceImpl;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

package org.example.service.Funko.FunkoService;
public class Main {
    public static void main(String[] args) {
        System.out.println("Funkos");
        System.out.println("======");
        FunkoController controller = new FunkoController();
        controller.run();


        FunkoService funkoService = FunkoServiceImpl.getInstance(FunkosRepositoryImpl.getInstance(DatabaseManager.getInstance()));

        // Funko más caro
        CompletableFuture<Funko> funkoMasCaro = funkoService.findMostExpensive();
        funkoMasCaro.thenAccept(result -> System.out.println("Funko más caro: " + result));

        // Media de precio de Funkos
        CompletableFuture<Double> mediaPrecio = funkoService.averagePrice();
        mediaPrecio.thenAccept(result -> System.out.println("Media de precio de Funkos: " + result));

        // Funkos agrupados por modelos
        CompletableFuture<Map<String, List<Funko>>> funkosPorModelo = funkoService.groupByModel();
        funkosPorModelo.thenAccept(result -> {
            System.out.println("Funkos agrupados por modelos:");
            result.forEach((model, funkos) -> {
                System.out.println("Modelo: " + model);
                funkos.forEach(System.out::println);
            });
        });

        // Número de funkos por modelos
        CompletableFuture<Map<String, Long>> cantidadFunkosPorModelo = funkoService.countByModel();
        cantidadFunkosPorModelo.thenAccept(result -> {
            System.out.println("Número de funkos por modelos:");
            result.forEach((model, count) -> System.out.println("Modelo: " + model + ", Cantidad: " + count));
        });

        // Funkos que han sido lanzados en 2023
        CompletableFuture<List<Funko>> funkos2023 = funkoService.findByReleaseYear(2023);
        funkos2023.thenAccept(result -> {
            System.out.println("Funkos lanzados en 2023:");
            result.forEach(System.out::println);
        });

        // Número de funkos de Stitch
        CompletableFuture<Long> cantidadStitch = funkoService.countByName("Stitch");
        cantidadStitch.thenAccept(result -> System.out.println("Número de funkos de Stitch: " + result));

        // Listado de funkos de Stitch
        CompletableFuture<List<Funko>> funkosStitch = funkoService.findByName("Stitch");
        funkosStitch.thenAccept(result -> {
            System.out.println("Listado de funkos de Stitch:");
            result.forEach(System.out::println);
        });

        // Para esperar a que todas las consultas asincrónicas terminen antes de finalizar el programa
        CompletableFuture.allOf(
                funkoMasCaro, mediaPrecio, funkosPorModelo, cantidadFunkosPorModelo, funkos2023, cantidadStitch, funkosStitch
        ).join();
    }
    }
}