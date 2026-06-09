/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.modelo.usuarios;

/**
 *
 * @author Maria Jose
 */
public class Cajero  extends Empleado{
    private String horarioTrabajo;
    private int numeroVentanilla;
    
    public Cajero(String idEmpleado, double salario, DatosPersonales datosPersonales, 
            Credenciales accesos, String horarioTrabajo, int numeroVentanilla) {
        super(idEmpleado, salario, datosPersonales, accesos);
        this.horarioTrabajo = horarioTrabajo;
        this.numeroVentanilla = numeroVentanilla;
    }

    @Override
    public String obtenerRol() {
        return "Cajero";
    }

    public String getHorarioTrabajo() {
        return horarioTrabajo;
    }

    public void setHorarioTrabajo(String horarioTrabajo) {
        this.horarioTrabajo = horarioTrabajo;
    }

    public int getNumeroVentanilla() {
        return numeroVentanilla;
    }

    public void setNumeroVentanilla(int numeroVentanilla) {
        this.numeroVentanilla = numeroVentanilla;
    }
}
