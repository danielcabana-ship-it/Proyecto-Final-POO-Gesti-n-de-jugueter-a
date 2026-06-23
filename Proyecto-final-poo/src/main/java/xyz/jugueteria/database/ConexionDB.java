package xyz.jugueteria.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Proveedor de conexiones a MariaDB.
 *
 * CORRECCIÓN APLICADA:
 * El error anterior era que se guardaba UNA SOLA instancia de Connection.
 * Los DAOs usan try-with-resources que CIERRA automáticamente esa conexión
 * al salir del bloque, dejando a todos los demás métodos con una conexión
 * muerta y lanzando "Connection is closed".
 *
 * La solución: getConexion() ahora abre SIEMPRE una conexión nueva y fresca.
 * Para una app de escritorio con un solo usuario, esto es limpio y correcto.
 * Cada DAO abre la suya, la usa y la cierra sola via try-with-resources.
 */
public class ConexionDB {

    private static final Properties configuracion = new Properties();

    static {
        // Cargamos el config.properties una sola vez al arrancar la clase.
        try (InputStream input = ConexionDB.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("[ConexionDB] ERROR: No se encontró 'config.properties' en resources.");
            } else {
                configuracion.load(input);
                System.out.println("[ConexionDB] Configuración cargada correctamente.");
            }
        } catch (IOException e) {
            System.err.println("[ConexionDB] ERROR: Falló la lectura de 'config.properties'.");
            e.printStackTrace();
        }
    }

    // Constructor privado: esta clase solo se usa como fábrica estática de conexiones.
    private ConexionDB() {}

    /**
     * Abre y devuelve una conexión nueva a la base de datos.
     * El llamador es responsable de cerrarla (idealmente con try-with-resources).
     *
     * @return una Connection fresca lista para usar, o null si falla.
     */
    public static Connection getConexion() {
        try {
            String url      = configuracion.getProperty("db.url");
            String user     = configuracion.getProperty("db.user");
            String password = configuracion.getProperty("db.password");

            if (url == null || user == null || password == null) {
                System.err.println("[ConexionDB] ERROR: Faltan credenciales en 'config.properties'.");
                return null;
            }

            Class.forName("org.mariadb.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("[ConexionDB] Conexión abierta OK.");
            return con;

        } catch (ClassNotFoundException e) {
            System.err.println("[ConexionDB] ERROR: Driver MariaDB no encontrado. Revisa pom.xml.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[ConexionDB] ERROR SQL al conectar: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}