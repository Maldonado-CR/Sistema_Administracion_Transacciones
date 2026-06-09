/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.modelo.usuarios;

/**
 *
 * @author Maria Jose
 */
public class EjecutivoCuenta extends Empleado {
    private int numeroClientesAsignados;
    private String especializacion;
    
    public EjecutivoCuenta(String idEmpleado, double salario, DatosPersonales datosPersonales,
            Credenciales accesos, int numeroClientesAsignados, String especializacion) {
        super(idEmpleado, salario, datosPersonales, accesos);
        this.numeroClientesAsignados = numeroClientesAsignados;
        this.especializacion = especializacion;
    }

    @Override
    public String obtenerRol() {
        return "Ejecutivo de Cuenta";
    }

    public int getNumeroClientesAsignados() {
        return numeroClientesAsignados;
    }

    public void setNumeroClientesAsignados(int numeroClientesAsignados) {
        this.numeroClientesAsignados = numeroClientesAsignados;
    }

    public String getEspecializacion() {
        return especializacion;
    }

    public void setEspecializacion(String especializacion) {
        this.especializacion = especializacion;
    }
}
