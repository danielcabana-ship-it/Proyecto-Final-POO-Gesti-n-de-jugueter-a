package xyz.jugueteria.dao;

// Importamos la clase que maneja la conexión a la base de datos
import xyz.jugueteria.database.ConexionDB;
// Importamos el modelo de Producto para poder usar sus objetos
import xyz.jugueteria.models.Producto;

// Importamos las herramientas de Java SQL para interactuar con la base de datos
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// Importamos colecciones básicas para manejar listas de datos
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la tabla 'productos'.
 * Cada método abre su propia conexión fresca y la cierra al terminar (try-with-resources).
 */
public class ProductoDAO {

    // Método para guardar un juguete nuevo en la base de datos
    public boolean registrarProducto(Producto producto) {
        // La plantilla SQL con '?' que luego llenaremos con los datos reales del producto
        String sql = "INSERT INTO productos (nombre, precio, stock, id_categoria, requiere_baterias) VALUES (?, ?, ?, ?, ?)";

        // Abrimos la conexión y preparamos la consulta. El 'try-with-resources' asegura que todo se cierre solo al final
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Reemplazamos cada '?' en orden con las propiedades del objeto producto
            ps.setString(1, producto.getNombre());        // Primer '?': Nombre del juguete
            ps.setDouble(2, producto.getPrecio());        // Segundo '?': Su precio
            ps.setInt(3, producto.getStock());            // Tercer '?': Cuántos hay en inventario
            ps.setInt(4, producto.getIdCategoria());      // Cuarto '?': El ID de su categoría
            ps.setBoolean(5, producto.isRequiereBaterias()); // Quinto '?': ¿Lleva pilas o no?

            // Ejecutamos la orden en la base de datos y guardamos cuántas filas se crearon
            int filas = ps.executeUpdate();
            // Avisamos en consola que todo salió bien y cuántos registros se insertaron
            System.out.println("[ProductoDAO] Producto insertado. Filas afectadas: " + filas);

            // Si se afectó al menos una fila, significa que el registro fue un éxito (devuelve true)
            return filas > 0;

        } catch (SQLException e) {
            // Si algo falla (ej. problemas de conexión), atrapamos el error y avisamos en consola
            System.err.println("[ProductoDAO] Error al registrar producto: " + e.getMessage());
            e.printStackTrace(); // Muestra el rastro completo del error para investigar qué pasó
            return false; // Retornamos false porque el producto no se pudo guardar
        }
    }

    // Método para traer todos los productos almacenados en la juguetería
    public List<Producto> listarProductos() {
        // Creamos una lista vacía donde iremos guardando los juguetes que encontremos
        List<Producto> lista = new ArrayList<>();
        // La consulta SQL directa para pedir todos los datos de la tabla productos
        String sql = "SELECT * FROM productos";

        // Abrimos la conexión, preparamos la consulta y ejecutamos el SELECT para obtener los resultados
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Mientras la base de datos nos siga entregando filas de resultados...
            while (rs.next()) {
                // Convertimos esa fila de la base de datos en un objeto Java 'Producto' y lo sumamos a la lista
                lista.add(mapearProducto(rs));
            }
            // Informamos en consola cuántos productos logramos cargar con éxito
            System.out.println("[ProductoDAO] Productos cargados: " + lista.size());

        } catch (SQLException e) {
            // Si ocurre un error al leer los datos, lo reportamos aquí
            System.err.println("[ProductoDAO] Error al listar productos: " + e.getMessage());
            e.printStackTrace();
        }
        // Devolvemos la lista (que puede tener productos o estar vacía si hubo error)
        return lista;
    }

    // Método para buscar un juguete específico usando su código único
    public Producto obtenerProductoPorId(int codigo) {
        // Consulta SQL para buscar un único registro que coincida con el código enviado
        String sql = "SELECT * FROM productos WHERE codigo = ?";

        // Abrimos la conexión y preparamos la consulta
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Asignamos el código que nos pasaron al '?' de la consulta
            ps.setInt(1, codigo);
            // Ejecutamos la búsqueda y abrimos la caja de resultados (ResultSet)
            try (ResultSet rs = ps.executeQuery()) {
                // Si la base de datos encontró un registro que coincida...
                if (rs.next()) {
                    // Lo transformamos en objeto Producto y lo devolvemos inmediatamente
                    return mapearProducto(rs);
                }
            }
        } catch (SQLException e) {
            // Reportamos en consola si hubo fallas en la búsqueda
            System.err.println("[ProductoDAO] Error al obtener producto: " + e.getMessage());
            e.printStackTrace();
        }
        // Si no se encontró nada o hubo un error, devolvemos 'null' (nada)
        return null;
    }

    // Método para modificar los datos de un juguete que ya existe
    public boolean actualizarProducto(Producto producto) {
        // Sentencia SQL para actualizar los campos basados en el código del producto
        String sql = "UPDATE productos SET nombre = ?, precio = ?, stock = ?, id_categoria = ?, requiere_baterias = ? WHERE codigo = ?";

        // Abrimos conexión y preparamos la actualización
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Llenamos los '?' en orden con la nueva información que viene en el objeto
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getStock());
            ps.setInt(4, producto.getIdCategoria());
            ps.setBoolean(5, producto.isRequiereBaterias());
            ps.setInt(6, producto.getCodigo()); // El sexto '?' define a QUÉ producto vamos a cambiar los datos

            // Ejecutamos el cambio en la base de datos y vemos cuántas filas cambiaron
            int filas = ps.executeUpdate();
            // Retorna true si se modificó el producto correctamente (filas > 0)
            return filas > 0;

        } catch (SQLException e) {
            // Si algo sale mal en la actualización, mandamos el aviso
            System.err.println("[ProductoDAO] Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para borrar un juguete del sistema usando su código
    public boolean eliminarProducto(int codigo) {
        // Sentencia SQL para eliminar un registro por su identificador único
        String sql = "DELETE FROM productos WHERE codigo = ?";

        // Abrimos conexión y preparamos el borrado
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Le decimos a la consulta cuál es el código del juguete que se va a eliminar
            ps.setInt(1, codigo);
            // Ejecutamos la eliminación
            int filas = ps.executeUpdate();
            // Si se borró el registro, filas será mayor a 0 y devolverá true
            return filas > 0;

        } catch (SQLException e) {
            // Capturamos problemas en caso de que no se pueda eliminar (ej. restricciones de clave foránea)
            System.err.println("[ProductoDAO] Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Este es un método privado de apoyo (un "traductor")
    // Toma una fila de la base de datos (ResultSet) y la convierte en un objeto Producto de Java
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        // Creamos una instancia vacía de Producto
        Producto p = new Producto();
        // Sacamos los datos de las columnas de la base de datos y se los asignamos a las variables del objeto
        p.setCodigo(rs.getInt("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setRequiereBaterias(rs.getBoolean("requiere_baterias"));
        // Devolvemos el producto completamente armado y listo para usar en Java
        return p;
    }
}