package xyz.jugueteria.dao;

// 1. Traemos nuestra propia clase de conexión a MariaDB
import xyz.jugueteria.database.ConexionDB;

// 2. Traemos las herramientas nativas de Java para hablar con bases de datos
import java.sql.Connection;        // Para abrir el "puente" de comunicación
import java.sql.PreparedStatement; // Para enviar consultas SQL de forma segura
import java.sql.ResultSet;         // Para leer los r   esultados que nos devuelve la base de datos
import java.sql.SQLException;      // Para atrapar cualquier error (ej. si la BD está apagada)

public class AdminDAO {
    // El cascarón está listo.
    // Este método responderá un simple 'true' (si ingresa) o 'false' (si se equivoca)
    public boolean login(String usuario, String contrasenia) {

        // Armamos la consulta.
        // ¡OJO! Usamos "?" en lugar de concatenar el texto directo.
        // Esto es súper importante para la eficiencia y para evitar que nos hackeen (Inyección SQL).
        String sql = "SELECT * FROM administradores WHERE usuario = ? AND contrasenia = ?";

        // ... (Aquí irá la conexión)
    }
}