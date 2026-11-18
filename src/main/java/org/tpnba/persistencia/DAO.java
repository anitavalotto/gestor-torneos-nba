package org.tpnba.persistencia;

import org.tpnba.excepciones.EntidadNoEncontradaException;

import java.sql.SQLException;
import java.util.List;

public interface DAO <T> {

    // inserta en la base
    void insertar (T elemento) throws SQLException;

    //actualiza los datos de un elemento existente
    void actualizar (T elemento)throws SQLException //si falla la base
    ,EntidadNoEncontradaException;// si no existe un elemento con ese id;

    // elimina el elemento con el id indicado
    void eliminar (int id) throws SQLException //si falla la base
    ,EntidadNoEncontradaException ;// si no existe un elemento con ese id;

    //busca un elemento por id
    T buscaPorId (int id) throws SQLException //si falla la base
    ,EntidadNoEncontradaException;// si no existe un elemento con ese id;

    //devuelve todas las entidades guardadas

    List<T> listarTodos () throws SQLException;

}
