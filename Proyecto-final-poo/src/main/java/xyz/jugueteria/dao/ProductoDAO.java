package xyz.jugueteria.dao;

import xyz.jugueteria.database.ConexionDB;
import xyz.jugueteria.models.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la tabla 'productos'.
 * Cada método abre su propia conexión fresca y la cierra al terminar (try-with-resources).
 */
public class ProductoDAO {

    public boolean registrarProducto(Producto producto) {
        String sql = "INSERT INTO productos (nombre, precio, stock, id_categoria, requiere_baterias) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getStock());
            ps.setInt(4, producto.getIdCategoria());
            ps.setBoolean(5, producto.isRequiereBaterias());

            int filas = ps.executeUpdate();
            System.out.println("[ProductoDAO] Producto insertado. Filas afectadas: " + filas);
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al registrar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
            System.out.println("[ProductoDAO] Productos cargados: " + lista.size());

        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al listar productos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public Producto obtenerProductoPorId(int codigo) {
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al obtener producto: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, precio = ?, stock = ?, id_categoria = ?, requiere_baterias = ? WHERE codigo = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getStock());
            ps.setInt(4, producto.getIdCategoria());
            ps.setBoolean(5, producto.isRequiereBaterias());
            ps.setInt(6, producto.getCodigo());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarProducto(int codigo) {
        String sql = "DELETE FROM productos WHERE codigo = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, codigo);
            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("[ProductoDAO] Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setCodigo(rs.getInt("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setRequiereBaterias(rs.getBoolean("requiere_baterias"));
        return p;
    }
}