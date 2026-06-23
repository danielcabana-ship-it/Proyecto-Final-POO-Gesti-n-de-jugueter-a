package xyz.jugueteria.views;

import xyz.jugueteria.dao.ProductoDAO;
import xyz.jugueteria.models.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductosView extends JPanel {

    private ProductoDAO productoDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtNombre, txtPrecio, txtStock, txtIdCategoria;
    private JCheckBox chkRequiereBaterias;

    public ProductosView() {
        productoDAO = new ProductoDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel lblTitle = new JLabel("Gestión de Productos");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // Tabla
        tableModel = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio", "Stock", "ID Categoria", "Baterias"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de Formulario y Botones
        JPanel panelBottom = new JPanel(new BorderLayout(10, 10));
        
        JPanel panelForm = new JPanel(new GridLayout(2, 5, 10, 10));
        txtNombre = new JTextField();
        txtPrecio = new JTextField();
        txtStock = new JTextField();
        txtIdCategoria = new JTextField();
        chkRequiereBaterias = new JCheckBox("Req. Baterías");

        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(new JLabel("Precio:"));
        panelForm.add(new JLabel("Stock:"));
        panelForm.add(new JLabel("ID Categoria:"));
        panelForm.add(new JLabel("Extras:"));

        panelForm.add(txtNombre);
        panelForm.add(txtPrecio);
        panelForm.add(txtStock);
        panelForm.add(txtIdCategoria);
        panelForm.add(chkRequiereBaterias);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnAgregar);

        panelBottom.add(panelForm, BorderLayout.CENTER);
        panelBottom.add(panelBotones, BorderLayout.SOUTH);

        add(panelBottom, BorderLayout.SOUTH);

        // Eventos
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
}
