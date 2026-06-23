package xyz.jugueteria.views;

import xyz.jugueteria.dao.ProductoDAO;
import xyz.jugueteria.models.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Pantalla de Gestión de Productos.
 * Rediseñada con un fondo oscuro y detalles modernos en azul vibrante.
 */
public class ProductosView extends JPanel {

    private ProductoDAO productoDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtNombre, txtPrecio, txtStock, txtIdCategoria;
    private JCheckBox chkRequiereBaterias;

    public ProductosView() {
        productoDAO = new ProductoDAO();
        setLayout(new BorderLayout(15, 15));
        
        // El fondo oscuro principal (Gris asfalto)
        setBackground(new Color(30, 32, 40)); 
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Título estilizado
        JLabel lblTitle = new JLabel("Gestión de Productos");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        add(lblTitle, BorderLayout.NORTH);

        // --- Tabla Moderna ---
        tableModel = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio (S/.)", "Stock", "ID Categoria", "Baterias"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setBackground(new Color(40, 42, 54)); // Fondo de celdas oscuro
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 62, 74));
        table.setShowVerticalLines(false); // Estilo moderno, sin líneas verticales

        // Cabecera con acento azul
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 122, 255)); // Azul vibrante
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Centrar el contenido de la tabla
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(30, 32, 40)); // Fondo al scrollear
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Sin borde exterior feo
        add(scrollPane, BorderLayout.CENTER);

        // --- Panel de Formulario y Botones ---
        JPanel panelBottom = new JPanel(new BorderLayout(15, 15));
        panelBottom.setBackground(new Color(30, 32, 40));
        
        JPanel panelForm = new JPanel(new GridLayout(2, 5, 10, 10));
        panelForm.setBackground(new Color(30, 32, 40));

        txtNombre = crearTextFieldOscuro();
        txtPrecio = crearTextFieldOscuro();
        txtStock = crearTextFieldOscuro();
        txtIdCategoria = crearTextFieldOscuro();
        
        chkRequiereBaterias = new JCheckBox("Req. Baterías");
        chkRequiereBaterias.setBackground(new Color(30, 32, 40));
        chkRequiereBaterias.setForeground(Color.WHITE);
        chkRequiereBaterias.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panelForm.add(crearLabelBlanco("Nombre:"));
        panelForm.add(crearLabelBlanco("Precio:"));
        panelForm.add(crearLabelBlanco("Stock:"));
        panelForm.add(crearLabelBlanco("ID Categoria:"));
        panelForm.add(crearLabelBlanco("Extras:"));

        panelForm.add(txtNombre);
        panelForm.add(txtPrecio);
        panelForm.add(txtStock);
        panelForm.add(txtIdCategoria);
        panelForm.add(chkRequiereBaterias);

        // Botones rediseñados
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelBotones.setBackground(new Color(30, 32, 40));
        
        JButton btnRefrescar = crearBotonPrimario("Refrescar");
        JButton btnEliminar = crearBotonPeligro("Eliminar");
        JButton btnAgregar = crearBotonPrimario("Guardar Nuevo");

        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnAgregar);

        panelBottom.add(panelForm, BorderLayout.CENTER);
        panelBottom.add(panelBotones, BorderLayout.SOUTH);

        add(panelBottom, BorderLayout.SOUTH);

        // --- Eventos ---
        btnRefrescar.addActionListener(e -> cargarDatos());
        
        btnAgregar.addActionListener(e -> {
            try {
                Producto p = new Producto();
                p.setNombre(txtNombre.getText());
                p.setPrecio(Double.parseDouble(txtPrecio.getText()));
                p.setStock(Integer.parseInt(txtStock.getText()));
                p.setIdCategoria(Integer.parseInt(txtIdCategoria.getText()));
                p.setRequiereBaterias(chkRequiereBaterias.isSelected());
                
                if (productoDAO.registrarProducto(p)) {
                    JOptionPane.showMessageDialog(this, "Producto registrado con éxito.");
                    cargarDatos();
                    limpiarForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Por favor revise los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) tableModel.getValueAt(row, 0);
                if (productoDAO.eliminarProducto(id)) {
                    JOptionPane.showMessageDialog(this, "Producto eliminado.");
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla.");
            }
        });

        cargarDatos();
    }

    private void cargarDatos() {
        tableModel.setRowCount(0);
        List<Producto> productos = productoDAO.listarProductos();
        for (Producto p : productos) {
            tableModel.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio(), p.getStock(), p.getIdCategoria(), p.isRequiereBaterias() ? "Sí" : "No"});
        }
    }

    private void limpiarForm() {
        txtNombre.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
        txtIdCategoria.setText("");
        chkRequiereBaterias.setSelected(false);
    }
    
    // --- Helpers Visuales ---
    
    private JLabel crearLabelBlanco(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(new Color(200, 200, 200));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
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
    
    private JButton crearBotonPeligro(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(255, 85, 85));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
