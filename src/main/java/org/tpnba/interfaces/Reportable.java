package org.tpnba.interfaces;

/**
 * Entidad capaz de generar un informe de texto con sus datos.
 * La implementan Equipo (plantel) y Torneo (tabla o cuadro de playoffs).
 */
public interface Reportable {

    /** @return el informe en formato de texto */
    String generarReporte();
}