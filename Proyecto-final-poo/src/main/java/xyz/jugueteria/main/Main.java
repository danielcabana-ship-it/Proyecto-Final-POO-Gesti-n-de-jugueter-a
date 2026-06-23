package xyz.jugueteria.main;

import com.formdev.flatlaf.FlatDarkLaf;
import xyz.jugueteria.views.MainView;

import javax.swing.*;

/**
 * Este es el punto de entrada de la aplicación. 
 * Básicamente es lo primero que se ejecuta. Aquí le ponemos el traje oscuro (FlatDarkLaf) 
 * y llamamos a la ventana principal para que arranque el show.
 */
public class Main {

    public static void main(String[] args) {
        configurarLookAndFeel();

        // Swing es medio delicado, así que exige que toda la manipulación de la interfaz
        // ocurra en el Event Dispatch Thread (un hilo especial para UI). Por eso usamos invokeLater.
        SwingUtilities.invokeLater(() -> {
            MainView ventana = new MainView();
            ventana.setVisible(true); // ¡Y se hizo la luz!
        });
    }

    private static void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch (UnsupportedLookAndFeelException e) {
            // Si por alguna razón el tema falla (que no debería), avisamos y Swing usará el feo por defecto.
            System.err.println("Uy, no se pudo inicializar FlatLaf. Se verá un poco retro con el tema por defecto.");
        }
    }
}