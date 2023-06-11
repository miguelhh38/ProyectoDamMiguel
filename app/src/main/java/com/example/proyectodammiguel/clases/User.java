package com.example.proyectodammiguel.clases;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String user;
    private String mail;
    private String telf;
    private Tipo tipo;

    public User() {

    }

    public User(String name, String user, String mail, String telf, Tipo tipo) {
        this.name = name;
        this.user = user;
        this.mail = mail;
        this.telf = telf;
        this.tipo = tipo;
    }

    public User(String name, String user, String mail) {
        this.name = name;
        this.user = user;
        this.mail = mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getName() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getMail() {
        return mail;
    }

    public String getTelf() {
        return telf;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTelf(String telf) {
        this.telf = telf;
    }
}
