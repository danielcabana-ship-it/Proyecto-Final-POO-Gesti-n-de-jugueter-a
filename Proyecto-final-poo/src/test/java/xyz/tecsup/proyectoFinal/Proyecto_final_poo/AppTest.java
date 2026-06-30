package xyz.tecsup.proyectoFinal.Proyecto_final_poo; // Define el paquete específico donde se organiza este test unitario dentro del proyecto

import static org.junit.jupiter.api.Assertions.assertNotNull; // Importa de forma estática el método assertNotNull de JUnit 5 para validar que un objeto no sea nulo
import org.junit.jupiter.api.Test; // Importa la anotación Test de JUnit 5 para indicarle al entorno de desarrollo que este método es una prueba ejecutable
import xyz.jugueteria.database.ConexionDB; // Importa la clase encargada de gestionar los parámetros y el ciclo de vida de la conexión a la base de datos

import java.sql.Connection; // Importa la interfaz nativa de Java para representar y manipular la sesión activa con el motor SQL
import java.sql.SQLException; // Importa la clase de excepción que captura cualquier error técnico de base de datos (credenciales mal puestas, servidor apagado, etc.)

/**
 * Test unitario para validar que la conexión a la BD funciona.
 */
public class AppTest { // Declara la clase pública AppTest que contendrá las pruebas automáticas del sistema

    @Test // Anotación clave de JUnit: transforma el siguiente método en una prueba que el sistema puede correr con un solo clic
    public void testConexionFunciona() { // Declara el método público de prueba para verificar el enlace con el servidor de datos
        // Verifica que se puede abrir una conexión real con la BD
        // El bloque try-with-resources abre la conexión y asegura que se cierre SOLA al terminar el bloque, evitando fugas de memoria
        try (Connection con = ConexionDB.getConexion()) { // Intenta invocar el método de conexión y lo guarda temporalmente en la variable 'con'
            assertNotNull(con, "La conexión a la base de datos no debe ser nula."); // Evalúa que 'con' contenga un objeto válido; si es null, detiene el test e informa del fallo
        } catch (SQLException e) { // Captura la excepción en caso el driver JDBC o la base de datos lancen un error durante el intento
            // Si la BD no está corriendo, el test falla con mensaje claro
            // Lanza un error de aserción personalizado arrastrando la causa original para que el programador sepa exactamente qué pasó
            throw new AssertionError("No se pudo conectar a la BD: " + e.getMessage(), e); // Detiene la prueba de inmediato pintándola en rojo con el reporte del fallo
        } // Cierre del bloque de captura catch
    } // Cierre del método de prueba testConexionFunciona
} // Cierre definitivo de la clase AppTest