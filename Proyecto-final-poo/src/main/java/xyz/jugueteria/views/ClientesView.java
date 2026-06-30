package xyz.jugueteria.views;

// Importamos el DAO de clientes para poder guardar, listar y borrar datos de la BD
import xyz.jugueteria.dao.ClienteDAO;
// Importamos el modelo Cliente para manejar la información que se escribe en la interfaz
import xyz.jugueteria.models.Cliente;

// Importamos las librerías de Swing y AWT para construir las ventanas, tablas, botones y colores
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

    // Declaramos las herramientas de datos y de la tabla
    private ClienteDAO clienteDAO;          // El intermediario con la base de datos
    private JTable table;                   // El componente visual de la tabla
    private DefaultTableModel tableModel;   // El objeto que controla las filas y columnas de la tabla

    // Declaramos los cuadros de texto donde el usuario escribirá los datos
    private JTextField txtDni, txtNombre, txtTelefono, txtDireccion;

    // Constructor: Aquí se dibuja toda la pantalla y se configuran los botones
    public ClientesView() {
        clienteDAO = new ClienteDAO(); // Inicializamos el acceso a datos de clientes
        setLayout(new BorderLayout(15, 15)); // Organizamos la pantalla en regiones (Norte, Centro, Sur) con espacios de 15 píxeles

        // Fondo asfalto para mantener consistencia con el diseño oscuro
        setBackground(new Color(30, 32, 40));
        // Le damos un colchón o margen interno de 25 píxeles a toda la ventana para que no esté pegada a los bordes
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Título de la sección
        JLabel lblTitle = new JLabel("Directorio de Clientes");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Fuente grande y en negrita
        lblTitle.setForeground(Color.WHITE); // Texto blanco brillante
        add(lblTitle, BorderLayout.NORTH); // Lo colocamos en la parte superior (Norte)

        // --- Tabla de Clientes ---
        // Definimos los nombres de las columnas que verá el usuario
        tableModel = new DefaultTableModel(new String[]{"ID", "DNI", "Nombre", "Teléfono", "Dirección"}, 0);
        table = new JTable(tableModel); // Le asignamos el modelo a la JTable
        table.setRowHeight(35); // Hacemos las filas altas y cómodas de leer (35 píxeles)
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Tipografía moderna para los datos
        table.setBackground(new Color(40, 42, 54)); // Fondo oscuro para las celdas
        table.setForeground(Color.WHITE); // Texto blanco para los datos
        table.setGridColor(new Color(60, 62, 74)); // Color gris sutil para las líneas de la tabla
        table.setShowVerticalLines(false); // Ocultamos las líneas verticales para un look más limpio y minimalista

        // Cabecera estilizada de la tabla
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 122, 255)); // El azul distintivo de la app para los títulos de columnas
        header.setForeground(Color.WHITE); // Texto blanco
        header.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Letra destacada en negrita

        // Creamos un alineador para centrar los textos dentro de todas las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER); // Centramos horizontalmente
        table.setDefaultRenderer(Object.class, centerRenderer); // Lo aplicamos a cualquier tipo de dato en la tabla

        // Metemos la tabla dentro de un panel con barras de desplazamiento por si la lista es muy larga
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(30, 32, 40)); // Fondo a juego con el resto de la app
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Quitamos los bordes por defecto del JScrollPane
        add(scrollPane, BorderLayout.CENTER); // Ponemos la tabla en el corazón de la pantalla (Centro)

        // --- Formulario de Entrada ---
        // Contenedor principal de la parte inferior de la pantalla
        JPanel panelBottom = new JPanel(new BorderLayout(15, 15));
        panelBottom.setBackground(new Color(30, 32, 40));

        // Cuadrícula de 2 filas y 4 columnas para organizar etiquetas y cajas de texto de forma alineada
        JPanel panelForm = new JPanel(new GridLayout(2, 4, 10, 10));
        panelForm.setBackground(new Color(30, 32, 40));

        // Creamos las cajas de texto oscuras usando nuestra función personalizada de abajo
        txtDni = crearTextFieldOscuro();
        txtNombre = crearTextFieldOscuro();
        txtTelefono = crearTextFieldOscuro();
        txtDireccion = crearTextFieldOscuro();

        // Fila 1 del formulario: Agregamos todas las etiquetas blancas de texto
        panelForm.add(crearLabelBlanco("DNI:"));
        panelForm.add(crearLabelBlanco("Nombre Completo:"));
        panelForm.add(crearLabelBlanco("Teléfono:"));
        panelForm.add(crearLabelBlanco("Dirección:"));

        // Fila 2 del formulario: Agregamos las cajas de texto justo debajo de sus respectivas etiquetas
        panelForm.add(txtDni);
        panelForm.add(txtNombre);
        panelForm.add(txtTelefono);
        panelForm.add(txtDireccion);

        // Botones de acción
        // Alineamos los botones a la derecha con un espacio horizontal de 15 píxeles entre ellos
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelBotones.setBackground(new Color(30, 32, 40));

        // Creamos los botones usando nuestros diseños estilizados de abajo
        JButton btnRefrescar = crearBotonPrimario("Actualizar Lista");
        JButton btnEliminar = crearBotonPeligro("Borrar Seleccionado");
        JButton btnAgregar = crearBotonPrimario("Guardar Cliente");

        // Añadimos los botones al contenedor de botones en orden
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnAgregar);

        // Ensamblamos el formulario y los botones en el panel inferior
        panelBottom.add(panelForm, BorderLayout.CENTER); // El formulario va en medio
        panelBottom.add(panelBotones, BorderLayout.SOUTH); // Los botones van abajo

        // Añadimos todo el bloque inferior a la región Sur de la pantalla principal
        add(panelBottom, BorderLayout.SOUTH);

        // --- Eventos ---

        // Acción del botón Actualizar Lista: Limpia la tabla y vuelve a leer la base de datos
        btnRefrescar.addActionListener(e -> cargarDatos());

        // Acción del botón Guardar Cliente
        btnAgregar.addActionListener(e -> {
            try {
                // Creamos un nuevo objeto cliente y lo llenamos con lo que escribió el usuario en las cajas de texto
                Cliente c = new Cliente();
                c.setDni(txtDni.getText());
                c.setNombreCompleto(txtNombre.getText());
                c.setTelefono(txtTelefono.getText());
                c.setDireccion(txtDireccion.getText());

                // Intentamos registrarlo a través del DAO
                if (clienteDAO.registrarCliente(c)) {
                    // Si la BD acepta el registro, mostramos un mensaje amigable de éxito
                    JOptionPane.showMessageDialog(this, "Cliente registrado con éxito.");
                    cargarDatos();  // Recargamos la tabla para ver al nuevo cliente al instante
                    limpiarForm();  // Vaciamos las cajas de texto para un nuevo registro
                } else {
                    // Si el DAO devuelve false (ej. DNI duplicado), mostramos una alerta de error
                    JOptionPane.showMessageDialog(this, "Error al registrar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                // Captura fallos inesperados de conversión o de lógica
                JOptionPane.showMessageDialog(this, "Por favor revise los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Acción del botón Borrar Seleccionado
        btnEliminar.addActionListener(e -> {
            // Averiguamos qué fila de la tabla tiene seleccionada el usuario (-1 significa ninguna)
            int row = table.getSelectedRow();
            if (row != -1) {
                // Recuperamos el valor de la columna 0 (que guarda el ID oculto del cliente) de la fila elegida
                int id = (int) tableModel.getValueAt(row, 0);

                // Le pedimos al DAO que intente borrar ese ID de la base de datos
                if (clienteDAO.eliminarCliente(id)) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado.");
                    cargarDatos(); // Actualizamos la vista de la tabla
                } else {
                    // Si el servidor SQL bloquea el borrado (por ejemplo, si ese cliente ya tiene compras hechas)
                    JOptionPane.showMessageDialog(this, "Error al eliminar (Puede que tenga ventas asociadas).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Alerta en caso de hacer clic en borrar sin haber seleccionado a nadie primero
                JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla.");
            }
        });

        // Al iniciar la pantalla por primera vez, cargamos automáticamente la lista de clientes
        cargarDatos();
    }

    // Método que limpia la tabla y descarga los registros actualizados de la base de datos
    private void cargarDatos() {
        tableModel.setRowCount(0); // Borra todas las filas existentes en la tabla visual
        List<Cliente> clientes = clienteDAO.listarClientes(); // Trae la lista de clientes desde el DAO
        for (Cliente c : clientes) {
            // Agrega una fila nueva a la tabla con los datos del cliente procesado
            tableModel.addRow(new Object[]{c.getIdCliente(), c.getDni(), c.getNombreCompleto(), c.getTelefono(), c.getDireccion()});
        }
    }

    // Método rápido para vaciar todos los campos de texto del formulario
    private void limpiarForm() {
        txtDni.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
    }

    // --- Utilidades visuales reutilizadas (Fábricas de diseño personalizado) ---

    // Crea etiquetas con fuente compacta y un color gris claro elegante
    private JLabel crearLabelBlanco(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(new Color(200, 200, 200));
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return lbl;
    }

    // Crea campos de texto oscuros, letras blancas, cursor blanco y márgenes cómodos para escribir
    private JTextField crearTextFieldOscuro() {
        JTextField txt = new JTextField();
        txt.setBackground(new Color(40, 42, 54)); // Fondo gris oscuro
        txt.setForeground(Color.WHITE);          // Letra blanca al escribir
        txt.setCaretColor(Color.WHITE);          // El cursor parpadeante es blanco
        // Le damos un acolchado interno (arriba, izquierda, abajo, derecha) para que el texto no esté pegado al borde físico
        txt.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return txt;
    }

    // Crea los botones estándar de la aplicación con color azul eléctrico y cursor de manita
    private JButton crearBotonPrimario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(0, 122, 255)); // Azul moderno
        btn.setForeground(Color.WHITE);            // Letras blancas
        btn.setFocusPainted(false);                // Elimina el cuadro punteado feo al hacer clic
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // El cursor cambia a una mano interactiva al pasar por encima
        return btn;
    }

    // Crea los botones de alertas o borrado con color rojo/coral llamativo
    private JButton crearBotonPeligro(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(255, 85, 85)); // Rojo suave tipo Flat
        btn.setForeground(Color.WHITE);            // Letras blancas
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}