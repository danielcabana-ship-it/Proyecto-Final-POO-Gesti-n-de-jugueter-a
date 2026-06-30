package xyz.jugueteria.dao;

// Importamos la conexión a la base de datos de la juguetería
import xyz.jugueteria.database.ConexionDB;
// Importamos los modelos de DetalleVenta (los juguetes comprados) y Venta (los datos generales del ticket)
import xyz.jugueteria.models.DetalleVenta;
import xyz.jugueteria.models.Venta;

// Importamos las herramientas de SQL para transacciones, consultas y manejo de errores
import java.sql.*;
// Importamos listas para estructurar colecciones de datos
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para las tablas 'ventas' y 'detalle_ventas'.
 *
 * CORRECCIÓN: registrarVentaCompleta() ahora abre una conexión propia,
 * maneja la transacción sobre esa conexión local, y la cierra en el finally.
 * Ya no depende del Singleton roto.
 */
public class VentaDAO {

    /**
     * Registra cabecera + detalles en una sola transacción atómica.
     * Si algún paso falla se hace rollback completo para evitar datos huérfanos.
     */
    public boolean registrarVentaCompleta(Venta venta, List<DetalleVenta> detalles) {
        // SQL para crear el "ticket principal" de la venta (quién compra, quién vende y cuánto da el total)
        String sqlVenta   = "INSERT INTO ventas (id_cliente, id_usuario, total) VALUES (?, ?, ?)";
        // SQL para meter cada juguete de la lista adjunta a ese ticket
        String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        // SQL para restar los juguetes vendidos de nuestro inventario (stock)
        String sqlStock   = "UPDATE productos SET stock = stock - ? WHERE codigo = ?";

        // Preparamos la variable de conexión fuera para poder controlarla con precisión en el bloque catch/finally
        Connection con = null;
        try {
            // Intentamos obtener una conexión limpia a la base de datos
            con = ConexionDB.getConexion();
            // Si por alguna razón la conexión es nula, avisamos del problema y detenemos todo
            if (con == null) {
                System.err.println("[VentaDAO] No se pudo obtener conexión.");
                return false;
            }
            // ¡Paso clave! Desactivamos el guardado automático para iniciar una "transacción manual"
            con.setAutoCommit(false);

            // Paso 1: Intentamos insertar el ticket principal y conseguir el ID que la base de datos le asigne automáticamente
            int idVentaGenerado = insertarCabecera(con, sqlVenta, venta);
            // Si nos devuelve un 0, algo falló generando el ID, por lo que lanzamos un error para detener el proceso
            if (idVentaGenerado == 0) {
                throw new SQLException("No se generó un ID de venta válido.");
            }

            // Paso 2: Usamos el ID recién generado para registrar los juguetes que se compraron y actualizar el stock
            insertarDetallesYActualizarStock(con, sqlDetalle, sqlStock, detalles, idVentaGenerado);

            // Si llegamos hasta aquí sin ningún error, le decimos a la base de datos: "¡Todo perfecto, guarda los cambios definitivamente!"
            con.commit();
            // Informamos en la consola del servidor que la venta fue un éxito rotundo
            System.out.println("[VentaDAO] Venta registrada exitosamente. ID: " + idVentaGenerado);
            return true;

        } catch (SQLException e) {
            // Si CUALQUIER cosa falló (ej. se cayó la luz, un producto no tenía stock, etc.), avisamos del error...
            System.err.println("[VentaDAO] Error en transacción, haciendo rollback: " + e.getMessage());
            e.printStackTrace();
            // ...y ejecutamos el "Rollback" para deshacer los pasos a medio terminar y dejar la base de datos limpia
            rollbackSilencioso(con);
            return false; // Retornamos false porque la venta no se pudo completar
        } finally {
            // Cerramos la conexión manualmente pase lo que pase (éxito o error) para no saturar el servidor
            cerrarConexion(con);
        }
    }

    // Método para consultar el historial de ventas realizadas
    public List<Venta> listarVentas() {
        // Creamos una lista vacía para ir ordenando las ventas que encontremos
        List<Venta> lista = new ArrayList<>();
        // Consulta SQL para traer las ventas de la más nueva a la más antigua
        String sql = "SELECT * FROM ventas ORDER BY id_venta DESC";

        // Abrimos conexión y ejecutamos la consulta directamente usando el try-with-resources automático
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Mientras la base de datos tenga filas con registros de ventas...
            while (rs.next()) {
                // Creamos un objeto Venta y lo rellenamos con los datos de esa fila
                Venta v = new Venta();
                v.setIdVenta(rs.getInt("id_venta"));
                v.setIdCliente(rs.getInt("id_cliente"));
                v.setIdUsuario(rs.getInt("id_usuario"));
                v.setFechaVenta(rs.getTimestamp("fecha_venta")); // Captura el día y la hora exacta de la venta
                v.setTotal(rs.getDouble("total"));
                // Agregamos la venta armada a nuestra lista de resultados
                lista.add(v);
            }
            // Informamos en consola cuántas ventas se listaron
            System.out.println("[VentaDAO] Ventas cargadas: " + lista.size());

        } catch (SQLException e) {
            // Avisamos en consola si hubo un problema al leer el historial de ventas
            System.err.println("[VentaDAO] Error al listar ventas: " + e.getMessage());
            e.printStackTrace();
        }
        // Devolvemos la lista de ventas recopiladas
        return lista;
    }

    // --- Métodos auxiliares privados (Herramientas internas que ayudan a los métodos principales) ---

    // Asistente para insertar los datos principales del ticket (Cabecera)
    private int insertarCabecera(Connection con, String sql, Venta venta) throws SQLException {
        // Preparamos la consulta pidiéndole explícitamente a la base de datos que nos devuelva las llaves autogeneradas (IDs)
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Reemplazamos los '?' por las propiedades generales del ticket
            ps.setInt(1, venta.getIdCliente());
            ps.setInt(2, venta.getIdUsuario());
            ps.setDouble(3, venta.getTotal());
            // Guardamos el ticket principal
            ps.executeUpdate();

            // Abrimos el contenedor especial que guarda la ID generada por el autoincremental
            try (ResultSet keys = ps.getGeneratedKeys()) {
                // Si la ID está disponible, la capturamos y la devolvemos de inmediato
                if (keys.next()) return keys.getInt(1);
            }
        }
        // Si algo falló al recuperar el ID, devolvemos 0
        return 0;
    }

    // Asistente para recorrer la lista de juguetes comprados, meterlos al detalle y actualizar los stocks
    private void insertarDetallesYActualizarStock(Connection con, String sqlDetalle, String sqlStock,
                                                  List<DetalleVenta> detalles, int idVenta) throws SQLException {
        // Preparamos ambas operaciones (insertar detalle y actualizar stock) sobre la misma conexión de la transacción
        try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
             PreparedStatement psStock   = con.prepareStatement(sqlStock)) {

            // Iniciamos un bucle para procesar cada uno de los juguetes del carrito de compras
            for (DetalleVenta d : detalles) {
                // 1. Asignamos los datos del juguete al formato del detalle de venta
                psDetalle.setInt(1, idVenta);          // Vinculamos el detalle al ID del ticket principal
                psDetalle.setInt(2, d.getIdProducto());     // Qué juguete es
                psDetalle.setInt(3, d.getCantidad());       // Cuántos lleva
                psDetalle.setDouble(4, d.getPrecioUnitario()); // A qué precio se le cobró
                psDetalle.setDouble(5, d.getSubtotal());       // El total por este juguete (cantidad * precio)
                // Guardamos este renglón del detalle
                psDetalle.executeUpdate();

                // 2. Asignamos los datos para restarle inventario al juguete que se acaban de llevar
                psStock.setInt(1, d.getCantidad());   // Cuánto vamos a restar del inventario
                psStock.setInt(2, d.getIdProducto()); // Cuál es el código del juguete a modificar
                // Ejecutamos la actualización de inventario en las estanterías de la base de datos
                psStock.executeUpdate();
            }
        }
    }

    // Un borrador silencioso: si la transacción falla, le dice a la base de datos "olvida lo que hicimos" sin alarmar al sistema si ya estaba cerrado
    private void rollbackSilencioso(Connection con) {
        try { if (con != null) con.rollback(); } catch (SQLException ignored) {}
    }

// Un cerraj