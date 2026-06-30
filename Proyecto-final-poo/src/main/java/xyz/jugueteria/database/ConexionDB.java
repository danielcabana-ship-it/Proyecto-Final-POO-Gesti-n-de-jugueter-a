package xyz.jugueteria.database;

// Importamos las herramientas para leer archivos de texto externos (.properties)
import java.io.IOException;
import java.io.InputStream;
// Importamos los componentes de Java necesarios para fabricar y gestionar la conexión física a la base de datos
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

    // Creamos una "cajita" estática para guardar los datos de configuración (URL, usuario, contraseña)
    private static final Properties configuracion = new Properties();

    // Bloque estático: Se ejecuta automáticamente UNA SOLA VEZ cuando el programa arranca y usa esta clase
    static {
        // Intentamos buscar y abrir el archivo 'config.properties' que está guardado en la carpeta de recursos (resources)
        try (InputStream input = ConexionDB.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            // Si el archivo no existe o no se encuentra en su lugar, avisamos del error
            if (input == null) {
                System.err.println("[ConexionDB] ERROR: No se encontró 'config.properties' en resources.");
            } else {
                // Si todo está bien, cargamos los datos del archivo dentro de nuestra variable 'configuracion'
                configuracion.load(input);
                // Confirmamos en consola que ya leímos las credenciales con éxito
                System.out.println("[ConexionDB] Configuración cargada correctamente.");
            }
        } catch (IOException e) {
            // Si el archivo está corrupto o no se puede leer, capturamos el error de lectura
            System.err.println("[ConexionDB] ERROR: Falló la lectura de 'config.properties'.");
            e.printStackTrace(); // Muestra el reporte de fallas detallado
        }
    }

    // Al hacer el constructor privado, evitamos que alguien intente hacer un "new ConexionDB()" desde fuera.
    // Esta clase solo sirve para llamarla directamente como: ConexionDB.getConexion()
    private ConexionDB() {}

    /**
     * Abre y devuelve una conexión nueva a la base de datos.
     * El llamador es responsable de cerrarla (idealmente con try-with-resources).
     *
     * @return una Connection fresca lista para usar.
     * @throws SQLException si ocurre un error al conectar o no se encuentran las credenciales.
     */
    public static Connection getConexion() throws SQLException {
        // Extraemos los datos que guardamos del archivo .properties usando sus nombres clave
        String url      = configuracion.getProperty("db.url");      // La dirección de la base de datos
        String user     = configuracion.getProperty("db.user");     // El usuario de acceso
        String password = configuracion.getProperty("db.password"); // La contraseña de acceso

        // Si falta alguno de los tres datos esenciales, cancelamos la operación por seguridad
        if (url == null || user == null || password == null) {
            throw new SQLException("Faltan credenciales en 'config.properties'.");
        }

        try {
            // Le avisamos a Java qué "traductor" (Driver) debe usar para comunicarse con MariaDB
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("[ConexionDB] ERROR: Driver MariaDB no encontrado. Revisa pom.xml.");
            throw new SQLException("Driver MariaDB no encontrado", e);
        }

        // Le pedimos al administrador de drivers que abra un puente físico (conexión) usando los datos extraídos
        Connection con = DriverManager.getConnection(url, user, password);

        // Ponemos un mensaje alegre en consola avisando que la conexión se abrió sin problemas
        System.out.println("[ConexionDB] Conexión abierta OK.");

        // Devolvemos el puente de conexión listo para que cualquier DAO haga sus consultas SQL
        return con;
    }
}