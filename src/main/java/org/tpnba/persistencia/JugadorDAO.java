package org.tpnba.persistencia;

import org.tpnba.enums.Posicion;
import org.tpnba.excepciones.EntidadNoEncontradaException;
import org.tpnba.modelo.Jugador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JugadorDAO implements DAO <Jugador>{

    private static final String COLUMNAS =
            "id, nombre, apellido, fecha_nacimiento, nacionalidad, dorsal, posicion, altura";

    private static final String SQL_INSERTAR =
            "INSERT INTO jugadores (id, nombre, apellido, fecha_nacimiento, nacionalidad, dorsal, posicion, altura) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_ACTUALIZAR =
            "UPDATE jugadores SET nombre = ?, apellido = ?, fecha_nacimiento = ?, nacionalidad = ?, "
                    + "dorsal = ?, posicion = ?, altura = ? WHERE id = ?";
    private static final String SQL_ELIMINAR =
            "DELETE FROM jugadores WHERE id = ?";
    private static final String SQL_BUSCAR =
            "SELECT " + COLUMNAS + " FROM jugadores WHERE id = ?";
    private static final String SQL_LISTAR =
            "SELECT " + COLUMNAS + " FROM jugadores ORDER BY id";
    private static final String SQL_LISTAR_POR_EQUIPO =
            "SELECT " + COLUMNAS + " FROM jugadores WHERE equipo_id = ? ORDER BY dorsal";
    private static final String SQL_ASIGNAR_EQUIPO =
            "UPDATE jugadores SET equipo_id = ? WHERE id = ?";


    @Override
    public void insertar(Jugador jugador) throws SQLException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {
            ps.setInt(1, jugador.getId());
            ps.setString(2, jugador.getNombre());
            ps.setString(3, jugador.getApellido());
            ps.setDate(4, Date.valueOf(jugador.getFechaNacimiento()));
            ps.setString(5, jugador.getNacionalidad());
            ps.setInt(6, jugador.getDorsal());
            ps.setString(7, jugador.getPosicion().name());
            ps.setDouble(8, jugador.getAltura());
            ps.executeUpdate();
        }
    }

    @Override
    public void actualizar(Jugador jugador) throws SQLException, EntidadNoEncontradaException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {
            ps.setString(1, jugador.getNombre());
            ps.setString(2, jugador.getApellido());
            ps.setDate(3, Date.valueOf(jugador.getFechaNacimiento()));
            ps.setString(4, jugador.getNacionalidad());
            ps.setInt(5, jugador.getDorsal());
            ps.setString(6, jugador.getPosicion().name());
            ps.setDouble(7, jugador.getAltura());
            ps.setInt(8, jugador.getId());
            if (ps.executeUpdate() == 0) {
                throw new EntidadNoEncontradaException("jugador", jugador.getId());
            }
        }
    }

    @Override
    public void eliminar(int id) throws SQLException, EntidadNoEncontradaException {
            try (Connection con = ConexionBD.getConexion();
                 PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {
                ps.setInt(1, id);
                if (ps.executeUpdate() == 0) {
                    throw new EntidadNoEncontradaException("jugador", id);
                }
            }
    }

    @Override
    public Jugador buscaPorId(int id) throws SQLException, EntidadNoEncontradaException {
            try (Connection con = ConexionBD.getConexion();
                 PreparedStatement ps = con.prepareStatement(SQL_BUSCAR)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapear(rs);
                    }
                    throw new EntidadNoEncontradaException("jugador", id);
                }
            }
    }

    @Override
    public List<Jugador> listarTodos() throws SQLException {
            List<Jugador> resultado = new ArrayList<>();
            try (Connection con = ConexionBD.getConexion();
                 PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapear(rs));
                }
            }
            return resultado;
    }


    //devuelve los jugadores de un equipo ordenados por nro de dorsal
    public List<Jugador> listarPorEquipo(int equipoId) throws SQLException {
        List<Jugador> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_POR_EQUIPO)) {
            ps.setInt(1, equipoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapear(rs));
                }
            }
        }
        return resultado; // vuelve vacio si el equipo esta vacio
    }


    //asigna un jugador a un equipo ambos por id, salta exception si el equipo no existe o si otro jugador de ese equipo ya tiene esa dorsal

    public void asignarEquipo(int jugadorId, int equipoId) throws SQLException, EntidadNoEncontradaException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ASIGNAR_EQUIPO)) {
            ps.setInt(1, equipoId);
            ps.setInt(2, jugadorId);
            if (ps.executeUpdate() == 0) {
                throw new EntidadNoEncontradaException("jugador", jugadorId);
            }
        }
    }


    //convierte la fila actual del ResultSet en un objeto jugador
    private Jugador mapear(ResultSet rs) throws SQLException {
        return new Jugador(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getDate("fecha_nacimiento").toLocalDate(),
                rs.getString("nacionalidad"),
                rs.getInt("dorsal"),
                Posicion.valueOf(rs.getString("posicion")),
                rs.getDouble("altura"));
    }

}
