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
import java.util.ArrayList;
import java.util.List;
import uv.lis.sistema_administracion_transacciones.modelo.entidades.Sucursal;
import uv.lis.sistema_administracion_transacciones.modelo.excepciones.SucursalDuplicadaException;
import uv.lis.sistema_administracion_transacciones.modelo.excepciones.SucursalNoEncontradaException;

/**
 *
 * @author cinth
 */
public class SucursalRepositorio implements IRepositorio<Sucursal> {

    private final String RUTA_ARCHIVO = "sucursal_data.txt";

    public SucursalRepositorio() {
        File archivo = new File(RUTA_ARCHIVO);
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error crítico: No se pudo inicializar el archivo de sucursales", ex);
        }
    }

    @Override
    public void guardar(Sucursal entidad) throws Exception {
        if (buscarPorId(entidad.getNumeroIdentificacion()) != null) {
            throw new SucursalDuplicadaException("La sucursal con ID " + entidad.getNumeroIdentificacion() + " ya existe.");
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(RUTA_ARCHIVO, true), StandardCharsets.UTF_8))) {
            
            String linea = convertirEntidadALinea(entidad);
            bufferedWriter.write(linea);
            bufferedWriter.newLine();
        } catch (IOException ex) {
            throw new Exception("Error al intentar guardar la sucursal en el dispositivo físico.", ex);
        }
    }

    @Override
    public List<Sucursal> obtenerTodos() throws Exception {
        List<Sucursal> sucursales = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return sucursales;

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(RUTA_ARCHIVO), StandardCharsets.UTF_8))) {
            
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                
                String[] datos = linea.split(",");
                if (datos.length >= 6) {
                    String personaContacto = (datos.length == 7) ? datos[6] : "No Asignada";
                    Sucursal s = new Sucursal.Builder(datos[0], datos[1], datos[2])
                            .telefono(datos[3])
                            .correoElectronico(datos[4])
                            .nombreGerente(datos[5])
                            .personaContacto(personaContacto)
                            .build();
                    sucursales.add(s);
                }
            }
        } catch (IOException ex) {
            throw new Exception("Error al leer el archivo físico de sucursales.", ex);
        }
        return sucursales;
    }

    @Override
    public Sucursal buscarPorId(String id) throws Exception {
        List<Sucursal> todas = obtenerTodos();
        for (Sucursal s : todas) {
            if (s.getNumeroIdentificacion().equalsIgnoreCase(id.trim())) {
                return s;
            }
        }
        return null;
    }

    @Override
    public void actualizar(Sucursal entidad) throws Exception {
        List<Sucursal> listaSucursales = obtenerTodos();
        boolean encontrado = false;

        for (int i = 0; i < listaSucursales.size(); i++) {
            if (listaSucursales.get(i).getNumeroIdentificacion().equalsIgnoreCase(entidad.getNumeroIdentificacion())) {
                listaSucursales.set(i, entidad);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new SucursalNoEncontradaException("No se puede actualizar. Sucursal no registrada.");
        }

        reescribirArchivo(listaSucursales);
    }

    @Override
    public void eliminar(String id) throws Exception {
        List<Sucursal> listaSucursales = obtenerTodos();
        boolean eliminado = listaSucursales.removeIf(s -> s.getNumeroIdentificacion().equalsIgnoreCase(id.trim()));

        if (!eliminado) {
            throw new SucursalNoEncontradaException("No se puede eliminar. La sucursal no existe.");
        }

        reescribirArchivo(listaSucursales);
    }

    private void reescribirArchivo(List<Sucursal> lista) throws Exception {
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(RUTA_ARCHIVO, false), StandardCharsets.UTF_8))) {
            
            for (Sucursal s : lista) {
                bufferedWriter.write(convertirEntidadALinea(s));
                bufferedWriter.newLine();
            }
        } catch (IOException ex) {
            throw new Exception("Error crítico al reescribir el almacenamiento de sucursales.", ex);
        }
    }

    private String convertirEntidadALinea(Sucursal s) {
        return s.getNumeroIdentificacion() + "," +
               s.getNombreSucursal() + "," +
               s.getDireccionSucursal() + "," +
               s.getTelefono() + "," +
               s.getCorreoElectronico() + "," +
               s.getNombreGerente() + "," +
               s.getPersonaContacto();
    }
}