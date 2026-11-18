package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase auxiliar que entrega conexiones a la base de datos MySQL.
 * <p>
 * Cada llamada a {@link #getConexion()} abre una conexion NUEVA. Quien la pide
 * es responsable de cerrarla, y lo hace con try-with-resources
 * (Connection implementa AutoCloseable). Abrir, usar y cerrar es el esquema
 * recomendado de JDBC: una conexion abierta ocupa recursos del servidor.
 */
public class ConexionDB {

    private static final String URL =
            "jdbc:mysql://localhost:3306/tp_nba?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "tpnba";
    private static final String CLAVE = "tpnba2026";

    // Clase de utilidad: no tiene sentido crear objetos ConexionDB
    private ConexionDB() {
    }

    /**
     * Abre una conexion nueva con la base tp_nba.
     *
     * @return la conexion abierta (hay que cerrarla despues de usarla)
     * @throws SQLException si el servidor esta caido, las credenciales son
     *                      incorrectas o la base no existe; la maneja el DAO
     */
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
