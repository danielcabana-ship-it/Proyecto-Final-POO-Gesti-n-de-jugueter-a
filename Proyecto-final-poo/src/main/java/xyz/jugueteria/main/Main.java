package xyz.jugueteria.main;

// Importamos el tema visual FlatLaf en su versión oscura para que la app no tenga el diseño gris antiguo de Java
import com.formdev.flatlaf.FlatDarkLaf;
// Importamos la vista del Login, que será la primera ventana en aparecer
import xyz.jugueteria.views.LoginView;

// Importamos las herramientas de Swing y AWT para construir y personalizar interfaces gráficas
import javax.swing.*;
import java.awt.*;

/**
 * Este es el punto de entrada de la aplicación.
 * Aquí le ponemos el traje oscuro (FlatDarkLaf) y algunas modernizaciones.
 */
public class Main {

    // El método main es el interruptor principal; lo primero que ejecuta Java al dar "Play" al proyecto
    public static void main(String[] args) {
        // Primero que nada, preparamos el diseño visual, los colores y los bordes redondeados
        configurarLookAndFeel();

        // Esto sirve para que la aplicación no se abra hasta que se logueen.
        // Iniciamos con la nueva pantalla de Login en lugar de la principal.
        // 'SwingUtilities.invokeLater' asegura que la interfaz gráfica se dibuje de forma segura sin congelar el programa
        SwingUtilities.invokeLater(() -> {
            // Creamos la ventana de inicio de sesión
            LoginView ventanaLogin = new LoginView();
            // La hacemos visible en la pantalla del usuario. ¡Que pase el siguiente!
            ventanaLogin.setVisible(true);
        });
    }

    // Este método es como el "estilista" de tu aplicación; define cómo se verán los botones, textos y tablas
    private static void configurarLookAndFeel() {
        try {
            // Le ordenamos a Java que use el tema oscuro moderno de FlatLaf
            UIManager.setLookAndFeel(new FlatDarkLaf());

            // Hacemos que todo sea más redondito para ese look premium
            UIManager.put("Button.arc", 15);        // Redondea las esquinas de todos los botones
            UIManager.put("Component.arc", 15);     // Redondea componentes generales como los cuadros de selección
            UIManager.put("TextComponent.arc", 15); // Redondea las cajas donde el usuario escribe texto
            UIManager.put("ScrollBar.thumbArc", 999); // Hace que la barra de desplazamiento sea completamente ovalada en las puntas
            UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2)); // Le da un pequeño margen de separación a la barra para que no pegue con los bordes

            // Colores por defecto para tablas para que resalten
            UIManager.put("Table.selectionBackground", new Color(0, 122, 255)); // Cuando el usuario haga clic en una fila, se pintará de un azul moderno
            UIManager.put("Table.selectionForeground", Color.WHITE);             // Las letras de la fila seleccionada se volverán blancas para que se lean bien

        } catch (UnsupportedLookAndFeelException e) {
            // Si por alguna razón la librería FlatLaf no se carga (ej. falta la dependencia), atrapamos el error y avisamos
            System.err.println("Uy, no se pudo inicializar FlatLaf. Se verá un poco retro con el tema por defecto.");
        }
    }
}