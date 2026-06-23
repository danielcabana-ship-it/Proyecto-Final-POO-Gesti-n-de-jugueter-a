package xyz.jugueteria.dao;

import xyz.jugueteria.database.ConexionDB;
import xyz.jugueteria.models.Usuario;
import xyz.jugueteria.util.SecurityUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object para la tabla 'usuarios'.
 * Encargado de las operaciones de Login, Registro y Validaciones de existencia.
 */
public class UsuarioDAO {

    /**
     * Valida las credenciales del usuario (por username o por email).
     *
     * @param usernameOrEmail el nombre de usuario o dirección de correo ingresados.
     * @param password la contraseña en texto plano ingresada.
     * @return el objeto Usuario si es válido, null en caso contrario.
     */
    public Usuario login(String usernameOrEmail, String password) {
        String sql = "SELECT * FROM usuarios WHERE (username = ? OR email = ?) AND password = ?";
        String hashedPassword = SecurityUtils.hashPassword(password);

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usernameOrEmail);
            ps.setString(2, usernameOrEmail);
            ps.setString(3, hashedPassword);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setUsername(rs.getString("username"));
                    u.setEmail(rs.getString("email"));
                    u.setPassword(rs.getString("password"));
                    u.setRol(rs.getString("rol"));
                    System.out.println("[UsuarioDAO] Login exitoso para el usuario: " + u.getUsername());
                    return u;
                }
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error en el proceso de login: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Registra un nuevo usuario en la base de datos aplicando encriptación a la contraseña.
     *
     * @param usuario el objeto Usuario a registrar (con contraseña en texto plano).
     * @return true si se registró con éxito, false en caso contrario.
     */
    public boolean registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, username, email, password, rol) VALUES (?, ?, ?, ?, ?)";
        String hashedPassword = SecurityUtils.hashPassword(usuario.getPassword());

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsername());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, hashedPassword);
            ps.setString(5, usuario.getRol());

            int filas = ps.executeUpdate();
            System.out.println("[UsuarioDAO] Usuario registrado con éxito. Filas afectadas: " + filas);
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si un nombre de usuario ya está registrado en el sistema.
     */
    public boolean usernameExiste(String username) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al verificar existencia del username: " + e.getMessage());
        }
        return false;
    }

    /**
     * Verifica si una dirección de correo ya está registrada en el sistema.
     */
    public boolean emailExiste(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al verificar existencia del correo: " + e.getMessage());
        }
        return false;
    }
}
