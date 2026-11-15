package org.tpnba.modelo;

import org.tpnba.interfaces.Identificable;
import org.tpnba.excepciones.DorsalDuplicadoException;
import org.tpnba.excepciones.EntidadNoEncontrada;
import org.tpnba.excepciones.PlantelCompletoException;
import org.tpnba.interfaces.Identificable;
import org.tpnba.interfaces.Reportable;
import org.tpnba.enums.Conferencia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Equipo de la NBA. Tiene un entrenador y un plantel de hasta 15 jugadores.
 * Implementa Identificable (tiene id) y Reportable (genera el informe del plantel).
 */
public class Equipo implements Identificable, Reportable {

    public static final int MAX_JUGADORES = 15;

    private int id;
    private String nombre;
    private String ciudad;
    private Conferencia conferencia;
    private Entrenador entrenador;              // puede ser null si todavia no tiene
    private final List<Jugador> plantel;        // agregacion: los jugadores existen por si solos

    public Equipo(int id, String nombre, String ciudad, Conferencia conferencia) {
        this.id = id;
        setNombre(nombre);
        setCiudad(ciudad);
        setConferencia(conferencia);
        this.plantel = new ArrayList<>();
    }

    // ----------------- Gestion del plantel -----------------

    /**
     * Agrega un jugador al plantel.
     * @throws PlantelCompletoException si el plantel ya tiene MAX_JUGADORES
     * @throws DorsalDuplicadoException si otro jugador del equipo ya usa ese dorsal
     */
    public void agregarJugador(Jugador jugador) throws PlantelCompletoException, DorsalDuplicadoException {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser null.");
        }
        if (plantel.size() >= MAX_JUGADORES) {
            throw new PlantelCompletoException(nombre, MAX_JUGADORES);
        }
        if (buscarJugador(jugador.getDorsal()) != null) {
            throw new DorsalDuplicadoException(jugador.getDorsal(), nombre);
        }
        plantel.add(jugador);
    }

    /**
     * Quita del plantel al jugador con el dorsal indicado.
     * @throws EntidadNoEncontrada si ningun jugador usa ese dorsal
     */
    public Jugador quitarJugador(int dorsal) throws EntidadNoEncontrada{
        Jugador jugador = buscarJugador(dorsal);
        if (jugador == null) {
            throw new EntidadNoEncontrada("No hay ningun jugador con el dorsal " + dorsal + " en " + nombre + ".");
        }
        plantel.remove(jugador);
        return jugador;
    }

    /** @return el jugador con ese dorsal, o null si no hay ninguno */
    public Jugador buscarJugador(int dorsal) {
        for (Jugador j : plantel) {
            if (j.getDorsal() == dorsal) {
                return j;
            }
        }
        return null;
    }

    public int getCantidadJugadores() {
        return plantel.size();
    }

    /**
     * Devuelve el plantel en una lista de solo lectura.
     * Asi nadie puede hacer equipo.getPlantel().add(...) salteando las validaciones.
     */
    public List<Jugador> getPlantel() {
        return Collections.unmodifiableList(plantel);
    }

    // ----------------- Reportable -----------------

    /** Genera el informe del equipo con su plantel ordenado por dorsal. */
    @Override
    public String generarReporte() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(getNombreCompleto()).append(" (").append(conferencia).append(") ===\n");
        sb.append(entrenador != null ? entrenador.getDescripcion() : "Sin entrenador asignado").append("\n");
        sb.append("Plantel (").append(plantel.size()).append("/").append(MAX_JUGADORES).append("):\n");

        List<Jugador> ordenado = new ArrayList<>(plantel);
        ordenado.sort(Comparator.comparingInt(Jugador::getDorsal));
        for (Jugador j : ordenado) {
            sb.append("  ").append(j.getDescripcion()).append("\n");
        }
        return sb.toString();
    }

    // ----------------- Getters y setters -----------------

    @Override
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del equipo no puede estar vacio.");
        }
        this.nombre = nombre.trim();
    }

    public String getCiudad() { return ciudad; }

    public void setCiudad(String ciudad) {
        if (ciudad == null || ciudad.isBlank()) {
            throw new IllegalArgumentException("La ciudad no puede estar vacia.");
        }
        this.ciudad = ciudad.trim();
    }

    /** @return ciudad + nombre, por ejemplo "Golden State Warriors" */
    public String getNombreCompleto() {
        return ciudad + " " + nombre;
    }

    public Conferencia getConferencia() {return conferencia; }

    public void setConferencia(Conferencia conferencia) {
        if (conferencia == null) {
            throw new IllegalArgumentException("La conferencia es obligatoria.");
        }
        this.conferencia = conferencia;
    }

    public Entrenador getEntrenador() { return entrenador; }

    public void setEntrenador(Entrenador entrenador) { this.entrenador = entrenador; }

    // ----------------- equals, hashCode y toString -----------------

    /** Dos equipos son iguales si tienen el mismo id. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Equipo)) return false;
        Equipo otro = (Equipo) o;
        return id == otro.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getNombreCompleto();
    }
}

