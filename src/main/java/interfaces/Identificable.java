package interfaces;

/**
 * Representa cualquier entidad del sistema que tiene un identificador unico.
 * <p>
 * La implementan Persona (y por herencia Jugador y Entrenador), Equipo y Partido.
 * Gracias a esta interfaz, la clase generica {@code Gestor<T extends Identificable>}
 * puede buscar y eliminar elementos por id sin saber de que tipo concreto son.
 */
public interface Identificable {

    /**
     * Devuelve el identificador unico de la entidad.
     *
     * @return el id de la entidad
     */
    int getId();
}
