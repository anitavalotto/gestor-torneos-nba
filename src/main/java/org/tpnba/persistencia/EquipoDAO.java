package org.tpnba.persistencia;

import org.tpnba.enums.Conferencia;
import org.tpnba.excepciones.EntidadNoEncontradaException;
import org.tpnba.modelo.Equipo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipoDAO implements DAO <Equipo>{

    private static final String SQL_INSERTAR = "INSERT INTO equipos (id, nombre, ciudad, conferencia) VALUES (?,?,?,?)";
    private static final String SQL_ACTUALIZAR = "UPDATE equipos SET nombre = ?, ciudad = ?, conferencia = ? WHERE id = ?";
    private static final String SQL_ELIMINAR = "DELETE FROM equipos WHERE id = ?";
    private static final String SQL_BUSCAR = "SELECT id, nombre, ciudad, conferencia FROM equipos WHERE id = ?";
    private static final String SQL_LISTAR = "SELECT id, nombre, ciudad, conferencia FROM equipos ORDER BY id";


    @Override
    public void insertar(Equipo equipo) throws SQLException {
        try (Connection con = ConexionBD.getConexion();
        PreparedStatement ps = con.prepareStatement (SQL_INSERTAR) ){
            ps.setInt(1, equipo.getId());
            ps.setString(2, equipo.getNombre());
            ps.setString(3, equipo.getCiudad());
            ps.setString(4, equipo.getConferencia().name()); // ESTE u OESTE
            ps.executeUpdate();
        }

    }

    @Override
    public void actualizar(Equipo equipo) throws SQLException, EntidadNoEncontradaException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement (SQL_ACTUALIZAR) ){
            ps.setString(1, equipo.getNombre());
            ps.setString(2, equipo.getCiudad());
            ps.setString(3, equipo.getConferencia().name());  // ESTE u OESTE
            ps.setInt(4, equipo.getId());
            int filas = ps.executeUpdate(); // filas afectadas
            if (filas == 0){ // filas == 0 --> ese id no existia
                throw new EntidadNoEncontradaException("equipo" , equipo.getId());
            }
        }
    }

    @Override
    public void eliminar(int id) throws SQLException, EntidadNoEncontradaException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new EntidadNoEncontradaException("equipo", id);
            }
        }
    }

    @Override
    public Equipo buscaPorId(int id) throws SQLException, EntidadNoEncontradaException {
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR)) {
            ps.setInt(1, id);
            // El ResultSet va en su propio try: recien existe despues de ejecutar la consulta
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
                throw new EntidadNoEncontradaException("equipo", id);
            }
        }
    }

    @Override
    public List<Equipo> listarTodos() throws SQLException {
        List<Equipo> resultado = new ArrayList<>();
        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
        }
        return resultado;   // devuelve una List independiente de la conexion, no el ResultSet
    }


    //convierte la fila actual en un objeto de tipo Equipo
    private Equipo mapear(ResultSet rs) throws SQLException {
        return new Equipo(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("ciudad"),
                Conferencia.valueOf(rs.getString("conferencia")));
    }

}
