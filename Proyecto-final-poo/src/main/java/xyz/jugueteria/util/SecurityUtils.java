package xyz.jugueteria.util;

import java.security.MessageDigest;
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
        if (password == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error crítico: No se encontró el algoritmo SHA-256.", e);
        }
    }
}
