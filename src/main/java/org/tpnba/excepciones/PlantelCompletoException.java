package org.tpnba.excepciones;

/** Se lanza al intentar agregar un jugador a un equipo que ya tiene el plantel completo. */

public class PlantelCompletoException extends TorneoException {
    public PlantelCompletoException(String nombreEquipo, int maximo) {
        super("El plantel de " + nombreEquipo + " ya tiene el maximo de " + maximo + " jugadores.");
    }
}
