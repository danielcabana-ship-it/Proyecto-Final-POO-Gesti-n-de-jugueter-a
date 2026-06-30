package xyz.jugueteria.util;

// Importamos la herramienta encargada de generar resúmenes criptográficos (hashes)
import java.security.MessageDigest;
// Importamos la excepción que saltaría si el sistema de Java no reconoce el algoritmo de seguridad
import java.security.NoSuchAlgorithmException;

/**
 * Clase utilitaria para encriptar contraseñas mediante hash SHA-256.
 * Esto asegura que las credenciales no se guarden en texto plano en la base de datos.
 */
public class SecurityUtils {

    /**
     * Aplica el algoritmo SHA-256 a la contraseña recibida en texto plano.
     *
     * @param password la contraseña en texto plano.
     * @return el hash hexadecimal de 64 caracteres.
     */
    public static String hashPassword(String password) {
        // Si no nos pasaron ninguna contraseña (es nula), devolvemos null de inmediato para evitar errores
        if (password == null) {
            return null;
        }
        try {
            // Inicializamos el "triturador" de seguridad configurándolo específicamente para usar el algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convertimos la contraseña de texto a un arreglo de bytes y la procesamos para obtener el resumen oculto (hash)
            byte[] hash = digest.digest(password.getBytes());

            // Creamos un constructor de texto eficiente para ir armando la cadena final en formato hexadecimal
            StringBuilder hexString = new StringBuilder();

            // Recorremos byte por byte el resultado encriptado para transformarlo en texto legible de letras y números
            for (byte b : hash) {
                // Convertimos el byte actual a su equivalente en formato hexadecimal
                String hex = Integer.toHexString(0xff & b);

                // Si el texto resultante tiene un solo dígito (ej. "5"), le agregamos un "0" a la izquierda ("05") para mantener el formato uniforme
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                // Añadimos el par hexadecimal al texto completo que estamos construyendo
                hexString.append(hex);
            }
            // Devolvemos la contraseña completamente transformada en una cadena fija de 64 caracteres indescifrables
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // Este bloque solo se ejecutaría en un caso sumamente extraño donde la instalación de Java no tenga el estándar SHA-256
            throw new RuntimeException("Error crítico: No se encontró el algoritmo SHA-256.", e);
        }
    }
}