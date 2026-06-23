package xyz.jugueteria.dao;

import xyz.jugueteria.database.ConexionDB;
import xyz.jugueteria.models.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para la tabla 'clientes'.
 * Cada método abre su propia conexión fresca y la cierra al terminar (try-with-resources).
 */
public class ClienteDAO {

    public boolean registrarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (dni, nombre_completo, telefono, direccion) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getDni());
            ps.setString(2, cliente.getNombreCompleto());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getDireccion());

            int filas = ps.executeUpdate();
            System.out.println("[ClienteDAO] Cliente insertado. Filas afectadas: " + filas);
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("[ClienteDAO] Error al registrar cliente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }
            System.out.println("[ClienteDAO] Clientes cargados: " + lista.size());

        } catch (SQLException e) {
            System.err.println("[ClienteDAO] Error al listar clientes: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public boolean actualizarCliente(Cliente cliente) {
        String sql = "UPDATE clientes SET dni = ?, nombre_completo = ?, telefono = ?, direccion = ? WHERE id_cliente = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getDni());
            ps.setString(2, cliente.getNombreCompleto());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getDireccion());
            ps.setInt(5, cliente.getIdCliente());

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("[ClienteDAO] Error al actualizar cliente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Si el cliente tiene ventas asociadas, la BD lanzará un error por la FK RESTRICT.
     */
    public boolean eliminarCliente(int idCliente) {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("[ClienteDAO] Error al eliminar cliente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdCliente(rs.getInt("id_cliente"));
        c.setDni(rs.getString("dni"));
        c.setNombreCompleto(rs.getString("nombre_completo"));
        c.setTelefono(rs.getString("telefono"));
        c.setDireccion(rs.getString("direccion"));
        return c;
    }
}
