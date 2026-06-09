/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.modelo.usuarios;

/**
 *
 * @author Maria Jose
 */
public class Gerente extends Empleado {
    private String nivelAcceso;
    private int anosExperiencia;

    public Gerente(String idEmpleado, double salario, DatosPersonales datosPersonales, 
                   Credenciales accesos, String nivelAcceso, int anosExperiencia) {
        super(idEmpleado, salario, datosPersonales, accesos);
        this.nivelAcceso = nivelAcceso;
        this.anosExperiencia = anosExperiencia;
    }

    @Override
    public String obtenerRol() {
        return "Gerente";
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public int getAnosExperiencia() {
        return anosExperiencia;
    }

    public void setAnosExperiencia(int anosExperiencia) {
        this.anosExperiencia = anosExperiencia;
    }
}