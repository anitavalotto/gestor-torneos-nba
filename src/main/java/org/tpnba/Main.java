package org.tpnba;

import org.tpnba.enums.Conferencia;
import org.tpnba.enums.Posicion;
import org.tpnba.excepciones.TorneoException;
import org.tpnba.modelo.Equipo;
import org.tpnba.modelo.Jugador;
import org.tpnba.persistencia.EquipoDAO;
import org.tpnba.persistencia.JugadorDAO;

import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        EquipoDAO equipoDAO = new EquipoDAO();
        JugadorDAO jugadorDAO = new JugadorDAO();

        try {
            equipoDAO.insertar(new Equipo(1, "Warriors", "Golden State", Conferencia.OESTE));
            jugadorDAO.insertar(new Jugador(1, "Stephen", "Curry", LocalDate.of(1988, 3, 14),
                    "Estados Unidos", 30, Posicion.BASE, 1.88));
            jugadorDAO.insertar(new Jugador(2, "Draymond", "Green", LocalDate.of(1990, 3, 4),
                    "Estados Unidos", 23, Posicion.ALA_PIVOT, 1.98));
            jugadorDAO.insertar(new Jugador(3, "LeBron", "James", LocalDate.of(1984, 12, 30),
                    "Estados Unidos", 23, Posicion.ALERO, 2.06));
            System.out.println("Jugadores en la base: " + jugadorDAO.listarTodos().size());

            jugadorDAO.asignarEquipo(1, 1);
            jugadorDAO.asignarEquipo(2, 1);
            System.out.println("Plantel del equipo 1 (desde la base):");
            for (Jugador j : jugadorDAO.listarPorEquipo(1)) {
                System.out.println("  " + j);
            }

            Jugador curry = jugadorDAO.buscaPorId(1);
            curry.setAltura(1.90);
            jugadorDAO.actualizar(curry);
            System.out.println("Tras actualizar: " + jugadorDAO.buscaPorId(1));

            // El programa une las piezas: el modelo (Equipo) no conoce al DAO
            Equipo warriors = equipoDAO.buscaPorId(1);
            for (Jugador j : jugadorDAO.listarPorEquipo(1)) {
                warriors.agregarJugador(j);
            }
            System.out.println(warriors.generarReporte());
        } catch (TorneoException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Casos de error: cada uno informa y el programa sigue
        try {
            jugadorDAO.buscaPorId(99);
        } catch (TorneoException | SQLException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
        try {
            jugadorDAO.actualizar(new Jugador(99, "Fantasma", "Ninguno", LocalDate.of(2000, 1, 1),
                    "Ninguna", 1, Posicion.BASE, 1.80));
        } catch (TorneoException | SQLException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
        try {
            jugadorDAO.eliminar(99);
        } catch (TorneoException | SQLException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
        try {
            jugadorDAO.asignarEquipo(99, 1);
        } catch (TorneoException | SQLException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
        try {
            jugadorDAO.insertar(new Jugador(1, "Repetido", "Id", LocalDate.of(2000, 1, 1),
                    "Ninguna", 5, Posicion.PIVOT, 2.00));          // id 1 ya existe
        } catch (SQLException e) {
            System.out.println("Error esperado (SQL): " + e.getMessage());
        }
        try {
            jugadorDAO.asignarEquipo(3, 1);                         // dorsal 23 ya esta en uso en el equipo 1
        } catch (TorneoException | SQLException e) {
            System.out.println("Error esperado (SQL): " + e.getMessage());
        }
        try {
            jugadorDAO.asignarEquipo(1, 99);                        // el equipo 99 no existe
        } catch (TorneoException | SQLException e) {
            System.out.println("Error esperado (SQL): " + e.getMessage());
        }

        // Limpieza: deja las tablas vacias para poder correr el programa de nuevo
        try {
            jugadorDAO.eliminar(1);
            jugadorDAO.eliminar(2);
            jugadorDAO.eliminar(3);
            equipoDAO.eliminar(1);
            System.out.println("Jugadores al final: " + jugadorDAO.listarTodos().size());
            System.out.println("Equipos al final: " + equipoDAO.listarTodos().size());
        } catch (TorneoException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}


