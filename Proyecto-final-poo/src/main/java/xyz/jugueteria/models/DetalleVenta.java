package xyz.jugueteria.models;

public class DetalleVenta {
    // Definimos las propiedades de cada renglón del ticket de la juguetería
    private int idDetalle;          // El identificador único de esta fila específica en la base de datos
    private int idVenta;            // El ID del ticket principal al que pertenece este detalle (la cabecera)
    private int idProducto;         // El código del juguete que se está comprando
    private int cantidad;           // Cuántas unidades de este juguete se lleva el cliente
    private double precioUnitario;  // El precio de un solo juguete al momento de hacer la venta
    private double subtotal;        // El costo total de este renglón (multiplicar cantidad por precioUnitario)

    // Constructor vacío: Ideal para crear un renglón en blanco e ir dándole datos según se requiera
    public DetalleVenta() {}

    // Constructor con parámetros: Útil para armar un renglón con toda la información lista de un solo golpe
    public DetalleVenta(int idDetalle, int idVenta, int idProducto, int cantidad, double precioUnitario, double subtotal) {
        this.idDetalle = idDetalle;           // Guarda el ID propio de la fila
        this.idVenta = idVenta;               // Lo vincula a la venta global
        this.idProducto = idProducto;         // Guarda el código del producto comprado
        this.cantidad = cantidad;             // Guarda cuántos lleva
        this.precioUnitario = precioUnitario; // Guarda el precio cobrado
        this.subtotal = subtotal;             // Guarda el resultado de la multiplicación
    }

    // --- BLOQUE DE GETTERS Y SETTERS (Intermediarios para leer o modificar la información guardada) ---

    // Obtiene el ID propio de este renglón de detalle
    public int getIdDetalle() {
        return idDetalle;
    }

    // Asigna el ID propio de este renglón de detalle
    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    // Obtiene el ID de la venta global a la que pertenece
    public int getIdVenta() {
        return idVenta;
    }

    // Vincula este renglón al ID de una venta global
    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    // Obtiene el código del juguete vendido
    public int getIdProducto() {
        return idProducto;
    }

    // Asigna el código del juguete vendido
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    // Obtiene la cantidad de unidades que se llevan
    public int getCantidad() {
        return cantidad;
    }

    // Asigna la cantidad de unidades que se llevan
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // Obtiene el precio por unidad del juguete
    public double getPrecioUnitario() {
        return precioUnitario;
    }

    // Asigna el precio por unidad del juguete
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    // Obtiene el subtotal acumulado en este renglón
    public double getSubtotal() {
        return subtotal;
    }

    // Asigna el subtotal acumulado en este renglón
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}