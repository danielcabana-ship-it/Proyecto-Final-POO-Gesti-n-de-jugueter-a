package xyz.jugueteria.views; // Especifica que esta clase pertenece al paquete de las interfaces gráficas (vistas)

import javax.swing.*; // Importa los elementos gráficos base de Swing para la construcción del formulario
import java.awt.*; // Importa las clases de AWT para gestionar disposiciones físicas, colores, fuentes y cursores
import xyz.jugueteria.database.Session; // Importa la clase de sesión global para comprobar los datos del empleado activo
import xyz.jugueteria.models.Usuario; // Importa el modelo Usuario para poder recuperar sus datos e imprimirlos en la barra

/**
 * La ventana principal de nuestra juguetería.
 * Aquí es donde ocurre la magia de la gestión.
 */
public class MainView extends JFrame { // Declara la clase MainView extendiendo de JFrame para que sea la ventana madre del sistema

    private JPanel panelCentral; // Declara el panel contenedor central que resguardará los módulos internos
    private CardLayout cardLayout; // Declara el gestor de distribución tipo "Mazo de cartas" para alternar paneles

    public MainView() { // Constructor de la clase: configura y dibuja todo el Dashboard al hacer 'new MainView()'
        setTitle("Sistema de Gestión de Juguetería Premium"); // Configura el enunciado o texto que saldrá en la barra superior
        setSize(1000, 700); // Establece el tamaño inicial fijo del Dashboard: 1000px de anchura por 700px de altura
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Indica que al pulsar la 'X', la máquina detenga todo el sistema
        setLocationRelativeTo(null); // Centra la ventana de forma exacta en el monitor de la computadora
        setLayout(new BorderLayout()); // Declara el diseño de bordes o zonas geográficas (Norte, Sur, Este, Oeste, Centro)

        // --- Panel Lateral (Sidebar Claro) ---
        // Se creó este panel de color claro para que la navegación sea más amigable,
        // contrastando con el fondo oscuro del contenido principal.
        JPanel sidebar = new JPanel(); // Crea el panel lateral destinado al menú de navegación de la juguetería
        sidebar.setLayout(new GridLayout(7, 1, 10, 10)); // Distribuye el menú en una rejilla rígida de 7 filas y 1 columna, con separación interna de 10px
        sidebar.setPreferredSize(new Dimension(220, getHeight())); // Modifica la anchura del sidebar a un bloque estático de 220px
        sidebar.setBackground(Color.WHITE); // Pinta el fondo del panel lateral de un color blanco nieve puro
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15)); // Agrega márgenes internos invisibles (arriba, izquierda, abajo, derecha)

        JLabel lblTitle = new JLabel("<html><h2 style='color:#1E1E1E;text-align:center;'>Toy Store</h2></html>"); // Crea el logo textual usando formato HTML interno
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER); // Centra la etiqueta horizontalmente en su sector
        sidebar.add(lblTitle); // Inserta el título dentro de la primera fila disponible de la rejilla

        // Mostrar usuario logueado en la barra lateral
        JLabel lblUser = new JLabel(); // Crea la etiqueta destinada a notificar las credenciales del trabajador activo
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Configura una letra tipo Segoe UI regular, tamaño 12
        lblUser.setForeground(new Color(80, 80, 80)); // Pinta la tipografía con un tono gris oscuro discreto
        lblUser.setHorizontalAlignment(SwingConstants.CENTER); // Centra el texto del perfil en su recuadro
        if (Session.isLogueado()) { // Comprueba si existe un perfil de usuario guardado en la sesión activa del software
            Usuario u = Session.getUsuarioLogueado(); // Extrae los datos enteros de la cuenta que ingresó desde el Login
            lblUser.setText("<html><center>👤 " + u.getNombre() + "<br><small style='color:#888888;'>(" + u.getRol() + ")</small></center></html>"); // Renderiza el nombre y el nivel de acceso (rol) de forma estructurada en HTML
        } else { // Si no se encuentra información alguna de autenticación por algún error
            lblUser.setText("<html><center>👤 Invitado</center></html>"); // Declara el perfil por defecto en modo Invitado
        } // Cierre del bloque de evaluación de sesión del empleado
        sidebar.add(lblUser); // Inserta la etiqueta del usuario en la segunda fila disponible del menú

        JButton btnProductos = createMenuButton("📦 Productos"); // Fabrica el botón del almacén mediante el método auxiliar inferior
        JButton btnClientes = createMenuButton("👥 Clientes"); // Fabrica el botón de clientes mediante la misma función de ayuda
        JButton btnVentas = createMenuButton("🛒 Ventas"); // Fabrica el botón de transacciones y caja registradora

        // Un botón para salir y volver al login
        JButton btnSalir = new JButton("🚪 Cerrar Sesión"); // Crea de manera directa el botón destinado a desconectarse
        btnSalir.setFocusPainted(false); // Quita el cuadro punteado feo de foco nativo de Java
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Aplica letra moderna y en negrita resaltada
        btnSalir.setBackground(new Color(255, 230, 230)); // Pinta un fondo de botón de color rojizo sumamente suave e intuitivo
        btnSalir.setForeground(new Color(220, 50, 50)); // Define el color de las letras en un tono rojo de advertencia
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Modifica el puntero a una manita interactiva al pasar por encima
        btnSalir.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Le da un relleno cómodo a los bordes internos de la celda

        sidebar.add(btnProductos); // Coloca el botón de Productos en la tercera fila de la cuadrícula
        sidebar.add(btnClientes); // Coloca el botón de Clientes en la cuarta fila de la cuadrícula
        sidebar.add(btnVentas); // Coloca el botón de Ventas en la quinta fila de la cuadrícula

        // Empujamos el botón de salir hacia abajo
        sidebar.add(new JLabel("")); // Crea una etiqueta vacía e invisible que actúa de amortiguador para rellenar la fila seis
        sidebar.add(btnSalir); // Posiciona el botón de salida al fondo de la rejilla, ocupando el último espacio (fila siete)

        add(sidebar, BorderLayout.WEST); // Ancla la barra lateral completa en la franja izquierda (Oeste) de la pantalla principal

        // --- Panel Central (Contenedor de Vistas Oscuro) ---
        cardLayout = new CardLayout(); // Inicializa el CardLayout, que apilará las pantallas como si fueran cartas en un mazo
        panelCentral = new JPanel(cardLayout); // Asigna esta estrategia de distribución al panel central
        panelCentral.setBackground(new Color(30, 32, 40)); // Pinta el fondo del contenedor principal con un gris oscuro asfalto

        // Añadimos las vistas a nuestro gestor de tarjetas (cardLayout)
        panelCentral.add(new ProductosView(), "Productos"); // Añade y etiqueta la pantalla del catálogo de juguetes como "Productos"
        panelCentral.add(new ClientesView(), "Clientes"); // Añade y etiqueta la pantalla del directorio como "Clientes"
        panelCentral.add(new VentasView(), "Ventas"); // Añade y etiqueta la pantalla del punto de venta como "Ventas"

        add(panelCentral, BorderLayout.CENTER); // Posiciona el panel central al medio, absorbiendo todo el espacio amplio sobrante de la pantalla

        // --- Eventos de Navegación ---
        // Aquí cambiamos de pantalla al instante sin tener que abrir mil ventanas
        btnProductos.addActionListener(e -> cardLayout.show(panelCentral, "Productos")); // Al hacer clic, ordena al CardLayout revelar instantáneamente la tarjeta de Productos
        btnClientes.addActionListener(e -> cardLayout.show(panelCentral, "Clientes")); // Al hacer clic, ordena al CardLayout traer al frente la tarjeta de Clientes
        btnVentas.addActionListener(e -> cardLayout.show(panelCentral, "Ventas")); // Al hacer clic, ordena al CardLayout desplegar la tarjeta de Ventas

        btnSalir.addActionListener(e -> { // Monitorea e interactúa inmediatamente ante el clic en "Cerrar Sesión"
            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres salir?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION); // Lanza un diálogo de confirmación con opciones Sí y No
            if(confirm == JOptionPane.YES_OPTION) { // Evalúa si el usuario seleccionó la opción positiva (Sí)
                Session.logout(); // Vacia la sesión del sistema borrando los datos del empleado en memoria
                new LoginView().setVisible(true); // Abre y despliega una nueva ventana de Login limpia en pantalla
                this.dispose(); // Destruye y desmantela el Dashboard actual liberando el consumo de memoria RAM
            } // Cierre del bloque IF de confirmación afirmativa
        }); // Cierre del evento del botón Cerrar Sesión
    } // Cierre del constructor de MainView

    /**
     * Helper para crear botones de menú con estilo moderno y claro.
     */
    private JButton createMenuButton(String text) { // Método auxiliar reutilizable que automatiza la creación de los botones de navegación
        JButton btn = new JButton(text); // Instancia el botón agregando el texto y el emoji correspondiente provisto por parámetro
        btn.setFocusPainted(false); // Quita el recuadro interno de selección nativo de Java
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15)); // Configura tipografía en negrita destacada con un tamaño estilizado de 15
        btn.setBackground(new Color(245, 245, 250)); // Pinta un tono gris sumamente suave y limpio en el fondo del botón
        btn.setForeground(new Color(40, 40, 40)); // Pinta el texto en un gris carbón oscuro de alta visibilidad
        btn.setHorizontalAlignment(SwingConstants.LEFT); // Alinea el texto del botón hacia la izquierda para un orden tipo lista
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Crea márgenes internos de confortación para expandir la superficie pulsable
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Activa el cursor de mano interactiva al sobrevolar el elemento gráfico
        return btn; // Devuelve el botón completamente diseñado y listo para agregarse al panel lateral
    } // Cierre de la función constructora createMenuButton
} // Cierre definitivo del archivo y la clase MainView