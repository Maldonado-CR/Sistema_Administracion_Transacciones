/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.modelo.entidades;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Maria Jose
 */
public class CuentaBancaria {
    private final String numeroCuenta;
    private final TipoCuenta tipo;
    private final Cliente clienteAsociado;
    
    private double saldoActual;
    private double limiteCredito;
    
    private final List<Tarjeta> listaTarjetas;
    
    private CuentaBancaria(Builder builder) {
        this.numeroCuenta = builder.numeroCuenta;
        this.tipo = builder.tipo;
        this.clienteAsociado = builder.clienteAsociado;
        this.saldoActual = builder.saldoActual;
        this.limiteCredito = builder.limiteCredito;
        this.listaTarjetas = new ArrayList<>();
    }
    
    public List<Tarjeta> getListaTarjetas() {
        return listaTarjetas;
    }
    
    public void asociarNuevaTarjeta(Tarjeta tarjeta) {
        if (tarjeta != null) {
            this.listaTarjetas.add(tarjeta);
        }
    }
    
    public static class Builder {
        private final String numeroCuenta;
        private final TipoCuenta tipo;
        private final Cliente clienteAsociado;
        
        private double saldoActual = 0.0;
        private double limiteCredito = 0.0;
        
        public Builder(String numeroCuenta, TipoCuenta tipo, Cliente clienteAsociado) {
            this.numeroCuenta = numeroCuenta;
            this.tipo = tipo;
            this.clienteAsociado = clienteAsociado;
        }
        
        public Builder saldoActual(double saldoActual) {
            this.saldoActual = saldoActual;
            return this;
        }
        
        public Builder limiteCredito(double limiteCredito) {
            this.limiteCredito = limiteCredito;
            return this;
        }
        
        public CuentaBancaria build() {
            return new CuentaBancaria(this);
        }
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public TipoCuenta getTipo() {
        return tipo;
    }

    public Cliente getClienteAsociado() {
        return clienteAsociado;
    }

    public double getSaldoActual() {
        return saldoActual;
    }

    public double getLimiteCredito() {
        return limiteCredito;
    }

    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }

    public void setLimiteCredito(double limiteCredito) {
        this.limiteCredito = limiteCredito;
    }
}
