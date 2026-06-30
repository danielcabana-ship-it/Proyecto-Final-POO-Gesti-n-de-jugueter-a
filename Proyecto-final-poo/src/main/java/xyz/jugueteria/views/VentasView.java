package xyz.jugueteria.views; // Especifica el paquete al que pertenece este componente de interfaz gráfica

import xyz.jugueteria.dao.ClienteDAO; // Importa el DAO de clientes para cargar la lista de compradores en el combo
import xyz.jugueteria.dao.ProductoDAO; // Importa el DAO de productos para jalar el stock y catálogo disponible
import xyz.jugueteria.dao.VentaDAO; // Importa el DAO transaccional para guardar la cabecera del ticket y sus detalles
import xyz.jugueteria.models.Cliente; // Importa el modelo Cliente para poblar la lista desplegable
import xyz.jugueteria.models.DetalleVenta; // Importa el modelo de los ítems del carrito para calcular subtotales
import xyz.jugueteria.models.Producto; // Importa el modelo Producto para verificar precios y stock actual
import xyz.jugueteria.models.Venta; // Importa el modelo Venta para estructurar los datos finales del ticket
import xyz.jugueteria.database.Session; // Importa la sesión global para capturar el ID del empleado que atiende la caja

import javax.swing.*; // Importa los componentes visuales base de Swing (combos, tablas, botones, etc.)
import javax.swing.table.DefaultTableCellRenderer; // Importa el renderizador para alinear y dar formato a las celdas
import javax.swing.table.DefaultTableModel; // Importa el modelo de datos de la tabla para controlar el carrito de compras
import javax.swing.table.JTableHeader; // Importa el componente que controla la barra superior de títulos de la tabla
import java.awt.*; // Importa las herramientas de AWT como layouts, colores de pantalla, fuentes y cursores
import java.util.ArrayList; // Importa ArrayList para instanciar la lista dinámica de los detalles comprados
import java.util.List; // Importa la interfaz List para manejar colecciones de datos en memoria

/**
 * Pantalla de Punto de Venta (POS).
 * Totalmente rediseñada con el nuevo estilo oscuro y toques modernos.
 */
public class VentasView extends JPanel { // Declara la clase VentasView heredando de JPanel para ser incrustada en el panel central

    private VentaDAO ventaDAO; // Declara el objeto DAO encargado de persistir la transacción final en la BD
    private ClienteDAO clienteDAO; // Declara el objeto DAO encargado de consultar el directorio de clientes
    private ProductoDAO productoDAO; // Declara el objeto DAO encargado de leer el inventario de juguetes

    private JComboBox<Cliente> cbClientes; // Declara la lista desplegable selectora de clientes
    private JComboBox<Producto> cbProductos; // Declara la lista desplegable selectora de productos
    private JTextField txtCantidad; // Declara la caja de texto para ingresar cuántas unidades se llevará el cliente

    private JTable tableDetalles; // Declara el componente de la tabla visual para mostrar el carrito de compras
    private DefaultTableModel tableModel; // Declara el modelo estructural de datos para agregar ítems a la tabla

    private JLabel lblTotal; // Declara la etiqueta grande destinada a reflejar el monto total acumulado de la venta
    private double totalVenta = 0.0; // Declara e inicializa en cero la variable acumuladora del costo total

    private List<DetalleVenta> detallesActuales; // Declara la lista en memoria que guardará temporalmente los ítems del ticket

    public VentasView() { // Constructor de la clase: levanta y acopla la interfaz del punto de venta
        ventaDAO = new VentaDAO(); // Instancia el DAO transaccional de ventas
        clienteDAO = new ClienteDAO(); // Instancia el DAO de consultas de clientes
        productoDAO = new ProductoDAO(); // Instancia el DAO de consultas de productos
        detallesActuales = new ArrayList<>(); // Inicializa la lista temporal como un ArrayList vacío listo para recibir productos

        setLayout(new BorderLayout(15, 15)); // Aplica diseño de BorderLayout separando las regiones por 15 píxeles de espacio

        // Fondo asfalto para mantener consistencia
        setBackground(new Color(30, 32, 40)); // Pinta el fondo del contenedor con el gris oscuro asfalto característico
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25)); // Añade un margen interno de confort de 25 píxeles en las cuatro direcciones

        // Título de la sección
        JLabel lblTitle = new JLabel("Punto de Venta"); // Instancia el encabezado principal de la ventana
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Asigna fuente moderna Segoe UI tamaño 28 en negrita
        lblTitle.setForeground(Color.WHITE); // Cambia el color del título a blanco para contraste premium
        add(lblTitle, BorderLayout.NORTH); // Posiciona el título en la franja superior (Norte) del panel

        // --- Panel Superior: Selección de Cliente y Producto ---
        JPanel panelTop = new JPanel(new GridLayout(2, 1, 10, 15)); // Crea un contenedor superior distribuido en 2 filas y 1 columna
        panelTop.setBackground(new Color(30, 32, 40)); // Pinta el fondo del contenedor superior en color oscuro asfalto

        JPanel panelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); // Crea una fila horizontal alineada a la izquierda para el cliente
        panelCliente.setBackground(new Color(30, 32, 40)); // Aplica fondo oscuro al bloque horizontal del cliente
        panelCliente.add(crearLabelBlanco("Cliente:")); // Inserta la etiqueta indicativa llamando al helper visual
        cbClientes = new JComboBox<>(); // Instancia la lista desplegable que alojará los objetos de tipo Cliente
        cbClientes.setPreferredSize(new Dimension(300, 35)); // Define el tamaño ideal del JComboBox: 300 de ancho por 35 de alto
        panelCliente.add(cbClientes); // Agrega el combo de clientes a su respectiva línea horizontal

        JPanel panelProducto = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); // Crea otra fila horizontal alineada a la izquierda para el producto
        panelProducto.setBackground(new Color(30, 32, 40)); // Fondo oscuro para la línea del producto
        panelProducto.add(crearLabelBlanco("Producto:")); // Inserta la etiqueta indicativa de producto
        cbProductos = new JComboBox<>(); // Instancia la lista desplegable que alojará los objetos de tipo Producto
        cbProductos.setPreferredSize(new Dimension(300, 35)); // Define el tamaño ideal del JComboBox de productos
        panelProducto.add(cbProductos); // Inserta el combo de productos en la línea de controles

        panelProducto.add(crearLabelBlanco("Cantidad:")); // Agrega el texto indicativo para el input numérico
        txtCantidad = crearTextFieldOscuro(); // Genera la caja de texto estilizada oscura mediante el helper inferior
        txtCantidad.setPreferredSize(new Dimension(80, 35)); // Define un tamaño compacto de 80px de ancho para la caja de cantidad
        panelProducto.add(txtCantidad); // Agrega la caja de entrada de cantidad a la hilera horizontal

        JButton btnAgregarDetalle = crearBotonPrimario("Agregar a Venta"); // Fabrica el botón azul para meter el ítem al carrito
        panelProducto.add(btnAgregarDetalle); // Inserta el botón de agregado al final de la hilera horizontal de productos

        panelTop.add(panelCliente); // Añade la hilera completa del cliente a la fila 1 de la cuadrícula superior
        panelTop.add(panelProducto); // Añade la hilera completa del producto a la fila 2 de la cuadrícula superior

        add(panelTop, BorderLayout.NORTH); // Reemplaza/fija el panel superior con sus dos hileras en la zona Norte de la pantalla

        // --- Tabla de Detalles ---
        tableModel = new DefaultTableModel(new String[]{"ID Producto", "Producto", "Cantidad", "Precio Unit.", "Subtotal"}, 0); // Define los títulos de las columnas del carrito
        tableDetalles = new JTable(tableModel); // Instancia la tabla de cotización inyectándole el modelo de datos vacío
        tableDetalles.setRowHeight(35); // Ajusta la altura de las filas a 35 píxeles para visualización de registros holgada
        tableDetalles.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Asigna tipografía estándar tamaño 14 a los datos escritos
        tableDetalles.setBackground(new Color(40, 42, 54)); // Define fondo oscuro integrado para las filas de productos
        tableDetalles.setForeground(Color.WHITE); // Cambia el color del texto de los productos a color blanco
        tableDetalles.setGridColor(new Color(60, 62, 74)); // Pinta los separadores de celdas en gris atenuado
        tableDetalles.setShowVerticalLines(false); // Oculta las rejillas verticales logrando un diseño plano limpio

        // Cabecera estilizada
        JTableHeader header = tableDetalles.getTableHeader(); // Extrae la barra de títulos de columnas para formatearla
        header.setBackground(new Color(0, 122, 255)); // Pinta la cabecera de la tabla con el color azul brillante
        header.setForeground(Color.WHITE); // Cambia las letras de los títulos a color blanco puro
        header.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Aplica fuente en negrita tamaño 14 a los títulos

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer(); // Instancia un formateador de celdas
        centerRenderer.setHorizontalAlignment(JLabel.CENTER); // Configura la alineación horizontal centrada
        tableDetalles.setDefaultRenderer(Object.class, centerRenderer); // Dictamina que todos los datos numéricos y de texto se muestren centrados

        JScrollPane scrollPane = new JScrollPane(tableDetalles); // Encasilla la tabla en un scrollpane por si la lista de compras crece demasiado
        scrollPane.getViewport().setBackground(new Color(30, 32, 40)); // Forza al fondo oculto a mantener el color oscuro al deslizar
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Elimina la molesta línea de borde por defecto de los scrollpanes
        add(scrollPane, BorderLayout.CENTER); // Ubica el carrito de compras en toda la zona neurálgica y amplia (Centro) de la pantalla

        // --- Panel Inferior: Total y Botón Finalizar ---
        JPanel panelBottom = new JPanel(new BorderLayout(15, 15)); // Crea el sub-panel para la base de la pantalla estructurado en regiones
        panelBottom.setBackground(new Color(30, 32, 40)); // Sincroniza el fondo en gris oscuro asfalto

        lblTotal = new JLabel("Total: S/. 0.00"); // Instancia el marcador en cero del costo final acumulado
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Aplica fuente imponente y robusta de tamaño 24
        lblTotal.setForeground(new Color(80, 250, 123)); // Pinta el dinero de un color verde eléctrico brillante estilo terminal hacker

        JButton btnFinalizar = new JButton("Finalizar Venta"); // Instancia el botón de facturación y cierre de caja
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 18)); // Letra grande tamaño 18 en negrita para llamar la atención del cajero
        btnFinalizar.setBackground(new Color(80, 250, 123)); // Pinta el fondo del botón de color verde brillante indicando éxito/cierre
        btnFinalizar.setForeground(new Color(20, 50, 20)); // Pinta las letras internas del botón de color verde bosque muy oscuro para contraste idóneo
        btnFinalizar.setFocusPainted(false); // Quita el contorno punteado nativo de foco al ser seleccionado
        btnFinalizar.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor con el icono de la mano interactiva al sobrevolarlo
        btnFinalizar.setPreferredSize(new Dimension(200, 50)); // Define dimensiones anchas e importantes para el botón: 200 por 50 píxeles

        panelBottom.add(lblTotal, BorderLayout.WEST); // Empuja el marcador verde del total hacia el costado izquierdo (Oeste)
        panelBottom.add(btnFinalizar, BorderLayout.EAST); // Empuja el botón verde de facturación hacia el extremo derecho (Este)

        add(panelBottom, BorderLayout.SOUTH); // Engancha el bloque completo de la base en la región Sur de la pantalla

        // --- Eventos ---
        btnAgregarDetalle.addActionListener(e -> agregarDetalle()); // Configura el botón azul para ejecutar la lógica de validación e inserción al carrito
        btnFinalizar.addActionListener(e -> finalizarVenta()); // Configura el botón verde para persistir la compra completa en el servidor base de datos

        cargarCombos(); // Ejecuta la recarga inmediata de clientes y productos al iniciar por primera vez el panel
    } // Cierre del constructor de VentasView

    private void cargarCombos() { // Declara la función destinada a sincronizar los menús desplegables con la base de datos
        cbClientes.removeAllItems(); // Vacía todas las opciones de clientes previas en la lista desplegable
        for (Cliente c : clienteDAO.listarClientes()) { // Recorre mediante un bucle for-each los clientes activos extraídos del DAO
            cbClientes.addItem(c); // Añade el objeto cliente completo como opción en el combo (utiliza su método toString para pintarse)
        } // Cierre del ciclo de clientes

        cbProductos.removeAllItems(); // Vacía de raíz todas las opciones previas del catálogo de juguetes en el combo
        for (Producto p : productoDAO.listarProductos()) { // Recorre uno a uno los juguetes disponibles en almacén provistos por el DAO
            cbProductos.addItem(p); // Añade el objeto producto completo a las opciones de compra de la lista desplegable
        } // Cierre del ciclo de productos
    } // Cierre del método cargarCombos

    private void agregarDetalle() { // Declara el método encargado de añadir un artículo seleccionado al mazo de compras en memoria
        Producto p = (Producto) cbProductos.getSelectedItem(); // Recupera el objeto Producto actualmente seleccionado en la lista desplegable
        if (p == null) return; // Si la lista de productos está totalmente vacía, interrumpe el flujo de agregado de inmediato

        try { // Abre bloque try-catch para capturar errores si el operario ingresa caracteres no numéricos en la cantidad
            int cantidad = Integer.parseInt(txtCantidad.getText()); // Extrae y convierte el texto ingresado a número entero
            if (cantidad <= 0) { // Valida si el cajero cometió el error de tipear un valor de cero o negativo
                JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0."); // Notifica la restricción del negocio
                return; // Corta el flujo del método impidiendo agregar registros inválidos
            } // Fin de verificación de cantidad nula
            if (cantidad > p.getStock()) { // Valida en tiempo real si el cliente pide más unidades de las disponibles físicamente en almacén
                JOptionPane.showMessageDialog(this, "Stock insuficiente. Disponible: " + p.getStock()); // Advierte del quiebre de inventario
                return; // Corta la ejecución del método para salvaguardar la consistencia matemática de la BD
            } // Fin de verificación de stock

            double subtotal = cantidad * p.getPrecio(); // Calcula el costo parcial multiplicando el volumen pedido por el precio unitario

            DetalleVenta detalle = new DetalleVenta(0, 0, p.getCodigo(), cantidad, p.getPrecio(), subtotal); // Inicializa un objeto DetalleVenta con los campos matemáticos correspondientes
            detallesActuales.add(detalle); // Guarda el ítem calculado dentro de la lista temporal en memoria

            tableModel.addRow(new Object[]{p.getCodigo(), p.getNombre(), cantidad, p.getPrecio(), subtotal}); // Inserta una nueva fila visual al carrito con la información del artículo

            totalVenta += subtotal; // Suma de forma acumulativa el subtotal calculado al valor general de la transacción
            lblTotal.setText(String.format("Total: S/. %.2f", totalVenta)); // Actualiza la etiqueta verde formateando el dinero con dos decimales exactos
            txtCantidad.setText(""); // Vacía la caja de texto de cantidad para dejarla lista para el próximo juguete

        } catch (NumberFormatException e) { // Captura la excepción si el usuario escribió letras, símbolos o dejó en blanco el campo de cantidad
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida."); // Muestra alerta solicitando redactar un número entero real
        } // Cierre del bloque de control de excepciones try-catch
    } // Cierre del método agregarDetalle

    private void finalizarVenta() { // Declara la función encargada de procesar, firmar y guardar la transacción total en SQL
        if (detallesActuales.isEmpty()) { // Comprueba si el operario de caja pulsó cobrar teniendo la lista de compras totalmente desierta
            JOptionPane.showMessageDialog(this, "No hay productos en la venta."); // Lanza advertencia flotante impidiendo transacciones vacías
            return; // Detiene el guardado final de inmediato
        } // Fin de validación de carrito desierto

        Cliente cliente = (Cliente) cbClientes.getSelectedItem(); // Captura cuál objeto cliente del combo está recibiendo la compra
        if (cliente == null) { // Si por fallos del sistema o falta de datos no hay ningún cliente seleccionado
            JOptionPane.showMessageDialog(this, "Seleccione un cliente."); // Solicita de forma explícita asociar a un comprador
            return; // Detiene la operación de inmediato
        } // Fin de validación de cliente nulo

        Venta venta = new Venta(); // Crea una instancia en blanco del modelo maestro Venta (cabecera del ticket)
        venta.setIdCliente(cliente.getIdCliente()); // Extrae el identificador del cliente del combo y lo asocia a la cabecera
        if (Session.isLogueado()) { // Evalúa si el software reconoce qué cajero o administrador se encuentra en turno activo
            venta.setIdUsuario(Session.getUsuarioLogueado().getIdUsuario()); // Extrae el ID del usuario en sesión y lo sella en el registro de la venta
        } else { // Si por un imprevisto técnico la sesión se ha desvinculado
            venta.setIdUsuario(1); // default / fallback -> Aplica el código de empleado por defecto (operador genérico número 1)
        } // Fin de asociación de usuario operador
        venta.setTotal(totalVenta); // Incorpora el acumulado general del dinero calculado a la cabecera del ticket

        if (ventaDAO.registrarVentaCompleta(venta, detallesActuales)) { // Despacha la cabecera y el carrito al método atómico/transaccional del DAO y valida el éxito en SQL
            JOptionPane.showMessageDialog(this, "¡Venta registrada con éxito!"); // Lanza cuadro de confirmación informando de la correcta inserción y descuento de stock
            // Limpiar la vista
            detallesActuales.clear(); // Elimina de memoria todos los detalles del carrito recién procesado
            tableModel.setRowCount(0); // Limpia por completo todas las filas visibles de la JTable
            totalVenta = 0.0; // Restablece el acumulador general de dinero de vuelta a cero absoluto
            lblTotal.setText("Total: S/. 0.00"); // Reinicia el marcador verde de total de la interfaz gráfica
            cargarCombos(); // Recarga los combos del catálogo actualizando instantáneamente las nuevas existencias de stock tras la venta
        } else { // Si ocurre un fallo crítico al guardar o al procesar la base de datos (Error de llaves foráneas, caída de red, etc.)
            JOptionPane.showMessageDialog(this, "Error al registrar la venta.", "Error", JOptionPane.ERROR_MESSAGE); // Lanza un aviso de alerta rojo notificando la falla de guardado
        } // Cierre del bloque condicional transaccional
    } // Cierre del método finalizarVenta

    // --- Utilidades visuales reutilizadas (Fábricas de Formato) ---

    private JLabel crearLabelBlanco(String texto) { // Método auxiliar rápido para estructurar leyendas textuales del POS
        JLabel lbl = new JLabel(texto); // Instancia la etiqueta con el rótulo provisto
        lbl.setForeground(new Color(200, 200, 200)); // Aplica tono gris claro de óptima visualización sobre fondos oscuros
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Configura tipografía en negrita y de tamaño cómodo 14
        return lbl; // Retorna la etiqueta formateada lista para insertarse
    } // Cierre de crearLabelBlanco

    private JTextField crearTextFieldOscuro() { // Método auxiliar rápido para estandarizar las entradas de escritura del módulo
        JTextField txt = new JTextField(); // Instancia un campo de escritura básico
        txt.setBackground(new Color(40, 42, 54)); // Configura el fondo con un tono gris oscuro unificado con las tablas
        txt.setForeground(Color.WHITE); // Configura el texto que tipee el empleado en color blanco para legibilidad perfecta
        txt.setCaretColor(Color.WHITE); // Cambia el color de la barra parpadeante de escritura a blanco brillante
        txt.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Añade un margen interior libre para evitar que los números queden apretados contra los bordes
        return txt; // Retorna la caja de texto armada
    } // Cierre de crearTextFieldOscuro

    private JButton crearBotonPrimario(String texto) { // Método auxiliar rápido para forjar botones de acción de flujo
        JButton btn = new JButton(texto); // Instancia el botón incorporando la descripción recibida
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Aplica fuente en negrita de tamaño estandarizado 14
        btn.setBackground(new Color(0, 122, 255)); // Define el color de fondo con el azul eléctrico moderno
        btn.setForeground(Color.WHITE); // Letras internas del botón en color blanco puro para alto contraste
        btn.setFocusPainted(false); // Desactiva el marco de puntos nativo que empaña el diseño al pulsar el control
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Activa el cursor de manita interactiva al posicionar el mouse encima
        return btn; // Retorna el botón azul diseñado
    } // Cierre de crearBotonPrimario
} // Cierre definitivo del archivo y la clase VentasView