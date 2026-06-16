package xyz.tecsup.proyectoFinal.Proyecto_final_poo;

import xyz.jugueteria.database.ConexionDB;
import java.sql.Connection;

/**
 * Clase de prueba rápida para verificar que la conexión a MariaDB funciona.
 *
 * Ejecutar este main antes de implementar los DAOs nos ahorra dolores de cabeza:
 * si la conexión falla aquí, sabemos que el problema es de infraestructura
 * (driver, credenciales, BD apagada) y no de nuestra lógica de negocio.
 */
public class TestConexion {

    public static void main(String[] args) {
        System.out.println("=== Test de Conexión a MariaDB ===\n");

        Connection cn = null;
        try {
            cn = ConexionDB.getInstancia().getConexion();

            if (cn != null && !cn.isClosed()) {
                System.out.println("✓ ¡Conexión establecida con éxito!");
                System.out.println("  Base de datos: " + cn.getCatalog());
                System.out.println("  Esquema:       " + cn.getSchema());
            } else {
                System.out.println("✗ La conexión se obtuvo pero está cerrada o es null.");
            }

        } catch (Exception e) {
            // Capturamos Exception (no solo SQLException) por si el driver no está
            // en el classpath y lanza ClassNotFoundException internamente
            System.out.println("✗ Error al intentar conectar:");
            System.out.println("  Mensaje: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Siempre cerramos la conexión al terminar la prueba para no dejar
            // recursos abiertos en el servidor de base de datos
            ConexionDB.getInstancia().cerrarConexion();
            System.out.println("\n=== Fin del test ===");
        }
    }
}