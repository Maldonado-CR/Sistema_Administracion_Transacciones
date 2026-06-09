/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.modelo.entidades;

import java.time.LocalDateTime;

/**
 *
 * @author Maria Jose
 */
public class Transaccion {
    private final String idUnico;
    private final double monto;
    private final LocalDateTime fechaHora;
    private final String tipo;
    private final CuentaBancaria cuentaOrigen;
    private final Sucursal sucursal;
    
    private final CuentaBancaria cuentaDestino;
    
    private Transaccion(Builder builder) {
        this.idUnico = builder.idUnico;
        this.monto = builder.monto;
        this.fechaHora = builder.fechaHora;
        this.tipo = builder.tipo;
        this.cuentaOrigen = builder.cuentaOrigen;
        this.sucursal = builder.sucursal;
        this.cuentaDestino = builder.cuentaDestino;
    }
    
    public static class Builder {
        private final String idUnico;
        private final double monto;
        private final LocalDateTime fechaHora;
        private final String tipo;
        private final CuentaBancaria cuentaOrigen;
        private final Sucursal sucursal;
        
        private CuentaBancaria cuentaDestino;
        
        public Builder(String idUnico, double monto, LocalDateTime fechaHora, 
                String tipo, CuentaBancaria cuentaOrigen, Sucursal sucursal) {
            this.idUnico = idUnico;
            this.monto = monto;
            this.fechaHora = fechaHora;
            this.tipo = tipo;
            this.cuentaOrigen = cuentaOrigen;
            this.sucursal = sucursal;
        }
        
        public Builder cuentaDestino(CuentaBancaria cuentaDestino) {
            this.cuentaDestino = cuentaDestino;
            return this;
        }
        
        public Transaccion build() {
            return new Transaccion(this);
        }
    }

    public String getIdUnico() {
        return idUnico;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getTipo() {
        return tipo;
    }

    public CuentaBancaria getCuentaOrigen() {
        return cuentaOrigen;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public CuentaBancaria getCuentaDestino() {
        return cuentaDestino;
    }
}
