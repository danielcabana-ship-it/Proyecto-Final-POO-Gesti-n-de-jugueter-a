package xyz.jugueteria.views;

import xyz.jugueteria.dao.ClienteDAO;
import xyz.jugueteria.dao.ProductoDAO;
import xyz.jugueteria.dao.VentaDAO;
import xyz.jugueteria.models.Cliente;
import xyz.jugueteria.models.DetalleVenta;
import xyz.jugueteria.models.Producto;
import xyz.jugueteria.models.Venta;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentasView extends JPanel {

    private VentaDAO ventaDAO;
    private ClienteDAO clienteDAO;
    private ProductoDAO productoDAO;

    private JComboBox<Cliente> cbClientes;
    private JComboBox<Producto> cbProductos;
    private JTextField txtCantidad;
    
    private JTable tableDetalles;
    private DefaultTableModel tableModel;
    
    private JLabel lblTotal;
    private double totalVenta = 0.0;
    
    private List<DetalleVenta> detallesActuales;

    public VentasView() {
        ventaDAO = new VentaDAO();
        clienteDAO = new ClienteDAO();
        productoDAO = new ProductoDAO();
        detallesActuales = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel lblTitle = new JLabel("Punto de Venta");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // Panel Superior: Selección de Cliente y Producto
        JPanel panelTop = new JPanel(new GridLayout(2, 1, 10, 10));
        
        JPanel panelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCliente.add(new JLabel("Cliente:"));
        cbClientes = new JComboBox<>();
        panelCliente.add(cbClientes);
        
        JPanel panelProducto = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelProducto.add(new JLabel("Producto:"));
        cbProductos = new JComboBox<>();
        panelProducto.add(cbProductos);
        
        panelProducto.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField(5);
        panelProducto.add(txtCantidad);
        
        JButton btnAgregarDetalle = new JButton("Agregar a Venta");
        panelProducto.add(btnAgregarDetalle);

        panelTop.add(panelCliente);
        panelTop.add(panelProducto);
        
        add(panelTop, BorderLayout.NORTH);

        // Tabla de Detalles
        tableModel = new DefaultTableModel(new String[]{"ID Producto", "Producto", "Cantidad", "Precio Unitario", "Subtotal"}, 0);
        tableDetalles = new JTable(tableModel);
        tableDetalles.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(tableDetalles);
        add(scrollPane, BorderLayout.CENTER);

        // Panel Inferior: Total y Botón Finalizar
        JPanel panelBottom = new JPanel(new BorderLayout(10, 10));
        
        lblTotal = new JLabel("Total: S/. 0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotal.setForeground(new Color(80, 250, 123)); // Verde claro
        
        JButton btnFinalizar = new JButton("Finalizar Venta");
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnFinalizar.setBackground(new Color(255, 85, 85)); // Rojo suave
        btnFinalizar.setForeground(Color.WHITE);

        panelBottom.add(lblTotal, BorderLayout.WEST);
        panelBottom.add(btnFinalizar, BorderLayout.EAST);

        add(panelBottom, BorderLayout.SOUTH);

        // Eventos
        btnAgregarDetalle.addActionListener(e -> agregarDetalle());
        btnFinalizar.addActionListener(e -> finalizarVenta());

        cargarCombos();
    }

    private void cargarCombos() {
        cbClientes.removeAllItems();
        for (Cliente c : clienteDAO.listarClientes()) {
            cbClientes.addItem(c);
        }

        cbProductos.removeAllItems();
        for (Producto p : productoDAO.listarProductos()) {
            cbProductos.addItem(p);
        }
    }

    private void agregarDetalle() {
        Producto p = (Producto) cbProductos.getSelectedItem();
        if (p == null) return;
        
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
                return;
            }
            if (cantidad > p.getStock()) {
                JOptionPane.showMessageDialog(this, "Stock insuficiente. Disponible: " + p.getStock());
                return;
            }

            double subtotal = cantidad * p.getPrecio();
            
            DetalleVenta detalle = new DetalleVenta(0, 0, p.getCodigo(), cantidad, p.getPrecio(), subtotal);
            detallesActuales.add(detalle);
            
            tableModel.addRow(new Object[]{p.getCodigo(), p.getNombre(), cantidad, p.getPrecio(), subtotal});
            
            totalVenta += subtotal;
            lblTotal.setText(String.format("Total: S/. %.2f", totalVenta));
            txtCantidad.setText("");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida.");
        }
    }

    private void finalizarVenta() {
        if (detallesActuales.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos en la venta.");
            return;
        }
        
        Cliente cliente = (Cliente) cbClientes.getSelectedItem();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente.");
            return;
        }

        Venta venta = new Venta();
        venta.setIdCliente(cliente.getIdCliente());
        venta.setTotal(totalVenta);

        if (ventaDAO.registrarVentaCompleta(venta, detallesActuales)) {
            JOptionPane.showMessageDialog(this, "¡Venta registrada con éxito!");
            // Limpiar la vista
            detallesActuales.clear();
            tableModel.setRowCount(0);
            totalVenta = 0.0;
            lblTotal.setText("Total: S/. 0.00");
            cargarCombos(); // Recargar productos para actualizar stock
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar la venta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
