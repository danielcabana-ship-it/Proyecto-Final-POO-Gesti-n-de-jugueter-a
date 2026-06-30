package xyz.jugueteria.models;

// Importamos la herramienta de Java que nos permite manejar fechas y horas con precisión milimétrica (Timestamp)
import java.sql.Timestamp;

public class Venta {
    // Definimos los datos generales que lleva la cabecera de cualquier ticket de la tienda
    private int idVenta;          // El número de ticket o ID único de la venta en la base de datos (autoincremental)
    private int idCliente;        // El ID del cliente que realizó la compra (vínculo con la tabla clientes)
    private int idUsuario;        // El ID del trabajador/cajero que registró la venta (vínculo con la tabla usuarios)
    private Timestamp fechaVenta; // El día, hora, minuto y segundo exacto en que se cerró la venta
    private double total;         // El dinero total que se cobró por toda la compra

    // Constructor vacío: Permite crear un ticket en blanco para ir rellenando sus datos en el proceso
    public Venta() {}

    // Constructor con parámetros: Ideal para estructurar una venta con toda la información junta de un solo golpe
    public Venta(int idVenta, int idCliente, int idUsuario, Timestamp fechaVenta, double total) {
        this.idVenta = idVenta;       // Guarda el número de ticket asignado
        this.idCliente = idCliente;   // Guarda quién fue el cliente
        this.idUsuario = idUsuario;   // Guarda qué cajero atendió
        this.fechaVenta = fechaVenta; // Guarda el momento exacto del cobro
        this.total = total;           // Guarda el monto total cobrado
    }

    // --- BLOQUE DE GETTERS Y SETTERS (Intermediarios obligatorios para leer o modificar la información de forma segura) ---

    // Obtiene el número de ticket (ID de venta)
    public int getIdVenta() {
        return idVenta;
    }

    // Asigna o cambia el número de ticket (ID de venta)
    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    // Obtiene el ID del cliente vinculado a la venta
    public int getIdCliente() {
        return idCliente;
    }

    // Vincula un cliente a este ticket mediante su ID
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    // Obtiene el ID del cajero que procesó el pago
    public int getIdUsuario() {
        return idUsuario;
    }

    // Vincula al cajero responsable a este ticket mediante su ID
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Obtiene la fecha y hora exacta del ticket
    public Timestamp getFechaVenta() {
        return fechaVenta;
    }

    // Asigna la fecha y hora en la que se realiza la transacción
    public void setFechaVenta(Timestamp fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    // Obtiene la suma total de dinero del ticket
    public double getTotal() {
        return total;
    }

    // Asigna el monto final a cobrar en este ticket
    public void setTotal(double total) {
        this.total = total;
    }

    // El método toString define cómo se verá este objeto si lo imprimes como una cadena de texto simple
    // Es genial para mostrar resúmenes rápidos en listas de la interfaz gráfica
    @Override
    public String toString() {
        // En lugar de códigos extraños de Java, mostrará limpiamente: "Venta #145 | Total: S/.120.50"
        return "Venta #" + idVenta + " | Total: S/." + total;
    }
}