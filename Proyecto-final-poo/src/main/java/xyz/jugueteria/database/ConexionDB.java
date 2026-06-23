package xyz.jugueteria.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * ¡Ojo aquí! Este es el Singleton que nos conecta a MariaDB.
 * Solo lo instanciamos una vez al arrancar la app para no reventar la base de datos
 * abriendo y cerrando conexiones a cada rato.
 */
public class ConexionDB {

    private static ConexionDB instancia;
    private Connection conexion;
    private static final Properties configuracion = new Properties();

    static {
        // Bloque estático que carga el config.properties apenas arranca esto.
        // Si no encuentra el archivo, avisamos por consola porque seguro alguien borró el archivo sin querer.
        try (InputStream input = ConexionDB.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.err.println("[ConexionDB] ¡Problema serio! No se encontró 'config.properties' en resources.");
            } else {
                configuracion.load(input);
            }
        } catch (IOException e) {
            System.err.println("[ConexionDB] Falló la lectura de 'config.properties'. Revisa que no esté corrupto.");
            e.printStackTrace();
        }
    }

    private ConexionDB() {
        try {
            // Traemos las credenciales. Si alguna viene nula, abortamos misión porque no podemos adivinar la clave.
            String url = configuracion.getProperty("db.url");
            String user = configuracion.getProperty("db.user");
            String password = configuracion.getProperty("db.password");

            if (url == null || user == null || password == null) {
                System.err.println("[ConexionDB] Te faltan credenciales en 'config.properties', llena los datos porfa.");
                return;
            }

            // Levantamos el driver y establecemos la conexión real.
            Class.forName("org.mariadb.jdbc.Driver");
            conexion = DriverManager.getConnection(url, user, password);
            System.out.println("[ConexionDB] ¡Listo! Nos conectamos a la BD sin problemas.");
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