package dao;

import config.ConexionDB;
import enums.Conferencia;
import excepciones.AccesoDatosException;
import excepciones.EntidadNoEncontradaException;
import interfaces.DAO;
import modelo.Equipo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de Equipo: unica clase que conoce el SQL de la tabla equipos.
 * <p>
 * Solo persiste los datos propios del equipo (id, nombre, ciudad, conferencia).
 * El plantel se guarda y se lee con JugadorDAO. Todas las consultas usan
 * PreparedStatement (los datos viajan como parametros, nunca concatenados),
 * cierran sus recursos con try-with-resources y, ante una SQLException,
 * lanzan AccesoDatosException conservando la causa.
 */
public class EquipoDAO implements DAO<Equipo> {

    private static final String SQL_INSERTAR =
            "INSERT INTO equipos (id, nombre, ciudad, conferencia) VALUES (?, ?, ?, ?)";
    private static final String SQL_LISTAR =
            "SELECT id, nombre, ciudad, conferencia FROM equipos ORDER BY id";
    private static final String SQL_BUSCAR =
            "SELECT id, nombre, ciudad, conferencia FROM equipos WHERE id = ?";
    private static final String SQL_ACTUALIZAR =
            "UPDATE equipos SET nombre = ?, ciudad = ?, conferencia = ? WHERE id = ?";
    private static final String SQL_ELIMINAR =
            "DELETE FROM equipos WHERE id = ?";

    @Override
    public void insertar(Equipo equipo) throws AccesoDatosException {
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {
            ps.setInt(1, equipo.getId());
            ps.setString(2, equipo.getNombre());
            ps.setString(3, equipo.getCiudad());
            ps.setString(4, equipo.getConferencia().name());   // "ESTE" u "OESTE"
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al insertar el equipo " + equipo.getNombre(), e);
        }
    }

    @Override
    public List<Equipo> listarTodos() throws AccesoDatosException {
        List<Equipo> lista = new ArrayList<>();
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar los equipos", e);
        }
        return lista;   // una List independiente de la conexion, no el ResultSet
    }

    @Override
    public Equipo buscarPorId(int id) throws AccesoDatosException, EntidadNoEncontradaException {
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR)) {
            ps.setInt(1, id);
            // El ResultSet va en su propio try: recien existe despues de setear el parametro
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar el equipo " + id, e);
        }
        throw new EntidadNoEncontradaException("equipo", id);   // error de negocio, fuera del try de SQL
    }

    @Override
    public void actualizar(Equipo equipo) throws AccesoDatosException, EntidadNoEncontradaException {
        int filas;
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {
            ps.setString(1, equipo.getNombre());
            ps.setString(2, equipo.getCiudad());
            ps.setString(3, equipo.getConferencia().name());
            ps.setInt(4, equipo.getId());
            filas = ps.executeUpdate();   // filas afectadas
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar el equipo " + equipo.getId(), e);
        }
        if (filas == 0) {                 // 0 filas: ese id no existia
            throw new EntidadNoEncontradaException("equipo", equipo.getId());
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
            throw new AccesoDatosException("Error al eliminar el equipo " + id, e);
        }
        if (filas == 0) {
            throw new EntidadNoEncontradaException("equipo", id);
        }
    }

    /** Convierte la fila actual del ResultSet en un objeto Equipo. */
    private Equipo mapear(ResultSet rs) throws SQLException {
        return new Equipo(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("ciudad"),
                Conferencia.valueOf(rs.getString("conferencia")));
    }
}
