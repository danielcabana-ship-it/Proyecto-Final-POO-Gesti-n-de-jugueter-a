package xyz.jugueteria.main;

import com.formdev.flatlaf.FlatDarkLaf;
import xyz.jugueteria.views.MainView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Configuramos el Look and Feel moderno de FlatLaf (Tema Oscuro)
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            // Configuraciones globales de UI para darle un toque más premium
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("ProgressBar.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception ex) {
            System.err.println("No se pudo inicializar FlatLaf");
        }

        // Iniciar la UI en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainView view = new MainView();
            view.setVisible(true);
        });
    }
}
