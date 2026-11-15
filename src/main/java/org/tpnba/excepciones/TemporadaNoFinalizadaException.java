package org.tpnba.excepciones;
/** Se lanza al intentar generar los playoffs antes de terminar la temporada regular. */
public class TemporadaNoFinalizadaException extends TorneoException {
    public TemporadaNoFinalizadaException(int partidosPendientes) {
        super("No se pueden generar los playoffs: quedan " + partidosPendientes
                + " partidos de temporada regular.");
    }
}
