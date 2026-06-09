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
public class DatosPersonales {
    private final String nombre;
    private final String direccion;
    private final LocalDate fechaNacimiento;
    private final String genero;
    
    public DatosPersonales(String nombre, String direccion, LocalDate fechaNacimiento, 
            String genero) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getGenero() {
        return genero;
    }
}
