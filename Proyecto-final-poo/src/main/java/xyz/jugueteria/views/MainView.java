package xyz.jugueteria.views;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {

    private JPanel panelCentral;
    private CardLayout cardLayout;

    public MainView() {
        setTitle("Sistema de Gestión de Juguetería Premium");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel Lateral (Sidebar)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(6, 1, 10, 10));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));
        sidebar.setBackground(new Color(40, 42, 54));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lblTitle = new JLabel("<html><h2 style='color:white;text-align:center;'>Toy Store</h2></html>");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(lblTitle);

        JButton btnProductos = createMenuButton("Productos");
        JButton btnClientes = createMenuButton("Clientes");
        JButton btnVentas = createMenuButton("Ventas");

        sidebar.add(btnProductos);
        sidebar.add(btnClientes);
        sidebar.add(btnVentas);

        add(sidebar, BorderLayout.WEST);

        // Panel Central (Contenedor de Vistas)
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);

        // Añadimos las vistas
        panelCentral.add(new ProductosView(), "Productos");
        panelCentral.add(new ClientesView(), "Clientes");
        panelCentral.add(new VentasView(), "Ventas");

        add(panelCentral, BorderLayout.CENTER);

        // Eventos
        btnProductos.addActionListener(e -> cardLayout.show(panelCentral, "Productos"));
        btnClientes.addActionListener(e -> cardLayout.show(panelCentral, "Clientes"));
        btnVentas.addActionListener(e -> cardLayout.show(panelCentral, "Ventas"));
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(68, 71, 90));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
