package xyz.jugueteria.controllers;

import xyz.jugueteria.dao.AdminDAO;

public class AdminController {
    // Estructura inicial creada. La lógica se implementará en el siguiente commit.
    private AdminDAO adminDAO;

    // Constructor que inicializa la conexión con el DAO
    public AdminController() {
        this.adminDAO = new AdminDAO();
    }

    // Método para validar las credenciales del administrador
    public boolean validarIngreso(String usuario, String contrasenia) {
        return adminDAO.login(usuario, contrasenia);
    }
}