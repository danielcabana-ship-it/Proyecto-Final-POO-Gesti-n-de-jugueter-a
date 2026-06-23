package xyz.jugueteria.dao;

// 1. Traemos nuestra propia clase de conexión a MariaDB
import xyz.jugueteria.database.ConexionDB;

// 2. Traemos las herramientas nativas de Java para hablar con bases de datos
import java.sql.Connection;        // Para abrir el "puente" de comunicación
import java.sql.PreparedStatement; // Para enviar consultas SQL de forma segura
import java.sql.ResultSet;         // Para leer los resultados que nos devuelve la base de datos
import java.sql.SQLException;      // Para atrapar cualquier error (ej. si la BD está apagada)

public class AdminDAO {
    // El cascarón está listo.
}