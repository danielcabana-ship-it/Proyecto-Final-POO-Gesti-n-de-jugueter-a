package xyz.jugueteria.views;

import xyz.jugueteria.dao.ClienteDAO;
import xyz.jugueteria.dao.ProductoDAO;
import xyz.jugueteria.dao.VentaDAO;
import xyz.jugueteria.models.Cliente;
import xyz.jugueteria.models.DetalleVenta;
import xyz.jugueteria.models.Producto;
import xyz.jugueteria.models.Venta;
import xyz.jugueteria.database.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla de Punto de Venta (POS).
 * Totalmente rediseñada con el nuevo estilo oscuro y toques modernos.
 */
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

        setLayout(new BorderLayout(15, 15));
        
        // Fondo asfalto para mantener consistencia
        setBackground(new Color(30, 32, 40));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Título de la sección
        JLabel lblTitle = new JLabel("Punto de Venta");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        add(lblTitle, BorderLayout.NORTH);

        // --- Panel Superior: Selección de Cliente y Producto ---
        JPanel panelTop = new JPanel(new GridLayout(2, 1, 10, 15));
        panelTop.setBackground(new Color(30, 32, 40));
        
        JPanel panelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelCliente.setBackground(new Color(30, 32, 40));
        panelCliente.add(crearLabelBlanco("Cliente:"));
        cbClientes = new JComboBox<>();
        cbClientes.setPreferredSize(new Dimension(300, 35));
        panelCliente.add(cbClientes);
        
        JPanel panelProducto = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelProducto.setBackground(new Color(30, 32, 40));
        panelProducto.add(crearLabelBlanco("Producto:"));
        cbProductos = new JComboBox<>();
        cbProductos.setPreferredSize(new Dimension(300, 35));
        panelProducto.add(cbProductos);
        
        panelProducto.add(crearLabelBlanco("Cantidad:"));
        txtCantidad = crearTextFieldOscuro();
        txtCantidad.setPreferredSize(new Dimension(80, 35));
        panelProducto.add(txtCantidad);
        
        JButton btnAgregarDetalle = crearBotonPrimario("Agregar a Venta");
        panelProducto.add(btnAgregarDetalle);

        panelTop.add(panelCliente);
        panelTop.add(panelProducto);
        
        add(panelTop, BorderLayout.NORTH);

        // --- Tabla de Detalles ---
        tableModel = new DefaultTableModel(new String[]{"ID Producto", "Producto", "Cantidad", "Precio Unit.", "Subtotal"}, 0);
        tableDetalles = new JTable(tableModel);
        tableDetalles.setRowHeight(35);
        tableDetalles.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableDetalles.setBackground(new Color(40, 42, 54)); 
        tableDetalles.setForeground(Color.WHITE);
        tableDetalles.setGridColor(new Color(60, 62, 74));
        tableDetalles.setShowVerticalLines(false);

        // Cabecera estilizada
        JTableHeader header = tableDetalles.getTableHeader();
        header.setBackground(new Color(0, 122, 255));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableDetalles.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tableDetalles);
        scrollPane.getViewport().setBackground(new Color(30, 32, 40));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // --- Panel Inferior: Total y Botón Finalizar ---
        JPanel panelBottom = new JPanel(new BorderLayout(15, 15));
        panelBottom.setBackground(new Color(30, 32, 40));
        
        lblTotal = new JLabel("Total: S/. 0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTotal.setForeground(new Color(80, 250, 123)); // Verde claro tipo consola
        
        JButton btnFinalizar = new JButton("Finalizar Venta");
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnFinalizar.setBackground(new Color(80, 250, 123)); // Verde claro
        btnFinalizar.setForeground(new Color(20, 50, 20)); // Texto verde oscuro
        btnFinalizar.setFocusPainted(false);
        btnFinalizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFinalizar.setPreferredSize(new Dimension(200, 50));

        panelBottom.add(lblTotal, BorderLayout.WEST);
        panelBottom.add(btnFinalizar, BorderLayout.EAST);

        add(panelBottom, BorderLayout.SOUTH);

        // --- Eventos ---
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
        if (Session.isLogueado()) {
            venta.setIdUsuario(Session.getUsuarioLogueado().getIdUsuario());
        } else {
            venta.setIdUsuario(1); // default / fallback
        }
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
    
    // --- Utilidades visuales reutilizadas ---
    
    private JLabel crearLabelBlanco(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(new Color(200, 200, 200));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return lbl;
    }
    
    private JTextField crearTextFieldOscuro() {
        JTextField txt = new JTextField();
        txt.setBackground(new Color(40, 42, 54));
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);
        txt.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return txt;
    }
    
    private JButton crearBotonPrimario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(0, 122, 255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
