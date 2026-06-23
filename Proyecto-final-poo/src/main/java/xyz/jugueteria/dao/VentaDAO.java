package xyz.jugueteria.dao;

import xyz.jugueteria.database.ConexionDB;
import xyz.jugueteria.models.DetalleVenta;
import xyz.jugueteria.models.Venta;

import java.sql.*;
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
        String sqlVenta   = "INSERT INTO ventas (id_cliente, id_usuario, total) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlStock   = "UPDATE productos SET stock = stock - ? WHERE codigo = ?";

        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            if (con == null) {
                System.err.println("[VentaDAO] No se pudo obtener conexión.");
                return false;
            }
            con.setAutoCommit(false); // Inicio de transacción

            // Paso 1: Insertar cabecera de la venta y obtener el ID generado
            int idVentaGenerado = insertarCabecera(con, sqlVenta, venta);
            if (idVentaGenerado == 0) {
                throw new SQLException("No se generó un ID de venta válido.");
            }

            // Paso 2: Insertar detalles y descontar stock
            insertarDetallesYActualizarStock(con, sqlDetalle, sqlStock, detalles, idVentaGenerado);

            con.commit(); // Confirmar todo
            System.out.println("[VentaDAO] Venta registrada exitosamente. ID: " + idVentaGenerado);
            return true;

        } catch (SQLException e) {
            System.err.println("[VentaDAO] Error en transacción, haciendo rollback: " + e.getMessage());
            e.printStackTrace();
            rollbackSilencioso(con);
            return false;
        } finally {
            // Cerrar la conexión de transacción manualmente (no usamos try-with-resources aquí
            // porque necesitamos controlar el commit/rollback primero)
            cerrarConexion(con);
        }
    }

    public List<Venta> listarVentas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas ORDER BY id_venta DESC";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Venta v = new Venta();
                v.setIdVenta(rs.getInt("id_venta"));
                v.setIdCliente(rs.getInt("id_cliente"));
                v.setIdUsuario(rs.getInt("id_usuario"));
                v.setFechaVenta(rs.getTimestamp("fecha_venta"));
                v.setTotal(rs.getDouble("total"));
                lista.add(v);
            }
            System.out.println("[VentaDAO] Ventas cargadas: " + lista.size());

        } catch (SQLException e) {
            System.err.println("[VentaDAO] Error al listar ventas: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    // --- Métodos auxiliares privados ---

    private int insertarCabecera(Connection con, String sql, Venta venta) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, venta.getIdCliente());
            ps.setInt(2, venta.getIdUsuario());
            ps.setDouble(3, venta.getTotal());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return 0;
    }

    private void insertarDetallesYActualizarStock(Connection con, String sqlDetalle, String sqlStock,
                                                   List<DetalleVenta> detalles, int idVenta) throws SQLException {
        try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
             PreparedStatement psStock   = con.prepareStatement(sqlStock)) {

            for (DetalleVenta d : detalles) {
                psDetalle.setInt(1, idVenta);
                psDetalle.setInt(2, d.getIdProducto());
                psDetalle.setInt(3, d.getCantidad());
                psDetalle.setDouble(4, d.getPrecioUnitario());
                psDetalle.setDouble(5, d.getSubtotal());
                psDetalle.executeUpdate();

                psStock.setInt(1, d.getCantidad());
                psStock.setInt(2, d.getIdProducto());
                psStock.executeUpdate();
            }
        }
    }

    private void rollbackSilencioso(Connection con) {
        try { if (con != null) con.rollback(); } catch (SQLException ignored) {}
    }

    private void cerrarConexion(Connection con) {
        try { if (con != null) con.close(); } catch (SQLException ignored) {}
    }
}
