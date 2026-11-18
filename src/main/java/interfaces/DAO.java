package interfaces;

import excepciones.AccesoDatosException;
import excepciones.EntidadNoEncontradaException;

import java.util.List;

/**
 * Contrato de acceso a datos (patron DAO: Data Access Object).
 * <p>
 * Define las operaciones basicas de persistencia para cualquier entidad T.
 * Cada implementacion (EquipoDAO, JugadorDAO) es la unica clase que conoce el SQL
 * de su tabla. Todas declaran AccesoDatosException (falla tecnica de la base) y,
 * cuando el id puede no existir, EntidadNoEncontradaException (error de negocio).
 *
 * @param <T> tipo de entidad que persiste
 */
public interface DAO<T> {

    /**
     * Inserta una entidad nueva.
     *
     * @throws AccesoDatosException si falla la base (por ejemplo, id repetido)
     */
    void insertar(T entidad) throws AccesoDatosException;

    /**
     * Devuelve todas las entidades guardadas.
     *
     * @return lista de entidades (vacia si no hay ninguna)
     * @throws AccesoDatosException si falla la base
     */
    List<T> listarTodos() throws AccesoDatosException;

    /**
     * Busca una entidad por su id.
     *
     * @throws AccesoDatosException         si falla la base
     * @throws EntidadNoEncontradaException si no existe una entidad con ese id
     */
    T buscarPorId(int id) throws AccesoDatosException, EntidadNoEncontradaException;

    /**
     * Actualiza los datos de una entidad existente.
     *
     * @throws AccesoDatosException         si falla la base
     * @throws EntidadNoEncontradaException si no existe una entidad con ese id
     */
    void actualizar(T entidad) throws AccesoDatosException, EntidadNoEncontradaException;

    /**
     * Elimina la entidad con el id indicado.
     *
     * @throws AccesoDatosException         si falla la base
     * @throws EntidadNoEncontradaException si no existe una entidad con ese id
     */
    void eliminar(int id) throws AccesoDatosException, EntidadNoEncontradaException;
}
