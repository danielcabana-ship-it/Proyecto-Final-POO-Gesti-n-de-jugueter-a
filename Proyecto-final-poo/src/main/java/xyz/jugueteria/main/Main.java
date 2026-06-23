package xyz.jugueteria.main;

import xyz.jugueteria.controllers.AdminController;
import xyz.jugueteria.dao.ProductoDAO;
import xyz.jugueteria.models.Producto;

public class Main {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   INICIANDO SISTEMA DE JUGUETERÍA TECSUP  ");
        System.out.println("==========================================\n");

        // 1. Demostrar el Login (Controlador + DAO)
        System.out.println("--- PRUEBA DE LOGIN DE ADMINISTRADOR (MVC) ---");
        AdminController adminCtrl = new AdminController();

        // Simulación de error
        System.out.println("Intentando ingresar con credenciales incorrectas...");
        boolean loginMalo = adminCtrl.validarIngreso("admin_falso", "123");
        System.out.println("Resultado: " + (loginMalo ? "Acceso Permitido" : "Acceso Denegado (Correcto)\n"));

        // Simulación de éxito (IMPORTANTE: Pon un usuario y clave que sí existan en tu BD)
        System.out.println("Intentando ingresar con credenciales válidas...");
        boolean loginBueno = adminCtrl.validarIngreso("root", "admin"); // <-- CAMBIA ESTOS DATOS POR LOS DE TU BD
        System.out.println("Resultado: " + (loginBueno ? "¡Acceso Permitido! (Correcto)\n" : "Acceso Denegado\n"));

        // 2. Demostrar la Base de Datos (Productos)
        System.out.println("--- INVENTARIO DE JUGUETERÍA ---");
        ProductoDAO prodDAO = new ProductoDAO();

        System.out.println("Consultando productos en la base de datos MariaDB...\n");
        for (Producto p : prodDAO.listarProductos()) {
            System.out.println(" -> " + p.getNombre() + " | Precio: S/." + p.getPrecio() + " | Stock: " + p.getStock());
        }

        System.out.println("\n==========================================");
        System.out.println("      EJECUCIÓN FINALIZADA CON ÉXITO      ");
        System.out.println("==========================================");
    }
}