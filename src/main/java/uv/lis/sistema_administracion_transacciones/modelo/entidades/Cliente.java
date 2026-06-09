/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.modelo.entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Maria Jose
 */
public class Cliente {
    private final String rfcCurp;
    private final String nombreCliente;
    private final String apellidosCliente;
    private String nacionalidadCliente;
    private final LocalDate fechaNacimiento;
    private String direccionCliente;
    private String telefonoCliente;
    private String correoElectronico;
    
    private List<CuentaBancaria> cuentasAsociadas;
    
    private Cliente(Builder builder) {
        this.rfcCurp = builder.rfcCurp;
        this.nombreCliente = builder.nombreCliente;
        this.apellidosCliente = builder.apellidosCliente;
        this.nacionalidadCliente = builder.nacionalidadCliente;
        this.fechaNacimiento = builder.fechaNacimiento;
        this.direccionCliente = builder.direccionCliente;
        this.telefonoCliente = builder.telefonoCliente;
        this.correoElectronico = builder.correoElectronico;
        this.cuentasAsociadas = new ArrayList<>();
    }
    
    public static class Builder{
        private String rfcCurp;
        private String nombreCliente;
        private String apellidosCliente;
        private String nacionalidadCliente;
        private LocalDate fechaNacimiento;
        private String direccionCliente;
        private String telefonoCliente;
        private String correoElectronico;
        
        public Builder(String rfcCurp) {
            this.rfcCurp = rfcCurp;
        }
        
        public Builder nombreCliente(String nombreCliente) {
            this.nombreCliente = nombreCliente;
            return this;
        }
        
        public Builder apellidosCliente(String apellidosCliente) {
            this.apellidosCliente = apellidosCliente;
            return this;
        }
        
        public Builder nacionalidadCliente(String nacionalidadCliente) {
            this.nacionalidadCliente = nacionalidadCliente;
            return this;
        }
        
        public Builder fechaNacimiento(LocalDate fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }
        
        public Builder direccionCliente(String direccionCliente) {
            this.direccionCliente = direccionCliente;
            return this;
        }
        
        public Builder telefonoCliente(String telefonoCliente) {
            this.telefonoCliente = telefonoCliente;
            return this;
        }
        
        public Builder correoElectronico(String correoElectronico) {
            this.correoElectronico = correoElectronico;
            return this;
        }
        
        public Cliente buil() {
            return new Cliente(this);
        }
    }
    
    public void agregarCuenta(CuentaBancaria nuevaCuenta) {
        this.cuentasAsociadas.add(nuevaCuenta);
    }

    public String getRfcCurp() {
        return rfcCurp;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getApellidosCliente() {
        return apellidosCliente;
    }

    public String getNacionalidadCliente() {
        return nacionalidadCliente;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public List<CuentaBancaria> getCuentasAsociadas() {
        return cuentasAsociadas;
    }
}
