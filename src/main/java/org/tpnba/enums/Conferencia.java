package org.tpnba.enums;

public enum Conferencia {
    ESTE("Conferencia Este"),
    OESTE("Conferencia Oeste");

    private final String nombre;

    Conferencia(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() { return nombre; }

}
