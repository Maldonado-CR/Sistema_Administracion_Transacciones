/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.modelo.excepciones;

/**
 *
 * @author Maria Jose
 */
public class EncriptacionFallidaException extends Exception {
    
    public EncriptacionFallidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
}
