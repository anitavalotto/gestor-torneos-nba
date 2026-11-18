package excepciones;

/**
 * Excepcion base del sistema. Todas las excepciones propias heredan de esta clase.
 * Extiende Exception (checked), por lo que el compilador obliga a manejarlas
 * con try/catch o a declararlas con throws.
 */
public class TorneoException extends Exception {

    public TorneoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con causa: permite encadenar la excepcion original
     * sin perder su rastro (por ejemplo, una SQLException).
     */
    public TorneoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
