package xyz.tecsup.proyectoFinal.Proyecto_final_poo;

import xyz.jugueteria.database.ConexionDB;

/**
 * Punto de entrada de la aplicación para el Entregable 1.
 *
 * Solo valida la inicialización de la conexión de la base de datos
 * para asegurar que la infraestructura esté correctamente configurada.
 */
public class App {

    public static void main(String[] args) {
        System.out.println("=== Sistema de Gestión de Juguetería — Entregable 1 ===");
        
        // Inicializa y valida la instancia de la base de datos
        if (ConexionDB.getInstancia() != null) {
            System.out.println("Infraestructura de base de datos cargada correctamente.");
        } else {
            System.err.println("Error crítico al inicializar el Singleton de conexión.");
        }
    }
}
