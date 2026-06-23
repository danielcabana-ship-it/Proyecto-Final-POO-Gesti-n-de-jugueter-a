package xyz.tecsup.proyectoFinal.Proyecto_final_poo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import xyz.jugueteria.database.ConexionDB;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Test unitario para validar que la conexión a la BD funciona.
 */
public class AppTest {

    @Test
    public void testConexionFunciona() {
        // Verifica que se puede abrir una conexión real con la BD
        try (Connection con = ConexionDB.getConexion()) {
            assertNotNull(con, "La conexión a la base de datos no debe ser nula.");
        } catch (SQLException e) {
            // Si la BD no está corriendo, el test falla con mensaje claro
            throw new AssertionError("No se pudo conectar a la BD: " + e.getMessage(), e);
        }
    }
}
