package xyz.jugueteria.main;

import com.formdev.flatlaf.FlatDarkLaf;
import xyz.jugueteria.views.LoginView;

import javax.swing.*;
import java.awt.*;

/**
 * Este es el punto de entrada de la aplicación. 
 * Aquí le ponemos el traje oscuro (FlatDarkLaf) y algunas modernizaciones.
 */
public class Main {

    public static void main(String[] args) {
        configurarLookAndFeel();

        // Esto sirve para que la aplicación no se abra hasta que se logueen.
        // Iniciamos con la nueva pantalla de Login en lugar de la principal.
        SwingUtilities.invokeLater(() -> {
            LoginView ventanaLogin = new LoginView();
            ventanaLogin.setVisible(true); // ¡Que pase el siguiente!
        });
    }

    private static void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            
            // Hacemos que todo sea más redondito para ese look premium
            UIManager.put("Button.arc", 15);
            UIManager.put("Component.arc", 15);
            UIManager.put("TextComponent.arc", 15);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));

            // Colores por defecto para tablas para que resalten
            UIManager.put("Table.selectionBackground", new Color(0, 122, 255));
            UIManager.put("Table.selectionForeground", Color.WHITE);
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Uy, no se pudo inicializar FlatLaf. Se verá un poco retro con el tema por defecto.");
        }
    }
}