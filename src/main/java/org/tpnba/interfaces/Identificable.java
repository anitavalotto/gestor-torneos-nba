package org.tpnba.interfaces;

/**
 * Representa cualquier entidad del sistema que tiene un identificador unico.
 * La implementan Persona, Equipo, Partido y Usuario. Gracias a esta interfaz,
 * Repositorio<T extends Identificable> puede buscar por id sin saber el tipo concreto.
 */

public interface Identificable {

    /** @return el identificador unico de la entidad */
    int getId();
}
