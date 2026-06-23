package xyz.jugueteria.views;

import javax.swing.*;
import java.awt.*;

/**
 * ¡Bienvenido a la puerta de entrada!
 * Esta es la nueva y moderna pantalla de Login.
 * Usamos un diseño de pantalla dividida (split-screen): claro a la izquierda, oscuro a la derecha.
 */
public class LoginView extends JFrame {

    public LoginView() {
        // Configuramos la ventana principal para que se vea moderna y sin bordes feos
        setTitle("Toy Store - Iniciar Sesión");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- LADO IZQUIERDO: Panel Claro (Formulario) ---
        // Se creó este panel de color claro para que la navegación sea más amigable.
        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setPreferredSize(new Dimension(450, 600));

        // Creamos una "tarjeta" flotante invisible donde irán los controles
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setOpaque(false); // Fondo transparente para que se vea el blanco del panel

        // Título de bienvenida
        JLabel lblTitulo = new JLabel("Bienvenido de nuevo");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(30, 30, 30));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitulo = new JLabel("Por favor, ingresa tus credenciales");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(100, 100, 100));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campos de texto
        JTextField txtUsuario = new JTextField();
        txtUsuario.setMaximumSize(new Dimension(300, 40));
        txtUsuario.setPreferredSize(new Dimension(300, 40));
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBackground(new Color(245, 245, 245)); // Gris muy claro
        txtUsuario.setForeground(Color.BLACK);
        txtUsuario.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(300, 40));
        txtPassword.setPreferredSize(new Dimension(300, 40));
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBackground(new Color(245, 245, 245));
        txtPassword.setForeground(Color.BLACK);
        txtPassword.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Agregamos este botón azul vibrante para que sepan dónde dar clic para entrar.
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setMaximumSize(new Dimension(300, 45));
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(0, 122, 255)); // Azul vibrante
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnRegistro = new JButton("Crear Usuario");
        btnRegistro.setMaximumSize(new Dimension(300, 45));
        btnRegistro.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistro.setBackground(new Color(230, 230, 230)); // Gris claro secundario
        btnRegistro.setForeground(new Color(50, 50, 50));
        btnRegistro.setFocusPainted(false);
        btnRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Armamos la tarjeta con espaciados
        tarjeta.add(Box.createVerticalStrut(20));
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(lblSubtitulo);
        tarjeta.add(Box.createVerticalStrut(40));
        tarjeta.add(crearLabelConEstilo("Usuario"));
        tarjeta.add(txtUsuario);
        tarjeta.add(Box.createVerticalStrut(20));
        tarjeta.add(crearLabelConEstilo("Contraseña"));
        tarjeta.add(txtPassword);
        tarjeta.add(Box.createVerticalStrut(40));
        tarjeta.add(btnLogin);
        tarjeta.add(Box.createVerticalStrut(15));
        tarjeta.add(btnRegistro);

        panelIzquierdo.add(tarjeta);

        // --- LADO DERECHO: Panel Oscuro (Decorativo) ---
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBackground(new Color(30, 32, 40)); // Gris oscuro asfalto

        JLabel lblLogo = new JLabel("<html><center><h1 style='color:#007AFF; font-size: 60px;'>Toy<br>Store</h1><p style='color:#A0A0A0; font-size: 16px;'>Premium Edition</p></center></html>");
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        panelDerecho.add(lblLogo, BorderLayout.CENTER);

        // Agregamos ambos lados a la ventana
        add(panelIzquierdo, BorderLayout.WEST);
        add(panelDerecho, BorderLayout.CENTER);

        // Aquí es donde validamos el usuario. ¡No toques esta parte sin revisar la tabla!
        // Para este prompt, es una validación simple.
        btnLogin.addActionListener(e -> {
            String usr = txtUsuario.getText();
            String pwd = new String(txtPassword.getPassword());
            
            // Validación básica para poder probar
            if (!usr.isEmpty() && !pwd.isEmpty()) {
                // Login exitoso, abrimos la pantalla principal
                MainView mainView = new MainView();
                mainView.setVisible(true);
                this.dispose(); // Cerramos el login
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, ingresa cualquier usuario y contraseña para entrar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Evento secundario
        btnRegistro.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Función de registro próximamente.", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    // Pequeño método de ayuda para que el texto de las etiquetas se vea bonito y alineado
    private JLabel crearLabelConEstilo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(100, 100, 100));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setMaximumSize(new Dimension(300, 20));
        return label;
    }
}
