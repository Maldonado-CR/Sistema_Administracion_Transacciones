/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.modelo.usuarios;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import uv.lis.sistema_administracion_transacciones.modelo.excepciones.EncriptacionFallidaException;

/**
 *
 * @author Maria Jose
 */
public class Credenciales {
    private String usuario;
    private String contraseniaEncriptada;
    
    public Credenciales(String usuario, String contraseniaPlana) throws
            EncriptacionFallidaException {
        this.usuario = usuario;
        this.contraseniaEncriptada = encriptarSHA256(contraseniaPlana);
    }
    
    public String getUsuario() {
        return usuario;
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    
    public String getContraseniaEncriptada() {
        return contraseniaEncriptada;
    }
    
    public boolean validarContrasenia(String contraseniaPlana) throws 
            EncriptacionFallidaException {
        String hashInput = encriptarSHA256(contraseniaPlana);
        return this.contraseniaEncriptada.equals(hashInput);
    }
    
    private String encriptarSHA256(String contraseniaPlana) throws EncriptacionFallidaException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(contraseniaPlana.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new EncriptacionFallidaException("Error crítico: No se encontró el algoritmo "
                    + "SHA-256 para la seguridad.", ex);
        }
    }
}
