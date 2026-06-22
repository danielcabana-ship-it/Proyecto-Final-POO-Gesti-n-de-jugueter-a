package xyz.jugueteria.models;

public class Producto {
    private int codigo;
    private String nombre;
    private double precio;
    private int stock;
    private int idCategoria;
    private boolean requiereBaterias;

    // Constructor vacío
    public Producto() {}

    // Constructor con parámetros
    public Producto(int codigo, String nombre, double precio, int stock, int idCategoria, boolean requiereBaterias) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.idCategoria = idCategoria;
        this.requiereBaterias = requiereBaterias;
    }

    // Getters y Setters
    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public boolean isRequiereBaterias() { return requiereBaterias; }
    public void setRequiereBaterias(boolean requiereBaterias) { this.requiereBaterias = requiereBaterias; }

    @Override
    public String toString() {
        return "Producto [" + codigo + "] " + nombre + " - Precio: S/." + precio + " - Stock: " + stock;
    }
}