package xyz.jugueteria.models;

public class Usuario {
    // Definimos las propiedades que tendrá cada trabajador o usuario en el sistema
    private int idUsuario;    // El número de identificación único en la base de datos (autoincremental)
    private String nombre;    // El nombre real y completo del trabajador (ej. "Carlos Mendoza")
    private String username;  // El alias o apodo con el que iniciará sesión (ej. "carlos_admin")
    private String email;     // Su correo electrónico de contacto
    private String password;  // Su contraseña (que se guardará encriptada como un hash de texto largo)
    private String rol;       // El puesto o nivel de acceso que tiene (ej. "ADMIN" o "CAJERO")

    // Constructor vacío: Permite crear un usuario "en blanco" para ir escribiendo sus datos paso a paso
    public Usuario() {}

    // Constructor con parámetros: Ideal para crear un usuario completo con todos sus datos de un solo golpe
    public Usuario(int idUsuario, String nombre, String username, String email, String password, String rol) {
        this.idUsuario = idUsuario; // Guarda el ID asignado
        this.nombre = nombre;       // Guarda el nombre real asignado
        this.username = username;   // Guarda el alias elegido
        this.email = email;         // Guarda el correo asignado
        this.password = password;   // Guarda la contraseña (encriptada) asignada
        this.rol = rol;             // Guarda el rol o puesto asignado
    }

    // --- BLOQUE DE GETTERS Y SETTERS (Intermediarios obligatorios para leer o modificar de forma segura la información) ---

    // Obtiene el ID único del usuario
    public int getIdUsuario() {
        return idUsuario;
    }

    // Asigna o cambia el ID único del usuario
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Obtiene el nombre real del usuario
    public String getNombre() {
        return nombre;
    }

    // Asigna o cambia el nombre real del usuario
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Obtiene el alias (username) para el login
    public String getUsername() {
        return username;
    }

    // Asigna o cambia el alias (username) para el login
    public void setUsername(String username) {
        this.username = username;
    }

    // Obtiene el correo electrónico del usuario
    public String getEmail() {
        return email;
    }

    // Asigna o cambia el correo electrónico del usuario
    public void setEmail(String email) {
        this.email = email;
    }

    // Obtiene la contraseña (el hash encriptado)
    public String getPassword() {
        return password;
    }

    // Asigna o cambia la contraseña del usuario
    public void setPassword(String password) {
        this.password = password;
    }

    // Obtiene el rol o nivel de permisos (ej. saber si puede ver reportes financieros)
    public String getRol() {
        return rol;
    }

    // Asigna o cambia el rol o nivel de permisos del usuario
    public void setRol(String rol) {
        this.rol = rol;
    }

    // El método toString define cómo se mostrará el usuario si lo imprimimos directamente como texto simple
    // Es genial para mostrar información limpia en la interfaz gráfica
    @Override
    public String toString() {
        // En lugar de un código confuso de Java, mostrará algo limpio como: "Carlos Mendoza (ADMIN)"
        return nombre + " (" + rol + ")";
    }
}