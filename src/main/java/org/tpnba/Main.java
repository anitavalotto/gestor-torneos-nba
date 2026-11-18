package org.tpnba;

import org.tpnba.enums.Conferencia;
import org.tpnba.enums.Fase;
import org.tpnba.excepciones.EntidadDuplicadaException;
import org.tpnba.excepciones.EntidadNoEncontradaException;
import org.tpnba.excepciones.TorneoException;
import org.tpnba.genericos.Repositorio;
import org.tpnba.modelo.*;
import org.tpnba.enums.Posicion;
import org.tpnba.persistencia.ConexionBD;
import org.tpnba.persistencia.EquipoDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Main {
    static void main(String[] args) {
        EquipoDAO dao = new EquipoDAO();

        try {
            dao.insertar(new Equipo(1, "Warriors", "Golden State", Conferencia.OESTE));
            dao.insertar(new Equipo(2, "Lakers", "Los Angeles", Conferencia.OESTE));
            dao.insertar(new Equipo(3, "Celtics", "Boston", Conferencia.ESTE));
            System.out.println("Equipos en la base: " + dao.listarTodos());

            Equipo lakers = dao.buscaPorId(2);
            System.out.println("Busqueda id 2: " + lakers.getNombreCompleto() + " - " + lakers.getConferencia());

            lakers.setCiudad("L.A.");
            dao.actualizar(lakers);
            System.out.println("Tras actualizar: " + dao.buscaPorId(2));

            dao.eliminar(3);
            System.out.println("Tras eliminar id 3: " + dao.listarTodos());
        } catch (TorneoException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Casos de error: cada uno informa y el programa sigue
        try {
            dao.buscaPorId(99);
        } catch (TorneoException | SQLException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
        try {
            dao.actualizar(new Equipo(99, "Fantasma", "Ninguna", Conferencia.ESTE));
        } catch (TorneoException | SQLException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
        try {
            dao.eliminar(99);
        } catch (TorneoException | SQLException e) {
            System.out.println("Error esperado: " + e.getMessage());
        }
        try {
            dao.insertar(new Equipo(1, "Repetido", "Ciudad", Conferencia.ESTE));   // id 1 ya existe
        } catch (SQLException e) {
            System.out.println("Error esperado (SQL): " + e.getMessage());
        }

        // Limpieza: deja la tabla vacia para poder correr el programa de nuevo
        try {
            dao.eliminar(1);
            dao.eliminar(2);
            System.out.println("Equipos al final: " + dao.listarTodos());
        } catch (TorneoException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}


