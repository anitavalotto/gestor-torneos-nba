package dao;

import config.ConexionDB;
import enums.Posicion;
import excepciones.AccesoDatosException;
import excepciones.EntidadNoEncontradaException;
import interfaces.DAO;
import modelo.Jugador;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Jugador: unica clase que conoce el SQL de la tabla jugadores.
 * <p>
 * Ademas de las 5 operaciones del contrato DAO, ofrece las que dependen del
 * equipo: insertar un jugador directamente en un equipo, listar el plantel de
 * un equipo y cambiar un jugador de equipo. El jugador existe por si solo
 * (equipo_id admite NULL), por eso insertar(Jugador) lo guarda sin equipo.
 */
public class JugadorDAO implements DAO<Jugador> {

    private static final String COLUMNAS =
            "id, nombre, apellido, fecha_nacimiento, nacionalidad, dorsal, posicion, altura";

    private static final String SQL_INSERTAR =
            "INSERT INTO jugadores (id, equipo_id, nombre, apellido, fecha_nacimiento, nacionalidad, "
                    + "dorsal, posicion, altura) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_LISTAR =
            "SELECT " + COLUMNAS + " FROM jugadores ORDER BY id";
    private static final String SQL_LISTAR_POR_EQUIPO =
            "SELECT " + COLUMNAS + " FROM jugadores WHERE equipo_id = ? ORDER BY dorsal";
    private static final String SQL_BUSCAR =
            "SELECT " + COLUMNAS + " FROM jugadores WHERE id = ?";
    private static final String SQL_ACTUALIZAR =
            "UPDATE jugadores SET nombre = ?, apellido = ?, fecha_nacimiento = ?, nacionalidad = ?, "
                    + "dorsal = ?, posicion = ?, altura = ? WHERE id = ?";
    private static final String SQL_ELIMINAR =
            "DELETE FROM jugadores WHERE id = ?";
    private static final String SQL_ASIGNAR_EQUIPO =
            "UPDATE jugadores SET equipo_id = ? WHERE id = ?";

    /** Inserta el jugador sin equipo (equipo_id NULL). */
    @Override
    public void insertar(Jugador jugador) throws AccesoDatosException {
        guardar(jugador, null);
    }

    /**
     * Inserta el jugador ya asignado a un equipo.
     *
     * @param jugador  jugador a guardar
     * @param equipoId id del equipo (tiene que existir: es clave foranea)
     * @throws AccesoDatosException si falla la base (id repetido, equipo inexistente, dorsal repetido en el equipo)
     */
    public void insertarEnEquipo(Jugador jugador, int equipoId) throws AccesoDatosException {
        guardar(jugador, equipoId);
    }

    @Override
    public List<Jugador> listarTodos() throws AccesoDatosException {
        List<Jugador> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar los jugadores", e);
        }
        return lista;
    }

    /**
     * Devuelve el plantel de un equipo, ordenado por dorsal.
     *
     * @param equipoId id del equipo
     * @return lista de jugadores (vacia si el equipo no tiene ninguno)
     * @throws AccesoDatosException si falla la base
     */
    public List<Jugador> listarPorEquipo(int equipoId) throws AccesoDatosException {
        List<Jugador> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_POR_EQUIPO)) {
            ps.setInt(1, equipoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar el plantel del equipo " + equipoId, e);
        }
        return lista;
    }

    @Override
    public Jugador buscarPorId(int id) throws AccesoDatosException, EntidadNoEncontradaException {
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar el jugador " + id, e);
        }
        throw new EntidadNoEncontradaException("jugador", id);
    }

    @Override
    public void actualizar(Jugador jugador) throws AccesoDatosException, EntidadNoEncontradaException {
        int filas;
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {
            ps.setString(1, jugador.getNombre());
            ps.setString(2, jugador.getApellido());
            ps.setDate(3, Date.valueOf(jugador.getFechaNacimiento()));
            ps.setString(4, jugador.getNacionalidad());
            ps.setInt(5, jugador.getDorsal());
            ps.setString(6, jugador.getPosicion().name());
            ps.setDouble(7, jugador.getAltura());
            ps.setInt(8, jugador.getId());
            filas = ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar el jugador " + jugador.getId(), e);
        }
        if (filas == 0) {
            throw new EntidadNoEncontradaException("jugador", jugador.getId());
        }
    }

    @Override
    public void eliminar(int id) throws AccesoDatosException, EntidadNoEncontradaException {
        int filas;
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {
            ps.setInt(1, id);
            filas = ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al eliminar el jugador " + id, e);
        }
        if (filas == 0) {
            throw new EntidadNoEncontradaException("jugador", id);
        }
    }

    /**
     * Cambia al jugador de equipo.
     *
     * @param jugadorId id del jugador
     * @param equipoId  id del nuevo equipo
     * @throws AccesoDatosException         si falla la base (por ejemplo, el equipo no existe)
     * @throws EntidadNoEncontradaException si no existe el jugador
     */
    public void asignarEquipo(int jugadorId, int equipoId)
            throws AccesoDatosException, EntidadNoEncontradaException {
        int filas;
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ASIGNAR_EQUIPO)) {
            ps.setInt(1, equipoId);
            ps.setInt(2, jugadorId);
            filas = ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al asignar el jugador " + jugadorId
                    + " al equipo " + equipoId, e);
        }
        if (filas == 0) {
            throw new EntidadNoEncontradaException("jugador", jugadorId);
        }
    }

    /** Metodo comun de insertar e insertarEnEquipo: equipoId null guarda NULL en la base. */
    private void guardar(Jugador jugador, Integer equipoId) throws AccesoDatosException {
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {
            ps.setInt(1, jugador.getId());
            if (equipoId == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, equipoId);
            }
            ps.setString(3, jugador.getNombre());
            ps.setString(4, jugador.getApellido());
            ps.setDate(5, Date.valueOf(jugador.getFechaNacimiento()));   // LocalDate -> java.sql.Date
            ps.setString(6, jugador.getNacionalidad());
            ps.setInt(7, jugador.getDorsal());
            ps.setString(8, jugador.getPosicion().name());
            ps.setDouble(9, jugador.getAltura());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al insertar el jugador " + jugador.getNombreCompleto(), e);
        }
    }

    /** Convierte la fila actual del ResultSet en un objeto Jugador. */
    private Jugador mapear(ResultSet rs) throws SQLException {
        return new Jugador(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getDate("fecha_nacimiento").toLocalDate(),   // java.sql.Date -> LocalDate
                rs.getString("nacionalidad"),
                rs.getInt("dorsal"),
                Posicion.valueOf(rs.getString("posicion")),
                rs.getDouble("altura"));
    }
}
