package xyz.jugueteria.views;

import xyz.jugueteria.dao.ClienteDAO;
import xyz.jugueteria.models.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Pantalla de Gestión de Clientes.
 * Adaptada al nuevo look & feel oscuro y moderno.
 */
public class ClientesView extends JPanel {

    private ClienteDAO clienteDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtDni, txtNombre, txtTelefono, txtDireccion;

    public ClientesView() {
        clienteDAO = new ClienteDAO();
        setLayout(new BorderLayout(15, 15));
        
        // Fondo asfalto para mantener consistencia
        setBackground(new Color(30, 32, 40)); 
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Título de la sección
        JLabel lblTitle = new JLabel("Directorio de Clientes");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        add(lblTitle, BorderLayout.NORTH);

        // --- Tabla de Clientes ---
        tableModel = new DefaultTableModel(new String[]{"ID", "DNI", "Nombre", "Teléfono", "Dirección"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setBackground(new Color(40, 42, 54)); 
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(60, 62, 74));
        table.setShowVerticalLines(false);

        // Cabecera estilizada
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 122, 255)); // El azul distintivo
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(30, 32, 40));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // --- Formulario de Entrada ---
        JPanel panelBottom = new JPanel(new BorderLayout(15, 15));
        panelBottom.setBackground(new Color(30, 32, 40));
        
        JPanel panelForm = new JPanel(new GridLayout(2, 4, 10, 10));
        panelForm.setBackground(new Color(30, 32, 40));

        txtDni = crearTextFieldOscuro();
        txtNombre = crearTextFieldOscuro();
        txtTelefono = crearTextFieldOscuro();
        txtDireccion = crearTextFieldOscuro();

        panelForm.add(crearLabelBlanco("DNI:"));
        panelForm.add(crearLabelBlanco("Nombre Completo:"));
        panelForm.add(crearLabelBlanco("Teléfono:"));
        panelForm.add(crearLabelBlanco("Dirección:"));

        panelForm.add(txtDni);
        panelForm.add(txtNombre);
        panelForm.add(txtTelefono);
        panelForm.add(txtDireccion);

        // Botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelBotones.setBackground(new Color(30, 32, 40));
        
        JButton btnRefrescar = crearBotonPrimario("Actualizar Lista");
        JButton btnEliminar = crearBotonPeligro("Borrar Seleccionado");
        JButton btnAgregar = crearBotonPrimario("Guardar Cliente");

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
                Cliente c = new Cliente();
                c.setDni(txtDni.getText());
                c.setNombreCompleto(txtNombre.getText());
                c.setTelefono(txtTelefono.getText());
                c.setDireccion(txtDireccion.getText());
                
                if (clienteDAO.registrarCliente(c)) {
                    JOptionPane.showMessageDialog(this, "Cliente registrado con éxito.");
                    cargarDatos();
                    limpiarForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Por favor revise los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) tableModel.getValueAt(row, 0);
                if (clienteDAO.eliminarCliente(id)) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado.");
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar (Puede que tenga ventas asociadas).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla.");
            }
        });

        cargarDatos();
    }

    private void cargarDatos() {
        tableModel.setRowCount(0);
        List<Cliente> clientes = clienteDAO.listarClientes();
        for (Cliente c : clientes) {
            tableModel.addRow(new Object[]{c.getIdCliente(), c.getDni(), c.getNombreCompleto(), c.getTelefono(), c.getDireccion()});
        }
    }

    private void limpiarForm() {
        txtDni.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
    }
    
    // --- Utilidades visuales reutilizadas ---
    
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
