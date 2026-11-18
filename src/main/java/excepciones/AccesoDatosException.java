package excepciones;

/**
 * Se lanza cuando falla el acceso a la base de datos: servidor caido, credenciales
 * incorrectas, restriccion violada (por ejemplo, un id repetido), etc.
 * <p>
 * Envuelve la SQLException original como causa. Asi las capas de arriba (gestores
 * y menu) no dependen de detalles tecnicos de JDBC y, a la vez, no se pierde
 * el rastro del error: getCause() devuelve la excepcion original.
 */
public class AccesoDatosException extends TorneoException {

    public AccesoDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
