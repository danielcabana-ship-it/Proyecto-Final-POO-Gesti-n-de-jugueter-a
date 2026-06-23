package xyz.jugueteria.main;

import com.formdev.flatlaf.FlatDarkLaf;
import xyz.jugueteria.views.MainView;

import javax.swing.*;

/**
 * Punto de entrada de la aplicación.
 * Configura el tema visual FlatLaf (Dark) y lanza la ventana principal.
 */
public class Main {

    public static void main(String[] args) {
        configurarLookAndFeel();

        // Swing exige que toda manipulación de UI ocurra en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainView ventana = new MainView();
            ventana.setVisible(true);
        });
    }

    private static void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("No se pudo inicializar FlatLaf, se usará el tema por defecto.");
        }
    }
}