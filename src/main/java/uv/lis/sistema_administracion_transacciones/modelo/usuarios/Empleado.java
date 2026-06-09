/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.modelo.usuarios;

import java.time.LocalDate;

/**
 *
 * @author Maria Jose
 */
public abstract class Empleado {
    private final String idEmpleado;
    private double salario;
    private final DatosPersonales datosPersonales;
    private final Credenciales accesos;
    
    public Empleado(String idEmpleado, double salario, DatosPersonales datosPersonales, 
            Credenciales accesos) {
        this.idEmpleado = idEmpleado;
        this.salario = salario;
        this.datosPersonales = datosPersonales;
        this.accesos = accesos;
    }
    
    public abstract String obtenerRol();

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public double getSalario() {
        return salario;
    }

    public DatosPersonales getDatosPersonales() {
        return datosPersonales;
    }

    public Credenciales getAccesos() {
        return accesos;
    }
}
