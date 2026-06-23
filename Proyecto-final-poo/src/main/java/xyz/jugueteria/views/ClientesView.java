package xyz.jugueteria.views;

import xyz.jugueteria.dao.ClienteDAO;
import xyz.jugueteria.models.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientesView extends JPanel {

    private ClienteDAO clienteDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField txtDni, txtNombre, txtTelefono, txtDireccion;

    public ClientesView() {
        clienteDAO = new ClienteDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel lblTitle = new JLabel("Gestión de Clientes");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(lblTitle, BorderLayout.NORTH);

        // Tabla
        tableModel = new DefaultTableModel(new String[]{"ID", "DNI", "Nombre", "Teléfono", "Dirección"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel de Formulario y Botones
        JPanel panelBottom = new JPanel(new BorderLayout(10, 10));
        
        JPanel panelForm = new JPanel(new GridLayout(2, 4, 10, 10));
        txtDni = new JTextField();
        txtNombre = new JTextField();
        txtTelefono = new JTextField();
        txtDireccion = new JTextField();

        panelForm.add(new JLabel("DNI:"));
        panelForm.add(new JLabel("Nombre Completo:"));
        panelForm.add(new JLabel("Teléfono:"));
        panelForm.add(new JLabel("Dirección:"));

        panelForm.add(txtDni);
        panelForm.add(txtNombre);
        panelForm.add(txtTelefono);
        panelForm.add(txtDireccion);

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
}
