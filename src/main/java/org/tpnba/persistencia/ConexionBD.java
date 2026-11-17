package org.tpnba.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.mysql.cj.conf.PropertyKey.PASSWORD;


    // clase aux que conecta el programa con la base de datos

    public class ConexionBD {
        private static final String URL = "jdbc:mysql://localhost:3306/tp_nba?useSSL=false&allowPublicKeyRetrieval=true";
        private static final String USUARIO = "tpnba";
        private static final String PASSWORD = "tpnba2026";


    private ConexionBD(){

    }

    //abre una conexion nueva con la base tpnba, lanza sqlException si el servidor esta caido, las credenciales son incorrectas o la base no existe

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

}
