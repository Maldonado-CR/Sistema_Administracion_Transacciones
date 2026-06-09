/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.logica;

import uv.lis.sistema_administracion_transacciones.modelo.entidades.CuentaBancaria;
import uv.lis.sistema_administracion_transacciones.modelo.entidades.TipoCuenta;
import uv.lis.sistema_administracion_transacciones.modelo.excepciones.SaldoInsuficienteException;
import uv.lis.sistema_administracion_transacciones.modelo.excepciones.TransaccionFallidaException;

/**
 *
 * @author Maria Jose
 */
public class OperacionesBancariasServicio {

    public void realizarRetiro(CuentaBancaria cuenta, double monto) throws SaldoInsuficienteException, TransaccionFallidaException {
        if (monto <= 0) {
            throw new TransaccionFallidaException("El monto a retirar debe ser mayor a cero.");
        }
        
        double fondosDisponibles = cuenta.getSaldoActual();
        
        if (cuenta.getTipo() == TipoCuenta.EMPRESARIAL || cuenta.getTipo() == TipoCuenta.CORRIENTE) {
            fondosDisponibles += cuenta.getLimiteCredito();
        }

        if (monto > fondosDisponibles) {
            throw new SaldoInsuficienteException("Fondos insuficientes para realizar la operación de retiro.");
        }

        cuenta.setSaldoActual(cuenta.getSaldoActual() - monto);
    }

    public void realizarDeposito(CuentaBancaria cuenta, double monto) throws TransaccionFallidaException {
        if (monto <= 0) {
            throw new TransaccionFallidaException("El monto a depositar debe ser mayor a cero.");
        }
        cuenta.setSaldoActual(cuenta.getSaldoActual() + monto);
    }
}