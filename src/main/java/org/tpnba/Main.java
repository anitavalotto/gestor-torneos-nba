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

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class Main {
    static void main(String[] args) {

        try (Connection con = ConexionBD.getConexion()) {
            System.out.println("Conectado a la base: " + con.getCatalog());
            System.out.println("Motor: " + con.getMetaData().getDatabaseProductName()
                    + " " + con.getMetaData().getDatabaseProductVersion());
        } catch (SQLException e) {
            System.out.println("No se pudo conectar: " + e.getMessage());
        }
    }
}

