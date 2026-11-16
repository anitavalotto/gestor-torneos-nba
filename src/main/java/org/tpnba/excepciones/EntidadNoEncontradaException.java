package org.tpnba.excepciones;
/** Se lanza cuando se busca por id un equipo, jugador, partido o usuario que no existe. */
public class EntidadNoEncontradaException extends TorneoException {


    /** Busqueda por id. Ej: new EntidadNoEncontradaException("equipo", 7) */
    public EntidadNoEncontradaException(String tipo, int id) {
            super("No se encontro " + tipo + " con id " + id + ".");
        }

        /** Busqueda por otro criterio, con mensaje libre. */
    public EntidadNoEncontradaException(String mensaje) {
            super(mensaje);
        }
}
