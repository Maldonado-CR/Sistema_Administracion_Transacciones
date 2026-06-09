/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.modelo.entidades;

/**
 *
 * @author cinth
 */
public class Tarjeta {
    private final String numeroTarjeta;
    private final String tipoTarjeta; 
    private final String fechaVencimiento;
    private final String cvv;

    public Tarjeta(String numeroTarjeta, String tipoTarjeta, String fechaVencimiento, String cvv) {
        this.numeroTarjeta = numeroTarjeta;
        this.tipoTarjeta = tipoTarjeta;
        this.fechaVencimiento = fechaVencimiento;
        this.cvv = cvv;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getCvv() {
        return cvv;
    }
}