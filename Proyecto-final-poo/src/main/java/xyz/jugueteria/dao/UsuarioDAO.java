package xyz.jugueteria.dao;

// Importamos la herramienta para conectarnos a la base de datos de la juguetería
import xyz.jugueteria.database.ConexionDB;
// Importamos el modelo Usuario para poder manejar sus datos como un objeto en Java
import xyz.jugueteria.models.Usuario;
// Importamos la herramienta de seguridad que encripta/hashea las contraseñas
import xyz.jugueteria.util.SecurityUtils;

// Importamos los componentes de Java SQL necesarios para interactuar con la base de datos
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
        // La consulta busca un usuario que coincida con el alias O con el correo, Y que además tenga la contraseña correcta
        String sql = "SELECT * FROM usuarios WHERE (username = ? OR email = ?) AND password = ?";
        // Convertimos la contraseña que escribió el usuario (texto plano) a su versión encriptada para poder compararla
        String hashedPassword = SecurityUtils.hashPassword(password);

        // Abrimos la conexión a la base de datos y preparamos la consulta SQL de forma segura
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Asignamos los datos reales a los '?' de la consulta en orden
            ps.setString(1, usernameOrEmail); // Primer '?': sirve para revisar si coincide con el username
            ps.setString(2, usernameOrEmail); // Segundo '?': sirve para revisar si coincide con el email
            ps.setString(3, hashedPassword);   // Tercer '?': la contraseña ya encriptada

            // Ejecutamos la consulta y abrimos el contenedor de resultados (ResultSet)
            try (ResultSet rs = ps.executeQuery()) {
                // Si la base de datos encontró un usuario con esos datos...
                if (rs.next()) {
                    // Creamos un objeto de tipo Usuario vacío para llenarlo con los datos reales
                    Usuario u = new Usuario();
                    // Sacamos la información de las columnas de la tabla y se la asignamos al objeto Java
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombre(rs.getString("nombre"));
                    u.setUsername(rs.getString("username"));
                    u.setEmail(rs.getString("email"));
                    u.setPassword(rs.getString("password")); // Aquí se guarda el hash de la contraseña
                    u.setRol(rs.getString("rol"));           // Por ejemplo: 'ADMIN' o 'CLIENTE'

                    // Dejamos un rastro en la consola del servidor avisando que el login funcionó
                    System.out.println("[UsuarioDAO] Login exitoso para el usuario: " + u.getUsername());
                    // Devolvemos el usuario completamente armado para saber quién inició sesión
                    return u;
                }
            }
        } catch (SQLException e) {
            // Si la base de datos se cae o la consulta falla, capturamos el error y avisamos
            System.err.println("[UsuarioDAO] Error en el proceso de login: " + e.getMessage());
            e.printStackTrace(); // Imprime la lista de fallas técnica detallada
        }
        // Si no se encontró el usuario o la contraseña no coincidía, devolvemos null (acceso denegado)
        return null;
    }

    /**
     * Registra un nuevo usuario en la base de datos aplicando encriptación a la contraseña.
     *
     * @param usuario el objeto Usuario a registrar (con contraseña en texto plano).
     * @return true si se registró con éxito, false en caso contrario.
     */
    public boolean registrarUsuario(Usuario usuario) {
        // Sentencia SQL para meter un nuevo registro a la tabla de usuarios
        String sql = "INSERT INTO usuarios (nombre, username, email, password, rol) VALUES (?, ?, ?, ?, ?)";
        // Antes de guardar, encriptamos la contraseña del usuario por seguridad (nunca hay que guardar texto plano)
        String hashedPassword = SecurityUtils.hashPassword(usuario.getPassword());

        // Abrimos la conexión y preparamos la orden de inserción
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Rellenamos los '?' con los datos del nuevo usuario que queremos crear
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getUsername());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, hashedPassword); // Aquí guardamos el password ya transformado en un texto seguro
            ps.setString(5, usuario.getRol());

            // Ejecutamos la inserción en la base de datos y contamos cuántas filas se crearon
            int filas = ps.executeUpdate();
            // Imprimimos en consola que el usuario se guardó bien
            System.out.println("[UsuarioDAO] Usuario registrado con éxito. Filas afectadas: " + filas);
            // Si las filas afectadas son mayores a 0, todo fue un éxito y devuelve true
            return filas > 0;

        } catch (SQLException e) {
            // Si el nombre de usuario o correo ya existen (y son campos únicos), saltará este error
            System.err.println("[UsuarioDAO] Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
            return false; // Retornamos false porque no se pudo crear el usuario
        }
    }

    /**
     * Verifica si un nombre de usuario ya está registrado en el sistema.
     */
    public boolean usernameExiste(String username) {
        // Le pedimos a la base de datos que CUENTE cuántos usuarios tienen ese mismo apodo
        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ?";

        // Abrimos la conexión y preparamos la búsqueda selectiva
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Pasamos el username que queremos verificar al '?'
            ps.setString(1, username);
            // Ejecutamos la consulta para obtener el conteo
            try (ResultSet rs = ps.executeQuery()) {
                // Si la consulta nos devuelve el resultado del conteo...
                if (rs.next()) {
                    // Si el conteo en la primera columna es mayor a 0, significa que el username ya existe (devuelve true)
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            // Informamos si hubo un problema al consultar la tabla
            System.err.println("[UsuarioDAO] Error al verificar existencia del username: " + e.getMessage());
        }
        // Si no se encontró nada o hubo un error, asumimos por seguridad que no existe (false)
        return false;
    }

    /**
     * Verifica si una dirección de correo ya está registrada en el sistema.
     */
    public boolean emailExiste(String email) {
        // Le pedimos a la base de datos que CUENTE cuántos usuarios tienen ese mismo correo electrónico
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";

        // Abrimos la conexión y configuramos la consulta de conteo
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Colocamos el correo bajo revisión en el lugar del '?'
            ps.setString(1, email);
            // Ejecutamos la consulta y analizamos el resultado
            try (ResultSet rs = ps.executeQuery()) {
                // Si logramos leer la respuesta del conteo...
                if (rs.next()) {
                    // Si el número contado es mayor a 0, significa que el correo ya está registrado (devuelve true)
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            // Avisamos en consola en caso de que falle la verificación en la base de datos
            System.err.println("[UsuarioDAO] Error al verificar existencia del correo: " + e.getMessage());
        }
        // Si hay un error o no se encuentra el correo, devolvemos false
        return false;
    }
}