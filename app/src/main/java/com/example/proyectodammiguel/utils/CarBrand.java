package com.example.proyectodammiguel.utils;

public class CarBrand {
    private final String name;
    private final int icon;

    public CarBrand(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }
}
