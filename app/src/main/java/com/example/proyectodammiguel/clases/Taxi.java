package com.example.proyectodammiguel.clases;

import java.util.HashMap;

public class Taxi {

    private String marca = "";
    private String modelo = "";
    private int numPlazas;
    private String tipoCombustible;

    private Taxista taxista;

    public Taxi() {

    }

    public Taxi(String marca, String modelo, int numPlazas, Taxista taxista, String tipoCombustible) {
        this.marca = marca;
        this.modelo = modelo;
        this.numPlazas = numPlazas;
        this.taxista = taxista;
        this.tipoCombustible = tipoCombustible;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getNumPlazas() {
        return numPlazas;
    }

    public void setNumPlazas(int numPlazas) {
        this.numPlazas = numPlazas;
    }

    public Taxista getTaxista() {
        return taxista;
    }

    public void setTaxista(Taxista taxista) {
        this.taxista = taxista;
    }

    public String getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(String tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

    @Override
    public String toString() {
        return "Taxi{" +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", numPlazas=" + numPlazas +
                ", tipoCombustible='" + tipoCombustible + '\'' +
                ", taxista=" + taxista +
                '}';
    }

//Map<String, String> map = new HashMap<String, String>();
}
