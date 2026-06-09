/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.modelo.entidades;

import java.util.ArrayList;
import java.util.List;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.Empleado;

/**
 *
 * @author Maria Jose
 */
public class Sucursal {
    private final String numeroIdentificacion;
    private final String nombreSucursal;
    private final String direccionSucursal;
    
    private String telefono;
    private String correoElectronico;
    private String nombreGerente;
    private String personaContacto;
    
    private final List<CuentaBancaria> cuentas;
    private final List<Empleado> empleados;
    
    private Sucursal(Builder builder) {
        this.numeroIdentificacion = builder.numeroIdentificacion;
        this.nombreSucursal = builder.nombreSucursal;
        this.direccionSucursal = builder.direccionSucursal;
        this.telefono = builder.telefono;
        this.correoElectronico = builder.correoElectronico;
        this.nombreGerente = builder.nombreGerente;
        this.personaContacto = builder.personaContacto;
        
        this.cuentas = new ArrayList<>();
        this.empleados = new ArrayList<>();
    }
    
    public static class Builder {
        private final String numeroIdentificacion;
        private final String nombreSucursal;
        private final String direccionSucursal;
        
        private String telefono;
        private String correoElectronico;
        private String nombreGerente;
        private String personaContacto;
        
        public Builder(String numeroIdentificacion, String nombreSucursal, 
                String direccionSucursal) {
            this.numeroIdentificacion = numeroIdentificacion;
            this.nombreSucursal = nombreSucursal;
            this.direccionSucursal = direccionSucursal;
        }
        
        public Builder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }
        
        public Builder correoElectronico(String correoElectronico) {
            this.correoElectronico = correoElectronico;
            return this;
        }
        
        public Builder nombreGerente(String nombreGerente) {
            this.nombreGerente = nombreGerente;
            return this;
        }
        
        public Builder personaContacto(String personaContacto) {
            this.personaContacto = personaContacto;
            return this;
        }
        
        public Sucursal build() {
            return new Sucursal(this);
        }
    }
    
    public void agregarCuenta(CuentaBancaria nuevaCuenta) {
        this.cuentas.add(nuevaCuenta);
    }
    
    public void agregarEmpleado(Empleado nuevoEmpleado) {
        this.empleados.add(nuevoEmpleado);
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public String getDireccionSucursal() {
        return direccionSucursal;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getNombreGerente() {
        return nombreGerente;
    }

    public String getPersonaContacto() {
        return personaContacto;
    }

    public List<CuentaBancaria> getCuentas() {
        return cuentas;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void setNombreGerente(String nombreGerente) {
        this.nombreGerente = nombreGerente;
    }

    public void setPersonaContacto(String personaContacto) {
        this.personaContacto = personaContacto;
    }
    
}
