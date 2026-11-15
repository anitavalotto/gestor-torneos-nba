package org.tpnba.excepciones;
/** Se lanza al intentar cargar el resultado de un partido que ya fue finalizado. */
public class PartidoYaJugadoException extends TorneoException {
    public PartidoYaJugadoException(int idPartido) {
        super("El partido " +idPartido+ "ya tiene resultado cargado");
    }
}
