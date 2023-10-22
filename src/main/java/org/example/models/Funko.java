package org.example.models;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder

public class Funko {
    @Builder.Default
    private long id = IdGenerator.getInstance().generateId();

    @Builder.Default
    private UUID cod = UUID.randomUUID();
    private String nombre;
    private Modelo modelo;
    private double precio;
    private LocalDate fechaLanzamiento;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();


    public Funko(long id, UUID cod, String nombre, Modelo modelo, double precio, LocalDate fechaLanzamiento, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.cod = cod;
        this.nombre = nombre;
        this.modelo = modelo;
        this.precio = precio;
        this.fechaLanzamiento = fechaLanzamiento;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public enum Modelo {
        MARVEL, DISNEY, ANIME, OTROS
    }

}
