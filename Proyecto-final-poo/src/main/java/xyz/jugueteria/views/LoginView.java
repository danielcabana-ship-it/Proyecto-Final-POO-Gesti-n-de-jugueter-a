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

        // Aquí es donde validamos el usuario contra la base de datos.
        btnLogin.addActionListener(e -> {
            String usr = txtUsuario.getText().trim();
            String pwd = new String(txtPassword.getPassword());
            
            if (usr.isEmpty() || pwd.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingresa tu usuario/correo y contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            xyz.jugueteria.dao.UsuarioDAO usuarioDAO = new xyz.jugueteria.dao.UsuarioDAO();
            xyz.jugueteria.models.Usuario usuario = usuarioDAO.login(usr, pwd);

            if (usuario != null) {
                xyz.jugueteria.database.Session.setUsuarioLogueado(usuario);
                MainView mainView = new MainView();
                mainView.setVisible(true);
                this.dispose(); // Cerramos el login
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Evento secundario para abrir el registro de nuevo usuario
        btnRegistro.addActionListener(e -> {
            mostrarDialogoRegistro();
        });
    }

    // Despliega una tarjeta flotante estilizada en un diálogo modal para crear un nuevo usuario
    private void mostrarDialogoRegistro() {
        JDialog dlg = new JDialog(this, "Registrar Nuevo Usuario", true);
        dlg.setSize(450, 520);
        dlg.setLocationRelativeTo(this);
        
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(new Color(30, 32, 40)); // Fondo oscuro asfalto
        pnl.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Título del formulario
        JLabel lblTitle = new JLabel("Crear Cuenta");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        pnl.add(lblTitle, gbc);
        
        gbc.gridwidth = 1;
        
        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        pnl.add(crearLabelRegistro("Nombre Completo:"), gbc);
        JTextField txtNombre = crearTextFieldRegistro();
        gbc.gridx = 1;
        pnl.add(txtNombre, gbc);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 2;
        pnl.add(crearLabelRegistro("Nombre de Usuario:"), gbc);
        JTextField txtRegUsuario = crearTextFieldRegistro();
        gbc.gridx = 1;
        pnl.add(txtRegUsuario, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        pnl.add(crearLabelRegistro("Correo Electrónico:"), gbc);
        JTextField txtEmail = crearTextFieldRegistro();
        gbc.gridx = 1;
        pnl.add(txtEmail, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 4;
        pnl.add(crearLabelRegistro("Contraseña:"), gbc);
        JPasswordField txtRegPassword = crearPasswordFieldRegistro();
        gbc.gridx = 1;
        pnl.add(txtRegPassword, gbc);
        
        // Rol
        gbc.gridx = 0; gbc.gridy = 5;
        pnl.add(crearLabelRegistro("Rol de Usuario:"), gbc);
        String[] roles = {"vendedor", "admin", "auditor"};
        JComboBox<String> cbRol = new JComboBox<>(roles);
        cbRol.setPreferredSize(new Dimension(200, 35));
        cbRol.setBackground(new Color(40, 42, 54));
        cbRol.setForeground(Color.WHITE);
        gbc.gridx = 1;
        pnl.add(cbRol, gbc);
        
        // Botones de acción
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBotones.setOpaque(false);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(70, 70, 70));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.addActionListener(e -> dlg.dispose());
        
        JButton btnGuardar = new JButton("Registrar");
        btnGuardar.setBackground(new Color(0, 122, 255)); // Azul
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(120, 35));
        
        pnlBotones.add(btnCancelar);
        pnlBotones.add(btnGuardar);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        pnl.add(pnlBotones, gbc);
        
        // Lógica de registro
        btnGuardar.addActionListener(ev -> {
            String nombre = txtNombre.getText().trim();
            String username = txtRegUsuario.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtRegPassword.getPassword()).trim();
            String rol = (String) cbRol.getSelectedItem();
            
            if (nombre.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Por favor, completa todos los campos.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            xyz.jugueteria.dao.UsuarioDAO usuarioDAO = new xyz.jugueteria.dao.UsuarioDAO();
            
            if (usuarioDAO.usernameExiste(username)) {
                JOptionPane.showMessageDialog(dlg, "El nombre de usuario ya está registrado.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (usuarioDAO.emailExiste(email)) {
                JOptionPane.showMessageDialog(dlg, "El correo electrónico ya está registrado.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            xyz.jugueteria.models.Usuario nuevoUsuario = new xyz.jugueteria.models.Usuario(0, nombre, username, email, password, rol);
            if (usuarioDAO.registrarUsuario(nuevoUsuario)) {
                JOptionPane.showMessageDialog(this, "¡Usuario creado correctamente! Ya puedes iniciar sesión.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose();
            } else {
                JOptionPane.showMessageDialog(dlg, "Error al crear el usuario. Inténtalo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dlg.add(pnl);
        dlg.setVisible(true);
    }
    
    private JLabel crearLabelRegistro(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(new Color(200, 200, 200));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
    }
    
    private JTextField crearTextFieldRegistro() {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBackground(new Color(40, 42, 54));
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);
        txt.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return txt;
    }
    
    private JPasswordField crearPasswordFieldRegistro() {
        JPasswordField txt = new JPasswordField();
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBackground(new Color(40, 42, 54));
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);
        txt.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return txt;
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
