package xyz.jugueteria.models;

public class Cliente {
    // Definimos las características o "cajas" que tendrá cada cliente en el sistema
    private int idCliente;          // El número de identificación único en la base de datos (autoincremental)
    private String dni;             // El documento de identidad del cliente
    private String nombreCompleto;  // Su nombre y apellidos
    private String telefono;        // Su número de contacto
    private String direccion;       // Dónde vive para enviarle los juguetes si es necesario

    // Constructor vacío: Permite crear un cliente "en blanco" para ir llenando sus datos poco a poco
    public Cliente() {}

    // Constructor con parámetros: Permite crear un cliente con todos sus datos listos de un solo golpe
    public Cliente(int idCliente, String dni, String nombreCompleto, String telefono, String direccion) {
        this.idCliente = idCliente;                 // Guarda el ID pasado al crear el objeto
        this.dni = dni;                             // Guarda el DNI pasado
        this.nombreCompleto = nombreCompleto;       // Guarda el nombre pasado
        this.telefono = telefono;                   // Guarda el teléfono pasado
        this.direccion = direccion;                 // Guarda la dirección pasada
    }

    // --- BLOQUE DE GETTERS Y SETTERS (Los intermediarios para leer o modificar los datos protegidos) ---

    // Obtiene el ID del cliente
    public int getIdCliente() {
        return idCliente;
    }

    // Cambia o asigna el ID del cliente
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    // Obtiene el DNI del cliente
    public String getDni() {
        return dni;
    }

    // Cambia o asigna el DNI del cliente
    public void setDni(String dni) {
        this.dni = dni;
    }

    // Obtiene el nombre completo del cliente
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    // Cambia o asigna el nombre completo del cliente
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    // Obtiene el teléfono del cliente
    public String getTelefono() {
        return telefono;
    }

    // Cambia o asigna el teléfono del cliente
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Obtiene la dirección del cliente
    public String getDireccion() {
        return direccion;
    }

    // Cambia o asigna la dirección del cliente
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // El método toString define cómo se mostrará el cliente si intentamos imprimirlo como texto simple
    // Muy útil para que en los JComboBox (listas desplegables de la interfaz gráfica) se vea ordenado
    @Override
    public String toString() {
        // En lugar de mostrar un código raro de Java, mostrará por ejemplo: "12345678 - Juan Pérez"
        return dni + " - " + nombreCompleto;
    }
}