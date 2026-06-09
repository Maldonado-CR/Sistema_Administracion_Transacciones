/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.logica;

import java.util.List;
import uv.lis.sistema_administracion_transacciones.modelo.repositorio.EmpleadoRepositorio;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.Empleado;

/**
 *
 * @author Maria Jose
 */
public class AutenticacionServicio {
    private final EmpleadoRepositorio empleadoRepo;

    public AutenticacionServicio() {
        this.empleadoRepo = new EmpleadoRepositorio();
    }

    public Empleado autenticar(String correo, String contrasenia) {
        try {
            List<Empleado> listaEmpleados = empleadoRepo.obtenerTodos();
            for (Empleado emp : listaEmpleados) {
                if (emp.getAccesos().getUsuario().equalsIgnoreCase(correo)) {
                    if (emp.getAccesos().validarContrasenia(contrasenia)) {
                        return emp;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error durante el proceso de autenticación en el archivo plano: " + e.getMessage());
        }
        return null;
    }
}