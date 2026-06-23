package xyz.jugueteria.views;

import javax.swing.*;
import java.awt.*;

/**
 * La ventana principal de nuestra juguetería.
 * Aquí es donde ocurre la magia de la gestión.
 */
public class MainView extends JFrame {

    private JPanel panelCentral;
    private CardLayout cardLayout;

    public MainView() {
        setTitle("Sistema de Gestión de Juguetería Premium");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Panel Lateral (Sidebar Claro) ---
        // Se creó este panel de color claro para que la navegación sea más amigable,
        // contrastando con el fondo oscuro del contenido principal.
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(7, 1, 10, 10)); // 7 filas para dejar espacio arriba y abajo
        sidebar.setPreferredSize(new Dimension(220, getHeight()));
        sidebar.setBackground(Color.WHITE); // Fondo nieve
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));

        JLabel lblTitle = new JLabel("<html><h2 style='color:#1E1E1E;text-align:center;'>Toy Store</h2></html>");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(lblTitle);

        JButton btnProductos = createMenuButton("📦 Productos");
        JButton btnClientes = createMenuButton("👥 Clientes");
        JButton btnVentas = createMenuButton("🛒 Ventas");
        
        // Un botón para salir y volver al login
        JButton btnSalir = new JButton("🚪 Cerrar Sesión");
        btnSalir.setFocusPainted(false);
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalir.setBackground(new Color(255, 230, 230)); // Rojizo muy clarito
        btnSalir.setForeground(new Color(220, 50, 50));
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        sidebar.add(btnProductos);
        sidebar.add(btnClientes);
        sidebar.add(btnVentas);
        
        // Empujamos el botón de salir hacia abajo
        sidebar.add(new JLabel("")); // Espaciador invisible
        sidebar.add(btnSalir);

        add(sidebar, BorderLayout.WEST);

        // --- Panel Central (Contenedor de Vistas Oscuro) ---
        cardLayout = new CardLayout();
        panelCentral = new JPanel(cardLayout);
        panelCentral.setBackground(new Color(30, 32, 40)); // Fondo oscuro principal

        // Añadimos las vistas a nuestro gestor de tarjetas (cardLayout)
        panelCentral.add(new ProductosView(), "Productos");
        panelCentral.add(new ClientesView(), "Clientes");
        panelCentral.add(new VentasView(), "Ventas");

        add(panelCentral, BorderLayout.CENTER);

        // --- Eventos de Navegación ---
        // Aquí cambiamos de pantalla al instante sin tener que abrir mil ventanas
        btnProductos.addActionListener(e -> cardLayout.show(panelCentral, "Productos"));
        btnClientes.addActionListener(e -> cardLayout.show(panelCentral, "Clientes"));
        btnVentas.addActionListener(e -> cardLayout.show(panelCentral, "Ventas"));
        
        btnSalir.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres salir?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                new LoginView().setVisible(true);
                this.dispose();
            }
        });
    }

    /**
     * Helper para crear botones de menú con estilo moderno y claro.
     */
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(new Color(245, 245, 250)); // Un gris súper clarito para los botones
        btn.setForeground(new Color(40, 40, 40)); // Texto oscuro
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
