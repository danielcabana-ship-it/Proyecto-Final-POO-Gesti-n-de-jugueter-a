package xyz.jugueteria.dao;

import xyz.jugueteria.database.ConexionDB;
import xyz.jugueteria.models.DetalleVenta;
import xyz.jugueteria.models.Venta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    /**
     * Registra una venta completa (Cabecera y Detalles) usando transacciones.
     * Así evitamos que se guarde una venta sin detalles si ocurre un error.
     */
    public boolean registrarVentaCompleta(Venta venta, List<DetalleVenta> detalles) {
        String sqlVenta = "INSERT INTO ventas (id_cliente, total) VALUES (?, ?)";
        String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlActualizarStock = "UPDATE productos SET stock = stock - ? WHERE codigo = ?";

        Connection con = null;
        try {
            con = ConexionDB.getInstancia().getConexion();
            con.setAutoCommit(false); // Iniciar transacción

            // 1. Insertar Venta
            int idVentaGenerado = 0;
            try (PreparedStatement psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setInt(1, venta.getIdCliente());
                psVenta.setDouble(2, venta.getTotal());
                psVenta.executeUpdate();

                try (ResultSet rsKeys = psVenta.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        idVentaGenerado = rsKeys.getInt(1);
                    }
                }
            }

            // 2. Insertar Detalles y Actualizar Stock
            try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle);
                 PreparedStatement psStock = con.prepareStatement(sqlActualizarStock)) {
                
                for (DetalleVenta detalle : detalles) {
                    // Detalle
                    psDetalle.setInt(1, idVentaGenerado);
                    psDetalle.setInt(2, detalle.getIdProducto());
                    psDetalle.setInt(3, detalle.getCantidad());
                    psDetalle.setDouble(4, detalle.getPrecioUnitario());
                    psDetalle.setDouble(5, detalle.getSubtotal());
                    psDetalle.executeUpdate();

                    // Stock
                    psStock.setInt(1, detalle.getCantidad());
                    psStock.setInt(2, detalle.getIdProducto());
                    psStock.executeUpdate();
                }
            }

            con.commit(); // Confirmar transacción
            return true;
        } catch (SQLException e) {
            try {
                if (con != null) con.rollback(); // Deshacer en caso de error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Error al registrar venta: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
}
