package xyz.jugueteria.dao; // Le decimos a Java en qué carpeta (paquete) vive este archivo.

import xyz.jugueteria.database.ConexionDB; // Traemos nuestra clase ConexionDB para poder hablar con MariaDB.
import xyz.jugueteria.models.Cliente; // Importamos el "molde" Cliente para saber qué datos tiene un cliente.

// Importamos las herramientas nativas de Java para manejar bases de datos.
import java.sql.Connection; // Para mantener la llamada abierta con la base de datos.
import java.sql.PreparedStatement; // Para preparar nuestras consultas SQL de forma segura.
import java.sql.ResultSet; // Para recibir y leer los resultados que nos devuelva la base de datos.
import java.sql.SQLException; // Para capturar cualquier error si la base de datos falla.
import java.util.ArrayList; // Herramienta para crear listas dinámicas.
import java.util.List; // La interfaz general para manejar listas en Java.

/**
 * Data Access Object para la tabla 'clientes'.
 * Cada método abre su propia conexión fresca y la cierra al terminar (try-with-resources).
 */
public class ClienteDAO { // Declaramos nuestra clase principal, pública para que el resto del sistema pueda usarla.

    public boolean registrarCliente(Cliente cliente) { // Método que recibe un cliente y devuelve true si se guardó bien.

        // Preparamos el SQL. Los "?" son escudos contra inyección SQL; ahí irán los datos reales luego.
        String sql = "INSERT INTO clientes (dni, nombre_completo, telefono, direccion) VALUES (?, ?, ?, ?)";

        // Iniciamos un bloque try-with-resources. Esto asegura que la conexión se cierre sola al terminar, ¡sin colapsar el sistema!
        try (Connection con = ConexionDB.getConexion(); // Abrimos la conexión a la base de datos.
             PreparedStatement ps = con.prepareStatement(sql)) { // Subimos nuestro SQL al "vehículo" que lo llevará a la BD.

            // Cambiamos el primer "?" por el DNI del cliente.
            ps.setString(1, cliente.getDni());
            // Cambiamos el segundo "?" por el nombre del cliente.
            ps.setString(2, cliente.getNombreCompleto());
            // Cambiamos el tercer "?" por el teléfono.
            ps.setString(3, cliente.getTelefono());
            // Cambiamos el cuarto "?" por la dirección.
            ps.setString(4, cliente.getDireccion());

            int filas = ps.executeUpdate(); // Disparamos el SQL a la BD. Nos devuelve cuántas filas se crearon.
            System.out.println("[ClienteDAO] Cliente insertado. Filas afectadas: " + filas); // Imprimimos un mensaje en consola para control nuestro.
            return filas > 0; // Si se modificó 1 o más filas, devuelve verdadero (¡éxito!).

        } catch (SQLException e) { // Si algo explota en la BD, atrapamos el error aquí para que el programa no se muera.
            System.err.println("[ClienteDAO] Error al registrar cliente: " + e.getMessage()); // Mostramos qué falló en la consola.
            e.printStackTrace(); // Imprimimos el rastro del error completo.
            return false; // Devolvemos falso porque no se pudo guardar.
        }
    }

    public List<Cliente> listarClientes() { // Método para traer a todos los clientes. Devuelve una lista.
        List<Cliente> lista = new ArrayList<>(); // Creamos una lista vacía donde iremos metiendo a los clientes que lleguen.
        String sql = "SELECT * FROM clientes"; // El SQL clásico para traer todos los registros de la tabla clientes.

        // Abrimos conexión y preparamos el SQL igual que en el método de registrar.
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { // Ejecutamos la consulta y guardamos la "tabla virtual" de resultados en "rs".

            while (rs.next()) { // Mientras haya una siguiente fila de clientes en los resultados...
                lista.add(mapearCliente(rs)); // Convertimos esa fila en un objeto Cliente y lo agregamos a nuestra lista.
            }
            System.out.println("[ClienteDAO] Clientes cargados: " + lista.size()); // Aviso en consola de cuántos clientes encontramos.

        } catch (SQLException e) { // Atrapamos cualquier error de conexión o consulta.
            System.err.println("[ClienteDAO] Error al listar clientes: " + e.getMessage());
            e.printStackTrace();
        }
        return lista; // Devolvemos la lista llena (o vacía si hubo error).
    }

    public boolean actualizarCliente(Cliente cliente) { // Método para editar un cliente que ya existe en la BD.

        // El SQL para actualizar. Ojo con el WHERE, es crucial para no actualizar a todos los clientes por error.
        String sql = "UPDATE clientes SET dni = ?, nombre_completo = ?, telefono = ?, direccion = ? WHERE id_cliente = ?";

        // Try-with-resources para manejar la conexión de forma segura y automática.
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Llenamos los datos nuevos en los "?" en orden.
            ps.setString(1, cliente.getDni());
            ps.setString(2, cliente.getNombreCompleto());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getDireccion());
            // Llenamos el 5to "?" que es el ID (para saber exactamente a qué cliente actualizar).
            ps.setInt(5, cliente.getIdCliente());

            int filas = ps.executeUpdate(); // Ejecutamos el cambio en la BD.
            return filas > 0; // Si se actualizó al menos una fila, devuelve true.

        } catch (SQLException e) { // Si algo sale mal, atrapamos el error para no colapsar.
            System.err.println("[ClienteDAO] Error al actualizar cliente: " + e.getMessage());
            e.printStackTrace();
            return false; // Falló la actualización.
        }
    }

    /**
     * Si el cliente tiene ventas asociadas, la BD lanzará un error por la FK RESTRICT.
     */
    public boolean eliminarCliente(int idCliente) { // Método para borrar, solo necesitamos saber su ID.
        String sql = "DELETE FROM clientes WHERE id_cliente = ?"; // Sentencia SQL para borrar un registro específico.

        try (Connection con = ConexionDB.getConexion(); // Abrimos conexión.
             PreparedStatement ps = con.prepareStatement(sql)) { // Preparamos la sentencia.

            ps.setInt(1, idCliente); // Reemplazamos el único "?" con el ID del cliente a borrar.
            int filas = ps.executeUpdate(); // Ejecutamos la orden de borrar.
            return filas > 0; // Si se borró, devuelve true.

        } catch (SQLException e) { // Control de errores por si falla la conexión o hay llaves foráneas bloqueando el borrado.
            System.err.println("[ClienteDAO] Error al eliminar cliente: " + e.getMessage());
            e.printStackTrace();
            return false; // No se pudo borrar.
        }
    }

    // Método ayudante. Es "private" porque solo se usa aquí adentro, no necesitamos exponerlo a otras clases.
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente(); // Creamos un cliente en blanco.

        c.setIdCliente(rs.getInt("id_cliente")); // Sacamos el ID de la fila de la BD y se lo asignamos a nuestro objeto.
        c.setDni(rs.getString("dni")); // Sacamos el DNI y lo guardamos.
        c.setNombreCompleto(rs.getString("nombre_completo")); // Sacamos el nombre y lo guardamos.
        c.setTelefono(rs.getString("telefono")); // Sacamos el teléfono.
        c.setDireccion(rs.getString("direccion")); // Sacamos la dirección.

        return c; // Devolvemos el cliente ya armadito con todos sus datos.
    }
}
