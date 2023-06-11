package com.example.proyectodammiguel.clases;

import android.location.Address;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Agrupacion implements Serializable {
    private String name;
    private double latitud;
    private double longitud;

    public Agrupacion() {

    }

    public Agrupacion(String name, double latitud, double longitud) {
        this.name = name;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return name;
    }
}
