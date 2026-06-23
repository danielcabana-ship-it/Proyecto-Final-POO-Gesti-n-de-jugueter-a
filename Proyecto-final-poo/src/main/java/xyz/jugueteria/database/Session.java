package xyz.jugueteria.database;

import xyz.jugueteria.models.Usuario;

/**
 * Gestor de la sesión actual de la aplicación.
 * Mantiene la información del usuario logueado en memoria durante la ejecución.
 */
public class Session {

    private static Usuario usuarioLogueado;

    /**
     * Establece el usuario que ha iniciado sesión con éxito.
     */
    public static void setUsuarioLogueado(Usuario usuario) {
        usuarioLogueado = usuario;
    }

    /**
     * Devuelve el usuario que está usando la aplicación actualmente.
     */
    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    /**
     * Verifica si hay alguna sesión activa.
     */
    public static boolean isLogueado() {
        return usuarioLogueado != null;
    }

    /**
     * Cierra la sesión activa.
     */
    public static void logout() {
        usuarioLogueado = null;
    }
}
