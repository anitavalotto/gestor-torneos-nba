package gestor;

import dao.EquipoDAO;
import dao.JugadorDAO;
import enums.Conferencia;
import excepciones.AccesoDatosException;
import excepciones.DorsalDuplicadoException;
import excepciones.EntidadNoEncontradaException;
import excepciones.PlantelCompletoException;
import interfaces.DAO;
import modelo.Equipo;
import modelo.Jugador;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de equipos: hereda de Gestor&lt;Equipo&gt; la administracion en memoria y
 * agrega (1) las consultas propias de los equipos y (2) la persistencia en
 * MySQL a traves de los DAO.
 * <p>
 * Es el unico punto de contacto entre el menu y la base de datos: el Main nunca
 * habla con los DAO directamente. Los equipos y sus jugadores viven en dos
 * lugares: la lista en memoria (heredada) y las tablas de MySQL. Los metodos
 * "EnBD" operan sobre la base.
 */
public class GestorEquipos extends Gestor<Equipo> {

    // Se programa contra el contrato DAO<Equipo>; JugadorDAO se usa directo porque
    // ofrece operaciones propias del plantel (listarPorEquipo, insertarEnEquipo).
    private final DAO<Equipo> equipoDAO = new EquipoDAO();
    private final JugadorDAO jugadorDAO = new JugadorDAO();

    public GestorEquipos() {
        super("equipo");
    }

    // ----------------- Operaciones en memoria -----------------

    /**
     * Agrega un jugador al plantel de un equipo ya cargado en el gestor.
     * Las reglas (plantel de 15 como maximo, dorsal unico) las aplica el modelo.
     *
     * @throws EntidadNoEncontradaException si no existe el equipo
     * @throws PlantelCompletoException     si el plantel ya tiene 15 jugadores
     * @throws DorsalDuplicadoException     si otro jugador del equipo usa ese dorsal
     */
    public void agregarJugador(int equipoId, Jugador jugador)
            throws EntidadNoEncontradaException, PlantelCompletoException, DorsalDuplicadoException {
        Equipo equipo = buscar(equipoId);
        equipo.agregarJugador(jugador);
    }

    /** @return los equipos de la conferencia indicada */
    public List<Equipo> listarPorConferencia(Conferencia conferencia) {
        List<Equipo> resultado = new ArrayList<>();
        for (Equipo equipo : elementos) {
            if (equipo.getConferencia() == conferencia) {   // los enums se comparan con ==
                resultado.add(equipo);
            }
        }
        return resultado;
    }

    /** @return cantidad total de jugadores de todos los equipos */
    public int contarJugadores() {
        int total = 0;
        for (Equipo equipo : elementos) {
            total += equipo.getCantidadJugadores();
        }
        return total;
    }

    // ----------------- Operaciones en la base de datos (JDBC) -----------------

    /**
     * Guarda un equipo y todo su plantel en la base.
     *
     * @throws AccesoDatosException si falla la base (por ejemplo, el id ya existe)
     */
    public void guardarEnBD(Equipo equipo) throws AccesoDatosException {
        equipoDAO.insertar(equipo);
        for (Jugador jugador : equipo.getPlantel()) {
            jugadorDAO.insertarEnEquipo(jugador, equipo.getId());
        }
    }

    /** @return todos los equipos de la base, cada uno con su plantel cargado */
    public List<Equipo> listarDesdeBD() throws AccesoDatosException {
        List<Equipo> equipos = equipoDAO.listarTodos();
        for (Equipo equipo : equipos) {
            cargarPlantel(equipo);
        }
        return equipos;
    }

    /** @return el equipo de la base con su plantel cargado */
    public Equipo buscarEnBD(int id) throws AccesoDatosException, EntidadNoEncontradaException {
        Equipo equipo = equipoDAO.buscarPorId(id);
        cargarPlantel(equipo);
        return equipo;
    }

    /** Actualiza en la base los datos propios del equipo (nombre, ciudad, conferencia). */
    public void actualizarEnBD(Equipo equipo) throws AccesoDatosException, EntidadNoEncontradaException {
        equipoDAO.actualizar(equipo);
    }

    /** Elimina el equipo de la base. Sus jugadores quedan sin equipo (ON DELETE SET NULL). */
    public void eliminarDeBD(int id) throws AccesoDatosException, EntidadNoEncontradaException {
        equipoDAO.eliminar(id);
    }

    /** Elimina un jugador de la base. */
    public void eliminarJugadorDeBD(int jugadorId) throws AccesoDatosException, EntidadNoEncontradaException {
        jugadorDAO.eliminar(jugadorId);
    }

    /**
     * Metodo auxiliar privado: arma el plantel de un equipo leido de la base.
     * El modelo no conoce el DAO: el gestor le entrega cada jugador a Equipo.agregarJugador.
     */
    private void cargarPlantel(Equipo equipo) throws AccesoDatosException {
        try {
            for (Jugador jugador : jugadorDAO.listarPorEquipo(equipo.getId())) {
                equipo.agregarJugador(jugador);
            }
        } catch (PlantelCompletoException | DorsalDuplicadoException e) {
            // La base tiene datos que rompen las reglas del equipo: se informa como error de datos
            throw new AccesoDatosException("Los datos guardados del equipo " + equipo.getNombre()
                    + " no cumplen las reglas del plantel", e);
        }
    }
}
