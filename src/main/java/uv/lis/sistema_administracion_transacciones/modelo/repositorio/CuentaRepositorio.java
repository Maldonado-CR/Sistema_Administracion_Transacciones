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
import java.util.ArrayList;
import java.util.List;
import uv.lis.sistema_administracion_transacciones.modelo.entidades.Cliente;
import uv.lis.sistema_administracion_transacciones.modelo.entidades.CuentaBancaria;
import uv.lis.sistema_administracion_transacciones.modelo.entidades.TipoCuenta;

/**
 *
 * @author Maria Jose
 */
public class CuentaRepositorio implements IRepositorio<CuentaBancaria> {
    
    private final String RUTA_ARCHIVO = "cuentas_data.txt";
    private final ClienteRepositorio clienteRepositorio = new ClienteRepositorio();
    
    public CuentaRepositorio() {
    }
    
    private void asegurarArchivoExiste() throws Exception {
        File archivo = new File(RUTA_ARCHIVO);
        try {
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        } catch (IOException ex) {
            throw new Exception("Error de persistencia: No se pudo crear ni acceder "
                    + "al archivo de cuentas.", ex);
        }
    }

    @Override
    public void guardar(CuentaBancaria entidad) throws Exception {
        asegurarArchivoExiste();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, true))) {
            String linea = entidad.getNumeroCuenta() + "," + 
                           entidad.getTipo().name() + "," + 
                           entidad.getClienteAsociado().getRfcCurp() + "," + 
                           entidad.getSaldoActual() + "," + entidad.getLimiteCredito();
            bufferedWriter.write(linea);
            bufferedWriter.newLine();
        } catch (IOException ex) {
            throw new Exception("Error al escribir los datos de la cuenta en el "
                    + "archivo.", ex);
        } 
    }

    @Override
    public List<CuentaBancaria> obtenerTodos() throws Exception {
        asegurarArchivoExiste();
        List<CuentaBancaria> cuentas = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(RUTA_ARCHIVO))) {
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    Cliente clienteAsociado = clienteRepositorio.buscarPorId(datos[2]);
                    
                    CuentaBancaria cuenta = new CuentaBancaria.Builder(datos[0], 
                    TipoCuenta.valueOf(datos[1]), clienteAsociado)
                    .saldoActual(Double.parseDouble(datos[3]))
                    .limiteCredito(Double.parseDouble(datos[4]))
                    .build();
                cuentas.add(cuenta);
                }
            }
        } catch (IOException ex) {
            throw new Exception("Error al leer el archivo de cuentas.", ex);
        }
        
        return cuentas;
    }

    @Override
    public CuentaBancaria buscarPorId(String id) throws Exception {
        for (CuentaBancaria cuenta : obtenerTodos()) {
            if (cuenta.getNumeroCuenta().equals(id)) {
                return cuenta;
            }
        }
        
        throw new Exception("Cuenta no encontrada.");
    }

    @Override
    public void actualizar(CuentaBancaria entidad) throws Exception {
        List<CuentaBancaria> cuentas = obtenerTodos();
        boolean encontrado = false;
        
        for (int i = 0; i < cuentas.size(); i ++) {
            if (cuentas.get(i).getNumeroCuenta().equals(entidad.getNumeroCuenta())) {
                cuentas.set(i, entidad);
                encontrado = true;
                break;
            }
        }
        
        if (!encontrado) {
            throw new Exception("No se puede actualizar: La cuenta no existe.");
    }
    
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, false))) {
        for (CuentaBancaria c : cuentas) {
            bufferedWriter.write(c.getNumeroCuenta() + "," + c.getTipo().name() + 
            "," + c.getClienteAsociado().getRfcCurp() + "," + c.getSaldoActual() + 
            "," + c.getLimiteCredito());
        bufferedWriter.newLine();
    }
} catch (IOException ex) {
    throw new Exception("Error al reescribir el archivo durante la actualización.", ex);
        }
    }

        @Override
        public void eliminar(String id) throws Exception {
            List<CuentaBancaria> cuentas = obtenerTodos();
            boolean encontrado = false;
            
            for (int i = 0; i < cuentas.size(); i ++) {
                if (cuentas.get(i).getNumeroCuenta().equals(id)) {
                    cuentas.remove(i);
                    encontrado = true;
                    break;
                }
            }
            
            if (!encontrado)
                throw new Exception("La cuenta no existe.");
            
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(RUTA_ARCHIVO, false))) {
                for (CuentaBancaria c : cuentas) {
                    bufferedWriter.write(c.getNumeroCuenta() + "," + c.getTipo().name() 
                    + "," + c.getClienteAsociado().getRfcCurp() + "," + c.getSaldoActual() + 
                            "," + c.getLimiteCredito());
                    bufferedWriter.newLine();
                }
            }
        }
    }
