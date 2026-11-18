package interfaces;

/**
 * Representa una entidad capaz de generar un informe de texto con sus datos.
 * <p>
 * La implementa Equipo, que genera el informe de su plantel.
 */
public interface Reportable {

    /**
     * Genera un informe legible con la informacion de la entidad.
     *
     * @return el informe en formato de texto
     */
    String generarReporte();
}
