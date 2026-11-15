package org.tpnba.modelo;

import org.tpnba.enums.Posicion;

import java.time.LocalDate;

/**
 * Jugador de basquet. Hereda los datos personales de Persona y agrega
 * los datos deportivos: dorsal, posicion y altura.
 */

public class Jugador extends Persona{

    public static final int DORSAL_MIN = 0;
    public static final int DORSAL_MAX = 99;

    private int dorsal;
    private Posicion posicion;
    private double altura; // en metros

    public Jugador(int id, String nombre, String apellido, LocalDate fechaNacimiento,
                   String nacionalidad, int dorsal, Posicion posicion, double altura) {
        super(id, nombre, apellido, fechaNacimiento, nacionalidad); // constructor de Persona
        setDorsal(dorsal);
        setPosicion(posicion);
        setAltura(altura);
    }

    /** Implementacion propia del metodo abstracto de Persona. */
    @Override
    public String getDescripcion() {
        return "#" + dorsal + " " + getNombreCompleto() + " - " + posicion.getAbreviatura()
                + " - " + altura + " m - " + getEdad() + " anios";
    }

    // ----------------- Getters y setters con validacion -----------------

    public int getDorsal() { return dorsal; }

    public void setDorsal(int dorsal) {
        if (dorsal < DORSAL_MIN || dorsal > DORSAL_MAX) {
            throw new IllegalArgumentException("El dorsal debe estar entre " + DORSAL_MIN + " y " + DORSAL_MAX + ".");
        }
        this.dorsal = dorsal;
    }

    public Posicion getPosicion() { return posicion; }

    public void setPosicion(Posicion posicion) {
        if (posicion == null) {
            throw new IllegalArgumentException("La posicion es obligatoria.");
        }
        this.posicion = posicion;
    }

    public double getAltura() { return altura; }

    public void setAltura(double altura) {
        if (altura < 1.50 || altura > 2.50) {
            throw new IllegalArgumentException("La altura debe estar entre 1.50 y 2.50 metros.");
        }
        this.altura = altura;
    }
}

