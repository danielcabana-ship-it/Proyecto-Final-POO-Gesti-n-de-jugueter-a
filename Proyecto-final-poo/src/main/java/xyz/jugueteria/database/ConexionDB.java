package xyz.jugueteria.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton de conexión a MariaDB.
 * Carga las credenciales desde config.properties una sola vez al iniciar la JVM.
 */
public class ConexionDB {

    private static ConexionDB instancia;
    private Connection conexion;
    private static final Properties configuracion = new Properties();

    static {
        try (InputStream input = ConexionDB.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("[ConexionDB] No se encontró 'config.properties' en resources.");
            } else {
                configuracion.load(input);
            }
        } catch (IOException e) {
            System.err.println("[ConexionDB] Error al leer 'config.properties'.");
            e.printStackTrace();
        }
    }

    private ConexionDB() {
        try {
            String url = configuracion.getProperty("db.url");
            String user = configuracion.getProperty("db.user");
            String password = configuracion.getProperty("db.password");

            if (url == null || user == null || password == null) {
                System.err.println("[ConexionDB] Faltan credenciales en 'config.properties'.");
                return;
            }

            Class.forName("org.mariadb.jdbc.Driver");
            conexion = DriverManager.getConnection(url, user, password);
            System.out.println("[ConexionDB] Conexión establecida exitosamente.");
        } catch (ClassNotFoundException e) {
            System.err.println("[ConexionDB] Driver MariaDB no encontrado. Verifica pom.xml.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[ConexionDB] Error SQL al conectar.");
            e.printStackTrace();
        }
    }

    public static synchronized ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("[ConexionDB] Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("[ConexionDB] Error al cerrar conexión.");
            e.printStackTrace();
        } finally {
            instancia = null;
        }
    }
}