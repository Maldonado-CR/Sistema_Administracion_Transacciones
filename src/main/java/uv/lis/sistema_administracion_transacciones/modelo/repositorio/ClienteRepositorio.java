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
import uv.lis.sistema_administracion_transacciones.modelo.entidades.Cliente;
import uv.lis.sistema_administracion_transacciones.modelo.excepciones.ClienteNoEncontradoException;

/**
 *
 * @author Maria Jose
 */
public class ClienteRepositorio implements IRepositorio<Cliente> {

    private final String RUTA_ARCHIVO = "cliente_data.txt";

    public ClienteRepositorio() {
        File archivo = new File(RUTA_ARCHIVO);
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error crítico: No se pudo inicializar el archivo de clientes", ex);
        }
    }

    @Override
    public void guardar(Cliente entidad) throws Exception {
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(RUTA_ARCHIVO, true), StandardCharsets.UTF_8))) {
            
            String nal = (entidad.getNacionalidadCliente() == null || entidad.getNacionalidadCliente().isEmpty()) ? "Mexicana" : entidad.getNacionalidadCliente();
            String dir = (entidad.getDireccionCliente() == null || entidad.getDireccionCliente().isEmpty()) ? "Conocida" : entidad.getDireccionCliente();
            String tel = (entidad.getTelefonoCliente() == null || entidad.getTelefonoCliente().isEmpty()) ? "SinTelefono" : entidad.getTelefonoCliente();
            String cor = (entidad.getCorreoElectronico() == null || entidad.getCorreoElectronico().isEmpty()) ? "SinCorreo" : entidad.getCorreoElectronico();
            
            String linea = entidad.getRfcCurp() + "," +
                           entidad.getNombreCliente() + "," +
                           entidad.getApellidosCliente() + "," +
                           nal + "," +
                           entidad.getFechaNacimiento() + "," +
                           dir + "," +
                           tel + "," +
                           cor;
            
            bufferedWriter.write(linea);
            bufferedWriter.newLine();
        } catch (IOException ex) {
            throw new Exception("Error al intentar guardar al cliente en el archivo.", ex);
        }
    }

    @Override
    public List<Cliente> obtenerTodos() throws Exception {
        List<Cliente> clientes = new ArrayList<>();
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) return clientes;

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(new FileInputStream(RUTA_ARCHIVO), StandardCharsets.UTF_8))) {
            
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                if (linea == null || linea.trim().isEmpty()) {
                    continue;
                }
                
                String[] datos = linea.split(",");
                if (datos.length == 8) {
                    Cliente nuevoCliente = new Cliente.Builder(datos[0])
                            .nombreCliente(datos[1])
                            .apellidosCliente(datos[2])
                            .nacionalidadCliente(datos[3])
                            .fechaNacimiento(LocalDate.parse(datos[4]))
                            .direccionCliente(datos[5])
                            .telefonoCliente(datos[6])
                            .correoElectronico(datos[7])
                            .buil();
                    clientes.add(nuevoCliente);
                }
            }
        } catch (IOException ex) {
            throw new Exception("Error al leer el archivo de clientes.", ex);
        }
        return clientes;
    }

    @Override
    public Cliente buscarPorId(String id) throws Exception {
        List<Cliente> todosLosClientes = obtenerTodos();
        for (Cliente cliente : todosLosClientes) {
            if (cliente.getRfcCurp().equalsIgnoreCase(id)) {
                return cliente;
            }
        }
        throw new ClienteNoEncontradoException("No se encontró ningún cliente registrado con el RFC/CURP: " + id);
    }

    @Override
    public void actualizar(Cliente entidad) throws Exception {
        List<Cliente> clientes = obtenerTodos();
        boolean encontrado = false;

        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getRfcCurp().equalsIgnoreCase(entidad.getRfcCurp())) {
                clientes.set(i, entidad);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new ClienteNoEncontradoException("No se puede actualizar: El cliente no existe.");
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(RUTA_ARCHIVO, false), StandardCharsets.UTF_8))) {
            
            for (Cliente c : clientes) {
                String nal = (c.getNacionalidadCliente() == null || c.getNacionalidadCliente().isEmpty()) ? "Mexicana" : c.getNacionalidadCliente();
                String dir = (c.getDireccionCliente() == null || c.getDireccionCliente().isEmpty()) ? "Conocida" : c.getDireccionCliente();
                String tel = (c.getTelefonoCliente() == null || c.getTelefonoCliente().isEmpty()) ? "SinTelefono" : c.getTelefonoCliente();
                String cor = (c.getCorreoElectronico() == null || c.getCorreoElectronico().isEmpty()) ? "SinCorreo" : c.getCorreoElectronico();

                String linea = c.getRfcCurp() + "," + 
                               c.getNombreCliente() + "," +
                               c.getApellidosCliente() + "," + 
                               nal + "," + 
                               c.getFechaNacimiento().toString() + "," +
                               dir + "," + 
                               tel + "," + 
                               cor;
                bufferedWriter.write(linea);
                bufferedWriter.newLine();
            }
        } catch (IOException ex) {
            throw new Exception("Error al actualizar el archivo de clientes.", ex);
        }
    }

    @Override
    public void eliminar(String id) throws Exception {
        List<Cliente> clientes = obtenerTodos();
        boolean encontrado = false;

        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getRfcCurp().equalsIgnoreCase(id)) {
                clientes.remove(i);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new ClienteNoEncontradoException("No se puede eliminar: El cliente no existe.");
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(RUTA_ARCHIVO, false), StandardCharsets.UTF_8))) {
            
            for (Cliente c : clientes) {
                String nal = (c.getNacionalidadCliente() == null || c.getNacionalidadCliente().isEmpty()) ? "Mexicana" : c.getNacionalidadCliente();
                String dir = (c.getDireccionCliente() == null || c.getDireccionCliente().isEmpty()) ? "Conocida" : c.getDireccionCliente();
                String tel = (c.getTelefonoCliente() == null || c.getTelefonoCliente().isEmpty()) ? "SinTelefono" : c.getTelefonoCliente();
                String cor = (c.getCorreoElectronico() == null || c.getCorreoElectronico().isEmpty()) ? "SinCorreo" : c.getCorreoElectronico();

                String linea = c.getRfcCurp() + "," + 
                               c.getNombreCliente() + "," +
                               c.getApellidosCliente() + "," + 
                               nal + "," + 
                               c.getFechaNacimiento().toString() + "," +
                               dir + "," + 
                               tel + "," + 
                               cor;
                bufferedWriter.write(linea);
                bufferedWriter.newLine();
            }
        } catch (IOException ex) {
            throw new Exception("Error al actualizar el archivo de clientes tras la eliminación.", ex);
        }
    }
}