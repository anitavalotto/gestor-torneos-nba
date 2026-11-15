package org.tpnba.excepciones;
/**
 * Excepcion base del sistema. Todas las excepciones propias heredan de esta clase.
 * Extiende Exception (checked), por lo que el compilador obliga a manejarlas
 * con try/catch o a declararlas con throws.
 */

public class TorneoException extends Exception {
    public TorneoException(String message) {
        super(message);
    }
}
