package xyz.jugueteria.views; // Especifica el paquete al que pertenece este componente de interfaz gráfica

import xyz.jugueteria.dao.ProductoDAO; // Importa el DAO de productos para interactuar con la base de datos
import xyz.jugueteria.models.Producto; // Importa el modelo Producto para estructurar la información de los juguetes

import javax.swing.*; // Importa los componentes gráficos base de Swing (paneles, tablas, botones, etc.)
import javax.swing.table.DefaultTableCellRenderer; // Importa el renderizador para configurar el estilo de las celdas de la tabla
import javax.swing.table.DefaultTableModel; // Importa el modelo estructural para manipular filas y columnas en la tabla
import javax.swing.table.JTableHeader; // Importa el componente que controla la barra superior de títulos de la tabla
import java.awt.*; // Importa las herramientas de AWT como esquemas de diseño, colores, fuentes y cursores
import java.util.List; // Importa la estructura de colecciones List para almacenar las listas de productos

/**
 * Pantalla de Gestión de Productos.
 * Rediseñada con un fondo oscuro y detalles modernos en azul vibrante.
 */
public class ProductosView extends JPanel { // Declara la clase heredando de JPanel para ser una sección incrustable

    private ProductoDAO productoDAO; // Declara la variable de acceso a datos para hacer consultas de productos
    private JTable table; // Declara el componente visual de la tabla
    private DefaultTableModel tableModel; // Declara el modelo de datos interno que gestionará el contenido de la tabla

    private JTextField txtNombre, txtPrecio, txtStock, txtIdCategoria; // Declara las cajas de texto del formulario
    private JCheckBox chkRequiereBaterias; // Declara la casilla de verificación para el estado de baterías

    public ProductosView() { // Constructor de la clase: configura e inicializa todo el módulo de productos
        productoDAO = new ProductoDAO(); // Instancia el DAO para conectar este panel con las consultas de la BD
        setLayout(new BorderLayout(15, 15)); // Aplica un diseño de regiones con separaciones de 15 píxeles en los ejes horizontal y vertical

        // El fondo oscuro principal (Gris asfalto)
        setBackground(new Color(30, 32, 40)); // Pinta el fondo del panel general con un gris asfalto oscuro
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25)); // Aplica un margen interno de respiración de 25 píxeles en todos sus lados

        // Título estilizado
        JLabel lblTitle = new JLabel("Gestión de Productos"); // Instancia la etiqueta del encabezado principal
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Asigna tipografía moderna de tamaño 28 en negrita
        lblTitle.setForeground(Color.WHITE); // Cambia el color del texto del título a blanco puro
        add(lblTitle, BorderLayout.NORTH); // Posiciona el título en la franja superior (Norte) del panel

        // --- Tabla Moderna ---
        tableModel = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio (S/.)", "Stock", "ID Categoria", "Baterias"}, 0); // Inicializa el modelo definiendo los títulos de las columnas
        table = new JTable(tableModel); // Instancia la tabla pasándole el modelo de datos estructural creado
        table.setRowHeight(35); // Define la altura de cada fila de la tabla en 35 píxeles para comodidad de lectura
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Aplica fuente Segoe UI regular tamaño 14 para los datos
        table.setBackground(new Color(40, 42, 54)); // Configura el fondo de las celdas con un tono gris oscuro integrado
        table.setForeground(Color.WHITE); // Configura el color de los textos internos de las celdas en blanco
        table.setGridColor(new Color(60, 62, 74)); // Cambia las líneas divisorias de la tabla a un gris sutil
        table.setShowVerticalLines(false); // Remueve las líneas verticales divisorias logrando un look limpio y minimalista

        // Cabecera con acento azul
        JTableHeader header = table.getTableHeader(); // Recupera el objeto cabecera de la tabla para personalizarlo
        header.setBackground(new Color(0, 122, 255)); // Pinta los títulos de las columnas con el azul eléctrico característico
        header.setForeground(Color.WHITE); // Cambia las letras de la cabecera a color blanco
        header.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Aplica fuente destacada de tamaño 14 en negrita a los títulos

        // Centrar el contenido de la tabla
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer(); // Instancia el renderizador para alinear el contenido de las celdas
        centerRenderer.setHorizontalAlignment(JLabel.CENTER); // Configura la alineación horizontal de forma centrada
        table.setDefaultRenderer(Object.class, centerRenderer); // Aplica este formateador a todos los tipos de datos en la tabla

        JScrollPane scrollPane = new JScrollPane(table); // Introduce la tabla en un panel con barras de desplazamiento automáticas
        scrollPane.getViewport().setBackground(new Color(30, 32, 40)); // Asegura que el área oculta de fondo mantenga el tono oscuro al deslizar
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Elimina los bordes grisáceos por defecto del JScrollPane
        add(scrollPane, BorderLayout.CENTER); // Ubica la tabla scrollable en la región principal (Centro) del panel

        // --- Panel de Formulario y Botones ---
        JPanel panelBottom = new JPanel(new BorderLayout(15, 15)); // Crea un contenedor inferior que organizará los controles en regiones
        panelBottom.setBackground(new Color(30, 32, 40)); // Pinta su fondo sincronizado al color oscuro asfalto

        JPanel panelForm = new JPanel(new GridLayout(2, 5, 10, 10)); // Crea una rejilla ordenada de 2 filas y 5 columnas, con 10px de separación interna
        panelForm.setBackground(new Color(30, 32, 40)); // Aplica el fondo oscuro al contenedor del formulario

        txtNombre = crearTextFieldOscuro(); // Fabrica la caja de texto para el nombre del producto usando el helper inferior
        txtPrecio = crearTextFieldOscuro(); // Fabrica la caja de texto para el precio unitario del artículo
        txtStock = crearTextFieldOscuro(); // Fabrica la caja de texto para la cantidad en almacén
        txtIdCategoria = crearTextFieldOscuro(); // Fabrica la caja de texto para asociar el código numérico de categoría

        chkRequiereBaterias = new JCheckBox("Req. Baterías"); // Instancia el cuadro de marcación para indicar el uso de baterías
        chkRequiereBaterias.setBackground(new Color(30, 32, 40)); // Pinta el fondo del checkbox a juego con el panel
        chkRequiereBaterias.setForeground(Color.WHITE); // Cambia el texto descriptivo del checkbox a color blanco
        chkRequiereBaterias.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Aplica tipografía estándar de tamaño 14 al checkbox

        // Fila 1 de la cuadrícula: Inserta todas las etiquetas explicativas de los campos
        panelForm.add(crearLabelBlanco("Nombre:")); // Agrega la etiqueta para el nombre
        panelForm.add(crearLabelBlanco("Precio:")); // Agrega la etiqueta para el precio
        panelForm.add(crearLabelBlanco("Stock:")); // Agrega la etiqueta para el stock
        panelForm.add(crearLabelBlanco("ID Categoria:")); // Agrega la etiqueta para el ID de categoría
        panelForm.add(crearLabelBlanco("Extras:")); // Agrega la etiqueta superior para el casillero de extras

        // Fila 2 de la cuadrícula: Inserta los campos de entrada de datos exactamente debajo de su respectiva etiqueta
        panelForm.add(txtNombre); // Inserta el input de nombre
        panelForm.add(txtPrecio); // Inserta el input de precio
        panelForm.add(txtStock); // Inserta el input de stock
        panelForm.add(txtIdCategoria); // Inserta el input del identificador de categoría
        panelForm.add(chkRequiereBaterias); // Inserta el componente checkbox al final de la fila

        // Botones rediseñados
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0)); // Crea un contenedor de botones alineados a la derecha con 15px de espacio horizontal
        panelBotones.setBackground(new Color(30, 32, 40)); // Mantiene la uniformidad de color pintando el fondo de gris asfalto

        JButton btnRefrescar = crearBotonPrimario("Refrescar"); // Fabrica el botón azul para limpiar y recargar datos de la BD
        JButton btnEliminar = crearBotonPeligro("Eliminar"); // Fabrica el botón rojo destinado al borrado de productos
        JButton btnAgregar = crearBotonPrimario("Guardar Nuevo"); // Fabrica el botón azul para añadir registros

        panelBotones.add(btnRefrescar); // Añade el botón de actualización al flujo del panel
        panelBotones.add(btnEliminar); // Añade el botón de borrado en segunda posición
        panelBotones.add(btnAgregar); // Añade el botón de guardado al final de la línea derecha

        panelBottom.add(panelForm, BorderLayout.CENTER); // Monta la cuadrícula del formulario en la zona media del panel inferior
        panelBottom.add(panelBotones, BorderLayout.SOUTH); // Monta la hilera de botones en la zona baja (Sur) de este bloque inferior

        add(panelBottom, BorderLayout.SOUTH); // Inserta el bloque completo del formulario en la región Sur de la pantalla principal

        // --- Eventos ---
        btnRefrescar.addActionListener(e -> cargarDatos()); // Al dar clic en Refrescar, invoca la recarga inmediata de productos desde la base de datos

        btnAgregar.addActionListener(e -> { // Escucha y reacciona de inmediato al presionar el botón "Guardar Nuevo"
            try { // Abre bloque try-catch para interceptar errores de conversión o ingreso de datos inválidos
                Producto p = new Producto(); // Crea una nueva instancia del modelo Producto en blanco
                p.setNombre(txtNombre.getText()); // Extrae el texto del campo de nombre y lo asigna al objeto producto
                p.setPrecio(Double.parseDouble(txtPrecio.getText())); // Convierte el input de precio a número decimal y lo asigna
                p.setStock(Integer.parseInt(txtStock.getText())); // Convierte el input de stock a número entero y lo asigna
                p.setIdCategoria(Integer.parseInt(txtIdCategoria.getText())); // Convierte el input de categoría a entero y lo asigna
                p.setRequiereBaterias(chkRequiereBaterias.isSelected()); // Captura el estado lógico (true/false) del checkbox de baterías

                if (productoDAO.registrarProducto(p)) { // Envía el objeto producto al DAO y evalúa si la inserción en la BD fue exitosa
                    JOptionPane.showMessageDialog(this, "Producto registrado con éxito."); // Lanza mensaje informando del éxito de la operación
                    cargarDatos(); // Actualiza la tabla visual para reflejar el juguete recién guardado
                    limpiarForm(); // Vacía de inmediato todas las cajas de texto para permitir otro ingreso limpio
                } else { // Si el DAO devuelve false debido a un bloqueo o error interno de SQL
                    JOptionPane.showMessageDialog(this, "Error al registrar el producto.", "Error", JOptionPane.ERROR_MESSAGE); // Lanza alerta roja de fallo
                } // Cierre de la condición evaluadora de éxito de guardado
            } catch (Exception ex) { // Captura excepciones si el usuario escribe letras en los campos de precio, stock o categoría
                JOptionPane.showMessageDialog(this, "Por favor revise los datos ingresados.", "Error", JOptionPane.ERROR_MESSAGE); // Lanza un cuadro de advertencia por datos mal redactados
            } // Cierre del bloque de excepciones try-catch
        }); // Cierre del evento del botón de agregado

        btnEliminar.addActionListener(e -> { // Escucha y reacciona ante el clic sobre el botón "Eliminar"
            int row = table.getSelectedRow(); // Consulta qué índice de fila de la tabla tiene seleccionado el usuario (-1 significa que ninguna)
            if (row != -1) { // Evalúa si efectivamente hay una fila activa elegida en la interfaz
                int id = (int) tableModel.getValueAt(row, 0); // Recupera el valor entero de la columna 0 (el ID del producto) de la fila marcada
                if (productoDAO.eliminarProducto(id)) { // Solicita al DAO que elimine ese ID de la BD y verifica el resultado lógico
                    JOptionPane.showMessageDialog(this, "Producto eliminado."); // Avisa de manera amistosa que el producto fue borrado
                    cargarDatos(); // Sincroniza la tabla visual recargando la información del servidor SQL
                } else { // Si la BD deniega el borrado (por ejemplo, si el producto está enlazado a un ticket de venta activo)
                    JOptionPane.showMessageDialog(this, "Error al eliminar.", "Error", JOptionPane.ERROR_MESSAGE); // Lanza una alerta informando del impedimento técnico
                } // Cierre de la evaluación del borrado en el DAO
            } else { // Si el empleado pulsó eliminar sin marcar primero a ningún producto de la lista
                JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla."); // Muestra aviso indicando la instrucción requerida
            } // Cierre de la condición de verificación de selección
        }); // Cierre del evento del botón de eliminación

        cargarDatos(); // Dispara la carga automática de registros al momento en que la pantalla se levanta por primera vez
    } // Cierre del constructor de ProductosView

    private void cargarDatos() { // Declara la función encargada de sincronizar la JTable con la base de datos
        tableModel.setRowCount(0); // Vacía por completo todas las filas visibles que tenga la tabla en ese momento
        List<Producto> productos = productoDAO.listarProductos(); // Trae una colección actualizada de productos desde el DAO
        for (Producto p : productos) { // Recorre uno a uno los productos obtenidos mediante un bucle for-each
            // Inserta una fila nueva en el modelo de la tabla descomponiendo las propiedades del producto actual
            tableModel.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getPrecio(), p.getStock(), p.getIdCategoria(), p.isRequiereBaterias() ? "Sí" : "No"}); // Utiliza un operador ternario para mostrar "Sí" o "No" en vez de true/false
        } // Cierre del ciclo for-each
    } // Cierre del método cargarDatos

    private void limpiarForm() { // Declara la función destinada a vaciar las entradas del formulario
        txtNombre.setText(""); // Borra el texto de la entrada de nombre
        txtPrecio.setText(""); // Borra el texto de la entrada de precio
        txtStock.setText(""); // Borra el texto de la entrada de stock
        txtIdCategoria.setText(""); // Borra el texto de la entrada de categoría
        chkRequiereBaterias.setSelected(false); // Restablece el checkbox desmarcando la casilla de baterías
    } // Cierre del método limpiarForm

    // --- Helpers Visuales (Fábricas de Diseño Reutilizables) ---

    private JLabel crearLabelBlanco(String texto) { // Define una utilidad rápida para estandarizar etiquetas del formulario
        JLabel lbl = new JLabel(texto); // Instancia la etiqueta con el texto enviado por parámetro
        lbl.setForeground(new Color(200, 200, 200)); // Aplica un tono gris claro suave de alta legibilidad sobre fondo oscuro
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Configura fuente compacta de tamaño 12 en negrita destacada
        return lbl; // Devuelve la etiqueta estilizada armada
    } // Cierre de crearLabelBlanco

    private JTextField crearTextFieldOscuro() { // Define una utilidad rápida para estandarizar las entradas de escritura oscuras
        JTextField txt = new JTextField(); // Instancia una caja de entrada de texto tradicional
        txt.setBackground(new Color(40, 42, 54)); // Configura el fondo de la caja en un gris oscuro moderno integrado al diseño
        txt.setForeground(Color.WHITE); // Cambia el color de la fuente tipográfica a blanco para que resalte al escribir
        txt.setCaretColor(Color.WHITE); // Pinta la barra vertical parpadeante de escritura en color blanco
        txt.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Agrega un acolchado interno invisible para distanciar el texto de los bordes físicos
        return txt; // Devuelve la caja de texto formateada
    } // Cierre de crearTextFieldOscuro

    private JButton crearBotonPrimario(String texto) { // Define una utilidad para armar botones de acción principal estándar
        JButton btn = new JButton(texto); // Instancia el botón incorporando la descripción recibida
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Aplica fuente en negrita tamaño 14
        btn.setBackground(new Color(0, 122, 255)); // Pinta el fondo del botón con el azul brillante moderno
        btn.setForeground(Color.WHITE); // Pinta las letras internas del botón en color blanco puro
        btn.setFocusPainted(false); // Elimina el recuadro punteado nativo de enfoque de Java al ser pulsado
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Modifica el puntero al icono de mano interactiva al sobrevolarlo
        return btn; // Devuelve el botón azul diseñado
    } // Cierre de crearBotonPrimario

    private JButton crearBotonPeligro(String texto) { // Define una utilidad para armar botones críticos de alerta o borrado
        JButton btn = new JButton(texto); // Instancia el botón con el enunciado recibido
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Aplica el estilo de fuente resaltada tamaño 14 en negrita
        btn.setBackground(new Color(255, 85, 85)); // Asigna un fondo rojo suave coral flat que denota peligro o remoción
        btn.setForeground(Color.WHITE); // Configura el enunciado en letras blancas para un óptimo contraste
        btn.setFocusPainted(false); // Quita el marco punteado de enfoque de Java
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Activa el cursor interactivo de mano selectora
        return btn; // Devuelve el botón rojo diseñado
    } // Cierre de crearBotonPeligro
} // Cierre definitivo de la clase ProductosView