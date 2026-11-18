package gestor;

import excepciones.EntidadDuplicadaException;
import excepciones.EntidadNoEncontradaException;
import interfaces.Identificable;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor generico en memoria: guarda elementos de cualquier tipo T que sea
 * Identificable (Equipo, Partido...).
 * <p>
 * Es la clase generica que pide el enunciado: se escribe una sola vez y sirve
 * para todas las entidades, con chequeo de tipos en tiempo de compilacion.
 * La cota {@code T extends Identificable} garantiza que todo elemento tiene
 * getId(), y por eso el gestor puede buscar y eliminar por id sin saber de que
 * clase concreta es. Las subclases (GestorEquipos, GestorPartidos) heredan todo
 * esto y agregan las operaciones propias de su entidad.
 *
 * @param <T> tipo de elemento que gestiona (debe implementar Identificable)
 */
public class Gestor<T extends Identificable> {

    /** protected: las subclases recorren la lista para armar sus propias consultas. */
    protected List<T> elementos;

    private final String nombreEntidad;   // solo para los mensajes de error: "equipo", "partido"...

    /**
     * Crea un gestor vacio.
     *
     * @param nombreEntidad nombre del tipo de elemento, en singular y minuscula
     *                      (por ejemplo "equipo"); se usa en los mensajes de error
     * @throws IllegalArgumentException si el nombre esta vacio
     */
    public Gestor(String nombreEntidad) {
        if (nombreEntidad == null || nombreEntidad.isBlank()) {
            throw new IllegalArgumentException("El nombre de la entidad no puede estar vacio.");
        }
        this.nombreEntidad = nombreEntidad.trim();
        this.elementos = new ArrayList<>();
    }

    /**
     * Agrega un elemento al gestor.
     *
     * @param elemento elemento a guardar
     * @throws EntidadDuplicadaException si ya hay un elemento con el mismo id
     * @throws IllegalArgumentException  si el elemento es null
     */
    public void agregar(T elemento) throws EntidadDuplicadaException {
        if (elemento == null) {
            throw new IllegalArgumentException("El elemento no puede ser null.");
        }
        if (existe(elemento.getId())) {
            throw new EntidadDuplicadaException(nombreEntidad, elemento.getId());
        }
        elementos.add(elemento);
    }

    /** @return true si hay un elemento con ese id */
    public boolean existe(int id) {
        for (T elemento : elementos) {
            if (elemento.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Busca un elemento por su id.
     *
     * @param id id a buscar
     * @return el elemento encontrado
     * @throws EntidadNoEncontradaException si no hay ningun elemento con ese id
     */
    public T buscar(int id) throws EntidadNoEncontradaException {
        for (T elemento : elementos) {
            if (elemento.getId() == id) {
                return elemento;
            }
        }
        throw new EntidadNoEncontradaException(nombreEntidad, id);
    }

    /**
     * Elimina el elemento con el id indicado.
     *
     * @param id id del elemento a eliminar
     * @throws EntidadNoEncontradaException si no hay ningun elemento con ese id
     */
    public void eliminar(int id) throws EntidadNoEncontradaException {
        T elemento = buscar(id);   // si no existe, buscar ya lanza la excepcion
        elementos.remove(elemento);
    }

    /**
     * Devuelve una COPIA de la lista de elementos. Si quien la recibe agrega o
     * quita cosas, el gestor no se entera: la lista interna queda protegida
     * (encapsulamiento).
     */
    public List<T> listar() {
        return new ArrayList<>(elementos);
    }

    /** @return cantidad de elementos guardados */
    public int cantidad() {
        return elementos.size();
    }
}
