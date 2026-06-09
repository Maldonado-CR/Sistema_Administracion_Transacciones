/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.modelo.repositorio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.Cajero;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.Credenciales;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.DatosPersonales;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.EjecutivoCuenta;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.Empleado;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.Gerente;

/**
 *
 * @author cinth
 */
public class EmpleadoRepositorio implements IRepositorio<Empleado> {

    private final String RUTA_ARCHIVO = "empleado_data.txt";

    public EmpleadoRepositorio() {
        File archivo = new File(RUTA_ARCHIVO);
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
                inicializarAdministradorRaiz();
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error crítico: No se pudo inicializar el archivo de empleados", ex);
        }
    }

    private void inicializarAdministradorRaiz() {
        try {
            DatosPersonales datosAdmin = new DatosPersonales("Administrador General", "EuroBank HQ", LocalDate.of(1990, 1, 1), "M");
            Credenciales credencialesAdmin = new Credenciales("admin@eurobank.com", "admin123");
            
            Empleado adminRoot = new Empleado("EMP00", 30000.0, datosAdmin, credencialesAdmin) {
                @Override
                public String obtenerRol() {
                    return "Administrador";
                }
            };
            guardar(adminRoot);
        } catch (Exception e) {
            System.err.println("No se pudo crear el usuario administrador raíz.");
        }
    }

    @Override
    public void guardar(Empleado entidad) throws Exception {
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(RUTA_ARCHIVO, true), StandardCharsets.UTF_8))) {
            
            String rol = entidad.obtenerRol();
            String datosEspecificos = "";

            if (entidad instanceof Cajero) {
                Cajero c = (Cajero) entidad;
                datosEspecificos = c.getHorarioTrabajo() + ";" + c.getNumeroVentanilla();
            } else if (entidad instanceof EjecutivoCuenta) {
                EjecutivoCuenta e = (EjecutivoCuenta) entidad;
                datosEspecificos = e.getNumeroClientesAsignados() + ";" + e.getEspecializacion();
            } else if (entidad instanceof Gerente) {
                Gerente g = (Gerente) entidad;
                datosEspecificos = g.getNivelAcceso() + ";" + g.getAnosExperiencia();
            } else {
                datosEspecificos = "N/A;N/A";
            }

            String linea = entidad.getIdEmpleado() + "," +
                           entidad.getSalario() + "," +
                           entidad.getDatosPersonales().getNombre() + "," +
                           entidad.getDatosPersonales().getDireccion() + "," +
                           entidad.getDatosPersonales().getFechaNacimiento() + "," +
                           entidad.getDatosPersonales().getGenero() + "," +
                           entidad.getAccesos().getUsuario() + "," +
                           entidad.getAccesos().getContraseniaEncriptada() + "," +
                           rol + "," +
                           datosEspecificos;

            bufferedWriter.write(linea);
            bufferedWriter.newLine();
        } catch (IOException ex) {
            throw new Exception("Error al intentar guardar al empleado en el archivo.", ex);
        }
    }

    @Override
    public List<Empleado> obtenerTodos() throws Exception {
        List<Empleado> empleados = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return empleados;

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(RUTA_ARCHIVO), StandardCharsets.UTF_8))) {
            
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                if (linea == null || linea.trim().isEmpty()) continue;
                
                String[] datos = linea.split(",");
                if (datos.length == 10) {
                    String id = datos[0];
                    double salario = Double.parseDouble(datos[1]);
                    DatosPersonales dp = new DatosPersonales(datos[2], datos[3], LocalDate.parse(datos[4]), datos[5]);
                    
                    Credenciales cred = new Credenciales(datos[6], "");
                    java.lang.reflect.Field fieldHash = Credenciales.class.getDeclaredField("contraseniaEncriptada");
                    fieldHash.setAccessible(true);
                    fieldHash.set(cred, datos[7]);

                    String rol = datos[8];
                    String[] espec = datos[9].split(";");

                    Empleado emp;
                    if (rol.equalsIgnoreCase("Cajero")) {
                        emp = new Cajero(id, salario, dp, cred, espec[0], Integer.parseInt(espec[1]));
                    } else if (rol.equalsIgnoreCase("Ejecutivo de Cuenta") || rol.equalsIgnoreCase("Ejecutivo")) {
                        emp = new EjecutivoCuenta(id, salario, dp, cred, Integer.parseInt(espec[0]), espec[1]);
                    } else if (rol.equalsIgnoreCase("Gerente")) {
                        emp = new Gerente(id, salario, dp, cred, espec[0], Integer.parseInt(espec[1]));
                    } else {
                        emp = new Empleado(id, salario, dp, cred) {
                            @Override
                            public String obtenerRol() {
                                return "Administrador";
                            }
                        };
                    }
                    empleados.add(emp);
                }
            }
        } catch (IOException ex) {
            throw new Exception("Error al leer el archivo de empleados.", ex);
        }
        return empleados;
    }

    @Override
    public Empleado buscarPorId(String id) throws Exception {
        List<Empleado> todos = obtenerTodos();
        for (Empleado e : todos) {
            if (e.getAccesos().getUsuario().equalsIgnoreCase(id) || e.getIdEmpleado().equalsIgnoreCase(id)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public void actualizar(Empleado entidad) throws Exception {
        List<Empleado> empleados = obtenerTodos();
        boolean encontrado = false;

        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).getIdEmpleado().equalsIgnoreCase(entidad.getIdEmpleado())) {
                empleados.set(i, entidad);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new Exception("El empleado no existe.");
        }

        reescribirArchivo(empleados);
    }

    @Override
    public void eliminar(String id) throws Exception {
        List<Empleado> empleados = obtenerTodos();
        boolean encontrado = false;

        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).getIdEmpleado().equalsIgnoreCase(id)) {
                empleados.remove(i);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new Exception("El empleado no existe.");
        }

        reescribirArchivo(empleados);
    }

    private void reescribirArchivo(List<Empleado> lista) throws Exception {
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(RUTA_ARCHIVO, false), StandardCharsets.UTF_8))) {

            for (Empleado entidad : lista) {
                String rol = entidad.obtenerRol();
                String datosEspecificos = "";

                if (entidad instanceof Cajero) {
                    Cajero c = (Cajero) entidad;
                    datosEspecificos = c.getHorarioTrabajo() + ";" + c.getNumeroVentanilla();
                } else if (entidad instanceof EjecutivoCuenta) {
                    EjecutivoCuenta e = (EjecutivoCuenta) entidad;
                    datosEspecificos = e.getNumeroClientesAsignados() + ";" + e.getEspecializacion();
                } else if (entidad instanceof Gerente) {
                    Gerente g = (Gerente) entidad;
                    datosEspecificos = g.getNivelAcceso() + ";" + g.getAnosExperiencia();
                } else {
                    datosEspecificos = "N/A;N/A";
                }

                String linea = entidad.getIdEmpleado() + ","
                        + entidad.getSalario() + ","
                        + entidad.getDatosPersonales().getNombre() + ","
                        + entidad.getDatosPersonales().getDireccion() + ","
                        + entidad.getDatosPersonales().getFechaNacimiento() + ","
                        + entidad.getDatosPersonales().getGenero() + ","
                        + entidad.getAccesos().getUsuario() + ","
                        + entidad.getAccesos().getContraseniaEncriptada() + ","
                        + rol + ","
                        + datosEspecificos;

                bufferedWriter.write(linea);
                bufferedWriter.newLine();
            }
        } catch (IOException ex) {
            throw new Exception("Error al escribir en el archivo de empleados.", ex);
        }
    }
}
