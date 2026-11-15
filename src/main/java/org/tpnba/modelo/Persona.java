package org.tpnba.modelo;

import org.tpnba.interfaces.Identificable;

import java.time.LocalDate;
import java.time.Period;

public abstract class Persona implements Identificable {

    protected int id;
    protected String nombre;
    protected String apellido;
    protected LocalDate fechaNacimiento;
    protected String nacionalidad;

    public Persona(int id, String nombre, String apellido, LocalDate fechaNacimiento, String nacionalidad) {
        this.id = id;
        setNombre (nombre);
        setApellido(apellido);
        setFechaNacimiento(fechaNacimiento);
        this.nacionalidad = nacionalidad;
    }

    /**
     * Metodo abstracto: cada tipo de persona se describe distinto.
     * Es el punto de polimorfismo de esta jerarquia.
     */
    public abstract String getDescripcion();

    /** Calcula la edad actual a partir de la fecha de nacimiento. */
    public int getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    // ----------------- Getters y setters -----------------

    @Override
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }
        this.nombre = nombre.trim();
    }

    public String getApellido() { return apellido; }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido no puede estar vacio.");
        }
        this.apellido = apellido.trim();
    }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null || fechaNacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no es valida.");
        }
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNacionalidad() { return nacionalidad; }

    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    @Override
    public String toString() { return getDescripcion(); }
}

