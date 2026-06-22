package xyz.tecsup.proyectoFinal.Proyecto_final_poo;

/**
 * Clase base para todos los productos del inventario de la juguetería.
 *
 * Es abstracta porque no tiene sentido crear un "Producto" genérico:
 * siempre será un tipo concreto (electrónico, peluche, didáctico, etc.).
 * Las subclases heredan estos atributos comunes y agregan los suyos propios.
 */
public abstract class Producto {

    private int codigo;
    private String nombre;
    private double precio;
    private int stock;
    private int idCategoria;

    /**
     * Constructor vacío — necesario para frameworks que crean objetos por
     * reflexión (ej. al mapear resultados de la BD a objetos Java).
     * Es protected porque solo las subclases deberían usarlo.
     */
    protected Producto() {
    }

    /**
     * Constructor completo — se usa cuando ya tenemos todos los datos
     * del producto (por ejemplo, al leerlo de la base de datos).
     */
    public Producto(int codigo, String nombre, double precio, int stock, int idCategoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.idCategoria = idCategoria;
    }

    // --- Getters y Setters ---

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Representación legible del producto, útil para depurar con System.out.println().
     */
    @Override
    public String toString() {
        return "Producto{" +
                "codigo=" + codigo +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", idCategoria=" + idCategoria +
                '}';
    }
}