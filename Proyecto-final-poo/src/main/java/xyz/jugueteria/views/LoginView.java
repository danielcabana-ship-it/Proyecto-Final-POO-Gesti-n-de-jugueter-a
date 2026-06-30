package xyz.jugueteria.views;

// Importamos el DAO de usuarios para validar el inicio de sesión y registrar nuevas cuentas
import xyz.jugueteria.dao.UsuarioDAO;
// Importamos el modelo Usuario para empaquetar y estructurar la información de las cuentas
import xyz.jugueteria.models.Usuario;

// Importamos las librerías gráficas de Swing y AWT para construir los paneles, entradas, textos y eventos
import javax.swing.*;
import java.awt.*;

/**
 * ¡Bienvenido a la puerta de entrada!
 * Esta es la nueva y moderna pantalla de Login.
 * Usamos un diseño de pantalla dividida (split-screen): claro a la izquierda, oscuro a la derecha.
 */
public class LoginView extends JFrame {

    // Constructor: Aquí levantamos la ventana y creamos los dos mundos (izquierdo claro, derecho oscuro)
    public LoginView() {
        // Configuramos la ventana principal para que se vea moderna y sin bordes feos
        setTitle("Toy Store - Iniciar Sesión"); // El título de la ventana
        setSize(900, 600);                     // Dimensiones de la pantalla (900 de ancho por 600 de alto)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Si el usuario cierra esta ventana, el programa se apaga por completo
        setLocationRelativeTo(null);            // Centra la ventana automáticamente en medio de la pantalla del monitor
        setLayout(new BorderLayout());          // Usamos el esquema de regiones (Norte, Sur, Este, Oeste, Centro)

        // --- LADO IZQUIERDO: Panel Claro (Formulario) ---
        // Se creó este panel de color claro para que la navegación sea más amigable.
        // GridBagLayout es genial porque nos permite centrar componentes de forma perfecta tanto vertical como horizontalmente
        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        panelIzquierdo.setBackground(Color.WHITE); // Fondo blanco puro
        panelIzquierdo.setPreferredSize(new Dimension(450, 600)); // Ocupará exactamente la mitad del ancho de la ventana

        // Creamos una "tarjeta" flotante invisible donde irán los controles
        // BoxLayout acomoda todo en una sola columna, de arriba hacia abajo (Y_AXIS)
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setOpaque(false); // Fondo transparente para que se vea el blanco del panel de atrás

        // Título de bienvenida principal
        JLabel lblTitulo = new JLabel("Bienvenido de nuevo");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Letra grande y llamativa
        lblTitulo.setForeground(new Color(30, 30, 30)); // Gris casi negro
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT); // Lo alineamos al centro de la tarjeta

        // Subtítulo secundario
        JLabel lblSubtitulo = new JLabel("Por favor, ingresa tus credenciales");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Letra estándar y delgada
        lblSubtitulo.setForeground(new Color(100, 100, 100)); // Gris suave
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campos de texto para el Usuario
        JTextField txtUsuario = new JTextField();
        txtUsuario.setMaximumSize(new Dimension(300, 40)); // Fijamos el tamaño máximo para que no se deforme
        txtUsuario.setPreferredSize(new Dimension(300, 40)); // El tamaño ideal en pantalla
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.setBackground(new Color(245, 245, 245)); // Gris muy claro tipo minimalista
        txtUsuario.setForeground(Color.BLACK); // Texto negro al escribir
        txtUsuario.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Margen interno para que las letras no choquen con el borde de la caja

        // Campo de texto oculto para la Contraseña
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(300, 40));
        txtPassword.setPreferredSize(new Dimension(300, 40));
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBackground(new Color(245, 245, 245));
        txtPassword.setForeground(Color.BLACK);
        txtPassword.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Agregamos este botón azul vibrante para que sepan dónde dar clic para entrar.
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setMaximumSize(new Dimension(300, 45)); // Botón alto e imponente
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(0, 122, 255)); // Azul eléctrico vibrante
        btnLogin.setForeground(Color.WHITE); // Texto blanco
        btnLogin.setFocusPainted(false); // Quita el borde punteado por defecto
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor de manita interactiva

        // Botón secundario para registrar un nuevo usuario
        JButton btnRegistro = new JButton("Crear Usuario");
        btnRegistro.setMaximumSize(new Dimension(300, 45));
        btnRegistro.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistro.setBackground(new Color(230, 230, 230)); // Gris claro neutro
        btnRegistro.setForeground(new Color(50, 50, 50)); // Texto gris oscuro
        btnRegistro.setFocusPainted(false);
        btnRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Armamos la tarjeta agregando los componentes y separadores invisibles (VerticalStrut)
        tarjeta.add(Box.createVerticalStrut(20)); // Espacio arriba
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(lblSubtitulo);
        tarjeta.add(Box.createVerticalStrut(40)); // Espacio antes de los campos
        tarjeta.add(crearLabelConEstilo("Usuario"));
        tarjeta.add(txtUsuario);
        tarjeta.add(Box.createVerticalStrut(20)); // Espacio entre campos
        tarjeta.add(crearLabelConEstilo("Contraseña"));
        tarjeta.add(txtPassword);
        tarjeta.add(Box.createVerticalStrut(40)); // Espacio antes de los botones
        tarjeta.add(btnLogin);
        tarjeta.add(Box.createVerticalStrut(15)); // Espacio entre botones
        tarjeta.add(btnRegistro);

        // Colocamos la tarjeta ya armada en el centro del panel izquierdo claro
        panelIzquierdo.add(tarjeta);

        // --- LADO DERECHO: Panel Oscuro (Decorativo) ---
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBackground(new Color(30, 32, 40)); // Gris oscuro asfalto para el contraste

        // Diseñamos un logo estilizado usando código HTML directo en el JLabel para darle formato premium
        JLabel lblLogo = new JLabel("<html><center><h1 style='color:#007AFF; font-size: 60px;'>Toy<br>Store</h1><p style='color:#A0A0A0; font-size: 16px;'>Premium Edition</p></center></html>");
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER); // Centramos el logo en el panel derecho
        panelDerecho.add(lblLogo, BorderLayout.CENTER);

        // Agregamos ambos bloques a la ventana principal de login
        add(panelIzquierdo, BorderLayout.WEST);   // El formulario va anclado al Oeste (izquierda)
        add(panelDerecho, BorderLayout.CENTER);   // El diseño decorativo ocupa el resto (Centro/derecha)

        // --- LÓGICA DE ACCIONES (Eventos de los botones) ---

        // Aquí es donde validamos el usuario contra la base de datos al hacer clic en Iniciar Sesión.
        btnLogin.addActionListener(e -> {
            String usr = txtUsuario.getText().trim(); // Obtenemos el texto ingresado quitando espacios vacíos en los extremos
            String pwd = new String(txtPassword.getPassword()); // Obtenemos la contraseña en un String seguro

            // Si alguno de los campos está totalmente en blanco, lanzamos una advertencia y detenemos el login
            if (usr.isEmpty() || pwd.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, ingresa tu usuario/correo y contraseña.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Llamamos al DAO para que busque al usuario en la base de datos usando las credenciales escritas
            xyz.jugueteria.dao.UsuarioDAO usuarioDAO = new xyz.jugueteria.dao.UsuarioDAO();
            xyz.jugueteria.models.Usuario usuario = usuarioDAO.login(usr, pwd);

            // Si el objeto devuelto no es nulo, significa que las credenciales son 100% reales y correctas
            if (usuario != null) {
                // Guardamos el usuario encontrado en la sesión global del programa para recordar quién está trabajando
                xyz.jugueteria.database.Session.setUsuarioLogueado(usuario);

                // Creamos y abrimos la ventana principal del sistema (el panel de control de la juguetería)
                MainView mainView = new MainView();
                mainView.setVisible(true);

                // Cerramos y destruimos la ventana de login para que no consuma memoria en segundo plano
                this.dispose();
            } else {
                // Si el DAO devolvió null, avisamos que los datos de acceso están mal redactados
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Evento secundario para abrir el formulario flotante de registro de nuevo usuario
        btnRegistro.addActionListener(e -> {
            mostrarDialogoRegistro();
        });
    }

    // Despliega una tarjeta flotante estilizada en un diálogo modal para crear un nuevo usuario
    private void mostrarDialogoRegistro() {
        // Creamos una ventana emergente de tipo modal (el 'true' bloquea la ventana de atrás hasta que se cierre esta)
        JDialog dlg = new JDialog(this, "Registrar Nuevo Usuario", true);
        dlg.setSize(450, 520);
        dlg.setLocationRelativeTo(this); // La centramos justo encima de la ventana de login

        // Panel con GridBagLayout para alinear perfectamente las etiquetas y campos del registro
        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(new Color(30, 32, 40)); // Mantenemos el look oscuro asfalto premium
        pnl.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25)); // Margen interno de respiración

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Separación de 8 píxeles entre cada celda de la cuadrícula
        gbc.fill = GridBagConstraints.HORIZONTAL; // Hace que los componentes se expandan horizontalmente si es necesario

        // Título interno del formulario de registro
        JLabel lblTitle = new JLabel("Crear Cuenta");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; // Ocupará toda la primera fila uniendo 2 columnas
        pnl.add(lblTitle, gbc);

        gbc.gridwidth = 1; // Restauramos el tamaño de columnas estándar a 1 para los campos de abajo

        // Campo: Nombre Completo
        gbc.gridx = 0; gbc.gridy = 1; // Columna 0, Fila 1
        pnl.add(crearLabelRegistro("Nombre Completo:"), gbc);
        JTextField txtNombre = crearTextFieldRegistro();
        gbc.gridx = 1; // Columna 1, Fila 1
        pnl.add(txtNombre, gbc);

        // Campo: Username (Alias)
        gbc.gridx = 0; gbc.gridy = 2;
        pnl.add(crearLabelRegistro("Nombre de Usuario:"), gbc);
        JTextField txtRegUsuario = crearTextFieldRegistro();
        gbc.gridx = 1;
        pnl.add(txtRegUsuario, gbc);

        // Campo: Correo Electrónico
        gbc.gridx = 0; gbc.gridy = 3; // Columna 0, Fila 3
        pnl.add(crearLabelRegistro("Correo Electrónico:"), gbc);
        JTextField txtEmail = crearTextFieldRegistro();
        gbc.gridx = 1; // Columna 1, Fila 3
        pnl.add(txtEmail, gbc);

        // Campo: Contraseña
        gbc.gridx = 0; gbc.gridy = 4;
        pnl.add(crearLabelRegistro("Contraseña:"), gbc);
        JPasswordField txtRegPassword = crearPasswordFieldRegistro();
        gbc.gridx = 1;
        pnl.add(txtRegPassword, gbc);

        // Campo: Rol de Usuario (lista desplegable)
        gbc.gridx = 0; gbc.gridy = 5;
        pnl.add(crearLabelRegistro("Rol de Usuario:"), gbc);
        String[] roles = {"vendedor", "admin", "auditor"};
        JComboBox<String> cbRol = new JComboBox<>(roles);
        cbRol.setPreferredSize(new Dimension(200, 35));
        cbRol.setBackground(new Color(40, 42, 54));
        cbRol.setForeground(Color.WHITE);
        gbc.gridx = 1;
        pnl.add(cbRol, gbc);

        // Botones de acción del formulario de registro
        JPanel pnlBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBotones.setOpaque(false); // Fondo transparente para que no tape el diseño oscuro

        // Botón Cancelar: cierra el diálogo sin guardar nada
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(70, 70, 70));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(100, 35));
        btnCancelar.addActionListener(e -> dlg.dispose()); // Al hacer clic, cierra el diálogo

        // Botón Registrar: valida y guarda el nuevo usuario en la base de datos
        JButton btnGuardar = new JButton("Registrar");
        btnGuardar.setBackground(new Color(0, 122, 255)); // Azul vibrante
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGuardar.setPreferredSize(new Dimension(120, 35));

        pnlBotones.add(btnCancelar);
        pnlBotones.add(btnGuardar);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; // Los botones ocupan las dos columnas
        gbc.insets = new Insets(20, 8, 8, 8); // Espacio extra arriba para separar de los campos
        pnl.add(pnlBotones, gbc);

        // Lógica de registro: validar campos y guardar en la BD
        btnGuardar.addActionListener(ev -> {
            String nombre = txtNombre.getText().trim();
            String username = txtRegUsuario.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtRegPassword.getPassword()).trim();
            String rol = (String) cbRol.getSelectedItem();

            // Verificamos que ningún campo esté vacío antes de continuar
            if (nombre.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Por favor, completa todos los campos.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            xyz.jugueteria.dao.UsuarioDAO usuarioDAO = new xyz.jugueteria.dao.UsuarioDAO();

            // Validamos que el nombre de usuario no esté ya registrado
            if (usuarioDAO.usernameExiste(username)) {
                JOptionPane.showMessageDialog(dlg, "El nombre de usuario ya está registrado.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Validamos que el correo no esté ya registrado
            if (usuarioDAO.emailExiste(email)) {
                JOptionPane.showMessageDialog(dlg, "El correo electrónico ya está registrado.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Creamos el objeto Usuario con los datos del formulario y lo registramos
            xyz.jugueteria.models.Usuario nuevoUsuario = new xyz.jugueteria.models.Usuario(0, nombre, username, email, password, rol);
            if (usuarioDAO.registrarUsuario(nuevoUsuario)) {
                JOptionPane.showMessageDialog(this, "¡Usuario creado correctamente! Ya puedes iniciar sesión.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                dlg.dispose(); // Cerramos el diálogo de registro
            } else {
                JOptionPane.showMessageDialog(dlg, "Error al crear el usuario. Inténtalo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dlg.add(pnl); // Agregamos el panel completo al diálogo
        dlg.setVisible(true); // Mostramos el diálogo en pantalla
    }

    // Crea etiquetas con estilo gris claro para el formulario de registro
    private JLabel crearLabelRegistro(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(new Color(200, 200, 200)); // Gris claro elegante
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
    }

    // Crea campos de texto oscuros para el formulario de registro
    private JTextField crearTextFieldRegistro() {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBackground(new Color(40, 42, 54)); // Fondo oscuro
        txt.setForeground(Color.WHITE);            // Letra blanca
        txt.setCaretColor(Color.WHITE);            // Cursor blanco
        txt.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return txt;
    }

    // Crea campos de contraseña oscuros para el formulario de registro
    private JPasswordField crearPasswordFieldRegistro() {
        JPasswordField txt = new JPasswordField();
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBackground(new Color(40, 42, 54));
        txt.setForeground(Color.WHITE);
        txt.setCaretColor(Color.WHITE);
        txt.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return txt;
    }

    // Crea etiquetas estilizadas con texto gris y alineación centrada para el formulario de login
    private JLabel crearLabelConEstilo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(100, 100, 100)); // Gris suave
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setMaximumSize(new Dimension(300, 20));
        return label;
    }
}