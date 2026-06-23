package com.hecos.entity;

public enum Criticidad {
    BAJA("#2ecc71"),
    MODERADA("#f1c40f"),
    ALTA("#e67e22"),
    CRITICA("#e74c3c"),
    BLOQUEANTE("#9b59b6");

    private final String color;

    Criticidad(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
