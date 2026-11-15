package org.tpnba.excepciones;
/** Se lanza cuando se asigna un dorsal que ya usa otro jugador del mismo equipo. */
public class DorsalDuplicadoException extends TorneoException {
    public DorsalDuplicadoException(int dorsal, String nombreEquipo) {
        super("El dorsal " + dorsal + " ya esta en uso en " + nombreEquipo + ".");
    }
}
