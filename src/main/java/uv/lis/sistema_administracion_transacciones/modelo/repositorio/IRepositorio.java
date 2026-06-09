/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.modelo.repositorio;

import java.util.List;

/**
 *
 * @author Maria Jose
 */
public interface IRepositorio<T> {
    void guardar(T entidad) throws Exception;
    List<T> obtenerTodos() throws Exception;
    T buscarPorId(String id) throws Exception;
    void actualizar(T entidad) throws Exception;
    void eliminar(String id) throws Exception;
}
