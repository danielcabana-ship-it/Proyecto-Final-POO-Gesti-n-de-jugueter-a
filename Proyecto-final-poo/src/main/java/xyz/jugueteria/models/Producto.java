package xyz.jugueteria.models;

public class Producto {
    // Definimos los atributos o características que describen a un juguete de la tienda
    private int codigo;              // El código o ID único del producto en la base de datos
    private String nombre;           // El nombre del juguete (ej. "Coche a Control Remoto")
    private double precio;           // El costo del juguete para el cliente
    private int stock;               // El inventario actual (cuántos quedan disponibles para vender)
    private int idCategoria;         // El código de la categoría a la que pertenece (ej. 1=Acción, 2=Mesa)
    private boolean requiereBaterias; // Un interruptor: 'true' si usa pilas, 'false' si no las necesita

    // Constructor vacío: Permite crear un juguete en blanco para ir asignándole datos poco a poco
    public Producto() {}

    // Constructor con parámetros: Ideal para crear un juguete con toda su información lista de un solo golpe
    public Producto(int codigo, String nombre, double precio, int stock, int idCategoria, boolean requiereBaterias) {
        this.codigo = codigo;                     // Guarda el código asignado
        this.nombre = nombre;                     // Guarda el nombre asignado
        this.precio = precio;                     // Guarda el precio asignado
        this.stock = stock;                       // Guarda el stock inicial asignado
        this.idCategoria = idCategoria;           // Guarda la categoría asignada
        this.requiereBaterias = requiereBaterias; // Guarda si lleva pilas o no
    }

    // --- BLOQUE DE GETTERS Y SETTERS (Intermediarios obligatorios para leer o modificar la información guardada) ---

    // Obtiene el código del juguete
    public int getCodigo() { return codigo; }
    // Asigna o cambia el código del juguete
    public void setCodigo(int codigo) { this.codigo = codigo; }

    // Obtiene el nombre del juguete
    public String getNombre() { return nombre; }
    // Asigna o cambia el nombre del juguete
    public void setNombre(String nombre) { this.nombre = nombre; }

    // Obtiene el precio del juguete
    public double getPrecio() { return precio; }
    // Asigna o cambia el precio del juguete
    public void setPrecio(double precio) { this.precio = precio; }

    // Obtiene cuántas unidades quedan en inventario
    public int getStock() { return stock; }
    // Actualiza la cantidad de unidades en inventario
    public void setStock(int stock) { this.stock = stock; }

    // Obtiene el ID de la categoría del juguete
    public int getIdCategoria() { return idCategoria; }
    // Vincula el juguete a una categoría específica
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    // Pregunta si el juguete necesita pilas (devuelve true o false)
    public boolean isRequiereBaterias() { return requiereBaterias; }
    // Cambia el estado de si el juguete necesita pilas o no
    public void setRequiereBaterias(boolean requiereBaterias) { this.requiereBaterias = requiereBaterias; }

    // El método toString define cómo se transformará este objeto si necesitas imprimirlo en consola como texto rápido
    @Override
    public String toString() {
        // En vez de mostrar una dirección de memoria rara, imprimirá algo claro como: "Producto [10] Oso de Peluche - Precio: S/.45.50 - Stock: 12"
        return "Producto [" + codigo + "] " + nombre + " - Precio: S/." + precio + " - Stock: " + stock;
    }
}