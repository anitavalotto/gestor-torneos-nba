package org.tpnba;

import org.tpnba.enums.Conferencia;
import org.tpnba.enums.Fase;
import org.tpnba.excepciones.EntidadDuplicadaException;
import org.tpnba.excepciones.EntidadNoEncontradaException;
import org.tpnba.excepciones.TorneoException;
import org.tpnba.genericos.Repositorio;
import org.tpnba.modelo.*;
import org.tpnba.enums.Posicion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Main {
    static void main(String[] args) {

        Equipo warriors = new Equipo(1, "Warriors", "Golden State", Conferencia.OESTE);
        Equipo lakers = new Equipo(2, "Lakers", "Los Angeles", Conferencia.OESTE);
        Equipo celtics = new Equipo(3, "Celtics", "Boston", Conferencia.ESTE);

        // ===== PARTIDO =====
        System.out.println("--- Partido ---");
        Partido p1 = new Partido(1, warriors, lakers, LocalDate.of(2026, 10, 21), Fase.REGULAR);
        System.out.println(p1);
        try {
            p1.registrarResultado(118, 112);
            System.out.println(p1);
            System.out.println("Ganador: " + p1.getGanador());
        } catch (TorneoException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            p1.registrarResultado(100, 90);               // ya jugado
        } catch (TorneoException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

        Partido p2 = new Partido(2, lakers, warriors, LocalDate.of(2026, 10, 28), Fase.REGULAR);
        try {
            p2.registrarResultado(105, 105);              // empate
        } catch (TorneoException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
        System.out.println(p2);                           // sigue programado

        try {
            new Partido(3, warriors, warriors, LocalDate.now(), Fase.REGULAR);
        } catch (IllegalArgumentException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

        try {
            p2.getGanador();                              // todavia no se jugo
        } catch (IllegalStateException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

        // ===== REPOSITORIO DE EQUIPOS =====
        System.out.println("\n--- Repositorio<Equipo> ---");
        Repositorio<Equipo> equipos = new Repositorio<>("equipo");
        try {
            equipos.agregar(warriors);
            equipos.agregar(lakers);
            equipos.agregar(celtics);
            System.out.println("Cantidad: " + equipos.cantidad());
            System.out.println("Busqueda id 2: " + equipos.buscarPorId(2));
            equipos.eliminar(2);
            System.out.println("Tras eliminar id 2: " + equipos.listar());
        } catch (TorneoException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            equipos.buscarPorId(2);                       // ya eliminado
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
        try {
            equipos.eliminar(99);                         // nunca existio
        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
        try {
            equipos.agregar(warriors);                    // id repetido
        } catch (EntidadDuplicadaException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
        try {
            equipos.agregar(null);
        } catch (IllegalArgumentException | EntidadDuplicadaException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

        equipos.listar().clear();                         // vacia la COPIA, no el repositorio
        System.out.println("Cantidad tras modificar la copia: " + equipos.cantidad());

        // ===== REPOSITORIO DE PARTIDOS: el MISMO codigo, otro tipo =====
        System.out.println("\n--- Repositorio<Partido> (mismo codigo, otro tipo) ---");
        Repositorio<Partido> partidos = new Repositorio<>("partido");
        try {
            partidos.agregar(p1);
            partidos.agregar(p2);
            System.out.println("Cantidad: " + partidos.cantidad());
            System.out.println("Partido id 1: " + partidos.buscarPorId(1));
        } catch (TorneoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}

