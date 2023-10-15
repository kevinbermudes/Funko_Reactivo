package org.example.models;

import com.google.gson.annotations.Expose;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Funko {
    @Expose
    private Long id;

    @Expose
    private final String cod;
    @Expose
    private final String nombre;
    @Expose
    private final String modelo;
    @Expose
    private double precio;
    @Expose
    private final LocalDate fecha;
    @Expose
    private LocalDateTime ultimoAcceso;

    public Funko(Long id, String cod, String nombre, String modelo, Double precio, LocalDate fecha) {
        this.id = id;
        this.cod = cod;
        this.nombre = nombre;
        this.modelo = modelo;
        this.precio = precio;
        this.fecha = fecha;
    }

    public LocalDateTime  getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    @Override
    public String toString() {
        return "Funko{" +
                "id=" + id +
                ", cod='" + cod + '\'' +
                ", nombre='" + nombre + '\'' +
                ", modelo='" + modelo + '\'' +
                ", precio=" + precio +
                ", fecha=" + fecha +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getCod() {
        return cod;
    }

    public String getNombre() {
        return nombre;
    }

    public String getModelo() {
        return modelo;
    }

    public double getPrecio() {
        return precio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setId(long aLong) {
    }
}
