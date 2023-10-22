package org.example;


import org.example.Controller.FunkoController;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        System.out.println("Funkos");
        System.out.println("======");
        FunkoController controller = new FunkoController();
        controller.run();

    }
}