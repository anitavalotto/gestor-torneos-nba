package org.tpnba;

import org.tpnba.enums.Conferencia;
import org.tpnba.excepciones.TorneoException;
import org.tpnba.modelo.Entrenador;
import org.tpnba.modelo.Equipo;
import org.tpnba.modelo.Jugador;
import org.tpnba.modelo.Persona;
import org.tpnba.enums.Posicion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Main {
    static void main(String[] args) {

        Equipo warriors = new Equipo(1, "Warriors", "Golden State", Conferencia.OESTE);
        warriors.setEntrenador(new Entrenador(100, "Steve", "Kerr", LocalDate.of(1965, 9, 27), "USA", 11));

        try {
            warriors.agregarJugador(new Jugador(1, "Stephen", "Curry", LocalDate.of(1988, 3, 14), "USA", 30, Posicion.BASE, 1.88));
            warriors.agregarJugador(new Jugador(2, "Draymond", "Green", LocalDate.of(1990, 3, 4), "USA", 23, Posicion.ALA_PIVOT, 1.98));
            warriors.agregarJugador(new Jugador(3, "Jimmy", "Butler", LocalDate.of(1989, 9, 14), "USA", 10, Posicion.ALERO, 2.01));
        } catch (TorneoException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println(warriors.generarReporte());

        // Prueba 1: dorsal repetido
        try {
            warriors.agregarJugador(new Jugador(4, "Otro", "Jugador", LocalDate.of(2000, 1, 1), "ARG", 30, Posicion.ESCOLTA, 1.95));
        } catch (TorneoException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

        // Prueba 2: plantel completo (completamos hasta 15 y probamos uno mas)
        try {
            for (int dorsal = 40; warriors.getCantidadJugadores() < Equipo.MAX_JUGADORES; dorsal++) {
                warriors.agregarJugador(new Jugador(dorsal, "Relleno", "N" + dorsal, LocalDate.of(2000, 1, 1), "USA", dorsal, Posicion.ALERO, 2.00));
            }
            warriors.agregarJugador(new Jugador(99, "Uno", "Mas", LocalDate.of(2000, 1, 1), "USA", 99, Posicion.PIVOT, 2.10));
        } catch (TorneoException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }

        // Prueba 3: quitar un dorsal que no existe
        try {
            warriors.quitarJugador(77);
        } catch (TorneoException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
    }

}

