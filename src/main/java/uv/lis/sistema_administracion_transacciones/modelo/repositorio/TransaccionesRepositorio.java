/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.modelo.repositorio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import uv.lis.sistema_administracion_transacciones.modelo.entidades.Transaccion;

/**
 *
 * @author Maria Jose
 */
public class TransaccionesRepositorio {
    private final String RUTA_ARCHIVO = "transacciones_data.txt";
    
    public TransaccionesRepositorio() {
    }
    
    private void asegurarArchivoExiste() throws Exception {
        File archivo = new File(RUTA_ARCHIVO);
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        } catch (IOException ex) {
            throw new Exception("Error de persistencia: No se pudo verificar el "
                    + "archivo de transacciones.", ex);
        }
    }
    
    public void guardar(Transaccion transaccion) throws Exception {
        asegurarArchivoExiste();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            String cuentaDestinoId = (transaccion.getCuentaDestino() != null) ? 
                    transaccion.getCuentaDestino().getNumeroCuenta() : "N/A";
            
            String linea = transaccion.getIdUnico() + "," +
                           transaccion.getMonto() + "," +
                           transaccion.getFechaHora().toString() + "," + 
                           transaccion.getTipo() + "," +
                           transaccion.getCuentaOrigen().getNumeroCuenta() + "," +
                           cuentaDestinoId;
            bufferedWriter.write(linea);
            bufferedWriter.newLine();
        } catch (IOException ex) {
            throw new Exception("Error al escribir el registro en el historial "
                    + "de transacciones.", ex);
        }
    }
}
