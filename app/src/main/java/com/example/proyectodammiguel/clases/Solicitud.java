package com.example.proyectodammiguel.clases;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Solicitud implements Serializable {
    private String id;
    private Taxista taxista;
    private LocalDateTime fecha;
    private User cliente;
    private Double latitud;
    private Double longitud;


    public Solicitud() {

    }

    public Solicitud(Taxista taxista, LocalDateTime fecha, User cliente, Double latitud, Double longitud) {
        this.taxista = taxista;
        this.fecha = fecha;
        this.cliente = cliente;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Solicitud(String id, Taxista taxista, LocalDateTime fecha, User cliente, Double latitud, Double longitud) {
        this.id = id;
        this.taxista = taxista;
        this.fecha = fecha;
        this.cliente = cliente;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Taxista getTaxista() {
        return taxista;
    }

    public void setTaxista(Taxista taxista) {
        this.taxista = taxista;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public User getCliente() {
        return cliente;
    }

    public void setCliente(User cliente) {
        this.cliente = cliente;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
}
