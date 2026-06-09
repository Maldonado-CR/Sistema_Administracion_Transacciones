/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.modelo.repositorio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
            throw new RuntimeException("Error crítico: No se pudo inicializar "
                    + "el archivo de clientes", ex);
        }
    }

    @Override
    public void guardar(Cliente entidad) throws Exception {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(RUTA_ARCHIVO))) {
            String linea = entidad.getRfcCurp() + "," +
                    entidad.getNombreCliente() + "," +
                    entidad.getApellidosCliente() + "," +
                    entidad.getNacionalidadCliente() + "," +
                    entidad.getFechaNacimiento() + "," +
                    entidad.getDireccionCliente() + "," + 
                    entidad.getTelefonoCliente() + "," + 
                    entidad.getCorreoElectronico();
            
            bufferedWriter.write(linea);
            bufferedWriter.newLine();
        } catch (IOException ex) {
            throw new Exception("Error al intentar guardar al cliente en el "
                    + "archivo.", ex);
        }
    }

    @Override
    public List<Cliente> obtenerTodos() throws Exception {
        List<Cliente> clientes = new ArrayList<>();
        
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 8) {
                    Cliente nuevoCliente = new Cliente.Builder(datos[0]).nombreCliente(datos[1])
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
            if (cliente.getRfcCurp().equals(id)) {
                return cliente;
            }
        }
        
        throw new ClienteNoEncontradoException("No se encontró ningún cliente "
                + "registrado con el RFC/CURP: " + id);
    }

    @Override
    public void actualizar(Cliente entidad) throws Exception {
        List<Cliente> clientes = obtenerTodos();
        boolean encontrado = false;
        
        for (int i = 0; i < clientes.size(); i ++) {
            if (clientes.get(i).getRfcCurp().equals(entidad.getRfcCurp())) {
                clientes.set(i, entidad);
                encontrado = true;
                break;
            }
        }
        
        if (!encontrado) {
            throw new ClienteNoEncontradoException("No se puede actualizar: "
                    + "El cliente no existe.");
        }
        
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, false))) {
            for (Cliente c : clientes) {
                String linea = c.getRfcCurp() + "," + c.getNombreCliente() + "," +
                        c.getApellidosCliente() + "," + c.getNacionalidadCliente() + 
                        "," + c.getFechaNacimiento().toString() + "," + 
                        c.getDireccionCliente() + "," + c.getTelefonoCliente() + 
                        "," + c.getCorreoElectronico();
            }
        } catch (IOException ex) {
            throw new Exception("Error al actualizar el archivo de clientes.", ex);
        }
    }
    
}
 