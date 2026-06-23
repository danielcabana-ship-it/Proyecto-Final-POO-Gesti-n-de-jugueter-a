package xyz.jugueteria.dao;

import xyz.jugueteria.database.ConexionDB;
import xyz.jugueteria.models.DetalleVenta;
import xyz.jugueteria.models.Venta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para las tablas 'ventas' y 'detalle_ventas'.
 */
public class VentaDAO {

    /**
     * Registra cabecera + detalles en una sola transacción atómica.
     * Si algún paso falla se hace rollback completo para evitar datos huérfanos.
     */
    public boolean registrarVentaCompleta(Venta venta, List<DetalleVenta> detalles) {
        String sqlVenta = "INSERT INTO ventas (id_cliente, total) VALUES (?, ?)";
        String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlStock = "UPDATE productos SET stock = stock - ? WHERE codigo = ?";

        Connection con = null;
        try {
            con = ConexionDB.getInstancia().getConexion();
            con.setAutoCommit(false);

            int idVentaGenerado = insertarCabecera(con, sqlVenta, venta);
            insertarDetallesYActualizarStock(con, sqlDetalle, sqlStock, detalles, idVentaGenerado);

            con.commit();
            return true;
        } catch (SQLException e) {
            rollbackSilencioso(con);
            System.err.println("Error al registrar venta: " + e.getMessage());
            return false;
        } finally {
            restaurarAutoCommit(con);
        }
    }

    public List<Venta> listarVentas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT * FROM ventas";

        try (Connection con = ConexionDB.getInstancia().getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Venta v = new Venta();
                v.setIdVenta(rs.getInt("id_venta"));
                v.setIdCliente(rs.getInt("id_cliente"));
                v.setFechaVenta(rs.getTimestamp("fecha_venta"));
                v.setTotal(rs.getDouble("total"));
                lista.add(v);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar ventas: " + e.getMessage());
        }
        return lista;
    }

    // --- Métodos auxiliares privados para mantener registrarVentaCompleta() legible ---

    private int insertarCabecera(Connection con, String sql, Venta venta) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, venta.getIdCliente());
            ps.setDouble(2, venta.getTotal());
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
             PreparedStatement psStock = con.prepareStatement(sqlStock)) {

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

    private void restaurarAutoCommit(Connection con) {
        try { if (con != null) con.setAutoCommit(true); } catch (SQLException ignored) {}
    }
}
