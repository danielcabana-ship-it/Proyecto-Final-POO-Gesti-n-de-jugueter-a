package xyz.tecsup.proyectoFinal.Proyecto_final_poo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import xyz.jugueteria.database.ConexionDB;

/**
 * Test unitario para validar el Singleton de la conexión.
 */
public class AppTest {

    @Test
    public void testConexionSingletonInstancia() {
        // Verifica que el Singleton de conexión no sea nulo al inicializarse
        assertNotNull(ConexionDB.getInstancia());
    }
}
