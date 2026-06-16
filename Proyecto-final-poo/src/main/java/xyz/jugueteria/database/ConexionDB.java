package xyz.jugueteria.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton de conexión segura a la base de datos MariaDB.
 *
 * Se garantiza una única instancia en toda la aplicación para optimizar el pool
 * de conexiones y evitar la saturación de conexiones abiertas en el servidor de BD.
 */
public class ConexionDB {

    private static ConexionDB instancia;
    private Connection conexion;

    // Objeto contenedor estático para mantener las propiedades cargadas
    private static final Properties configuracion = new Properties();

    /**
     * Bloque estático de inicialización: carga el archivo de configuración config.properties
     * de forma segura e inmediata cuando la clase se carga en memoria, antes de crear
     * cualquier instancia. Esto previene relecturas redundantes del disco.
     */
    static {
        try (InputStream input = ConexionDB.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("[ConexionDB] Error: No se encontró el archivo 'config.properties' en resources.");
            } else {
                configuracion.load(input);
            }
        } catch (IOException e) {
            System.err.println("[ConexionDB] Error al intentar leer el archivo 'config.properties'.");
            e.printStackTrace();
        }
    }

    /**
     * Constructor privado para evitar instanciaciones manuales externas.
     * Lee las propiedades precargadas del bloque estático y establece la conexión física.
     */
    private ConexionDB() {
        try {
            String url = configuracion.getProperty("db.url");
            String user = configuracion.getProperty("db.user");
            String password = configuracion.getProperty("db.password");

            if (url == null || user == null || password == null) {
                System.err.println("[ConexionDB] Error: Faltan credenciales clave en 'config.properties'.");
                return;
            }

            // Cargamos explícitamente el driver nativo de MariaDB
            Class.forName("org.mariadb.jdbc.Driver");

            // Abrimos el canal de comunicación con la base de datos
            conexion = DriverManager.getConnection(url, user, password);
            System.out.println("[ConexionDB] Conexión establecida exitosamente.");

        } catch (ClassNotFoundException e) {
            System.err.println("[ConexionDB] No se localizó el driver de MariaDB. Verifica tus dependencias en pom.xml.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("[ConexionDB] Error de SQL al intentar conectarse al servidor de BD.");
            e.printStackTrace();
        }
    }

    /**
     * Punto de acceso global y sincronizado para obtener la instancia única.
     * El modificador 'synchronized' asegura la integridad del Singleton en hilos concurrentes.
     */
    public static synchronized ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }

    /**
     * Retorna el objeto Connection para ejecutar sentencias JDBC.
     */
    public Connection getConexion() {
        return conexion;
    }

    /**
     * Cierra de manera ordenada la sesión actual de la base de datos.
     */
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("[ConexionDB] Conexión cerrada ordenadamente.");
            }
        } catch (SQLException e) {
            System.err.println("[ConexionDB] Error de SQL al cerrar la conexión.");
            e.printStackTrace();
        } finally {
            instancia = null;
        }
    }
}