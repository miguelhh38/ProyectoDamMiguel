package com.example.proyectodammiguel.clases;

import java.io.Serializable;

public class Taxista implements Serializable {

    private User user;
    private Agrupacion agrupacion;
    private int calificacion;

    public Taxista() {

    }

    public Taxista(User user, Agrupacion agrupacion) {
        this.user = user;
        this.agrupacion = agrupacion;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Agrupacion getAgrupacion() {
        return agrupacion;
    }

    public void setAgrupacion(Agrupacion agrupacion) {
        this.agrupacion = agrupacion;
    }
}
