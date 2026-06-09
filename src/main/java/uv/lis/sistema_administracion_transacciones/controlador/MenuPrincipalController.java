/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.controlador;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.Empleado;

/**
 *
 * @author Maria Jose
 */
public class MenuPrincipalController {

    @FXML
    private VBox contenedorPrincipal;
    @FXML
    private Button btnClientes;
    @FXML
    private Button btnTransacciones;
    @FXML
    private Button btnEmpleados;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private Button btnSucursales;
    @FXML
    private Button btnCuentas;
    
    private static Empleado empleadoAutenticado;

    public void inicializarPermisosRol(Empleado empleado) {
        if (empleado == null) {
            return;
        }

        empleadoAutenticado = empleado;

        String rol = empleado.obtenerRol();

        btnEmpleados.setDisable(false);
        btnClientes.setDisable(false);
        btnTransacciones.setDisable(false);
        btnSucursales.setDisable(false);
        btnCuentas.setDisable(false);

        if (rol.equalsIgnoreCase("Administrador")) {
            btnClientes.setDisable(true);
            btnTransacciones.setDisable(true);
            btnSucursales.setDisable(true);
            btnCuentas.setDisable(true);
            
        } else if (rol.equalsIgnoreCase("Gerente")) {
            btnEmpleados.setDisable(true);
            btnClientes.setDisable(true);
            btnCuentas.setDisable(true);
            btnTransacciones.setDisable(true);
            
        } else if (rol.equalsIgnoreCase("Ejecutivo de Cuenta") || rol.equalsIgnoreCase("Ejecutivo")) {
            btnEmpleados.setDisable(true);
            btnSucursales.setDisable(true);
            btnTransacciones.setDisable(true);
            
        } else if (rol.equalsIgnoreCase("Cajero")) {
            btnEmpleados.setDisable(true);
            btnClientes.setDisable(true);
            btnSucursales.setDisable(true);
            btnCuentas.setDisable(true);
        }
    }

    @FXML
    private void mostrarSeccionClientes() {
        cargarVistaSecundaria("/fxml/vista/ClientesView.fxml");
    }
    @FXML
    private void mostrarSeccionCuentas() {
        cargarVistaSecundaria("/fxml/vista/CuentasView.fxml");
    }

    @FXML
    private void mostrarSeccionSucursales() {
        cargarVistaSecundaria("/fxml/vista/SucursalesView.fxml");
    }

    @FXML
    private void mostrarSeccionTransacciones() {
        cargarVistaSecundaria("/fxml/vista/TransaccionesView.fxml");
    }

    @FXML
    private void mostrarSeccionEmpleados() {
        cargarVistaSecundaria("/fxml/vista/EmpleadosView.fxml");
    }

    private void cargarVistaSecundaria(String rutaFxml) {
        try {
            contenedorPrincipal.getChildren().clear();
            Parent vista = FXMLLoader.load(getClass().getResource(rutaFxml));
            contenedorPrincipal.getChildren().add(vista);
        } catch (IOException e) {
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error de Carga");
            alerta.setContentText("No se pudo cargar la sección solicitada.");
            alerta.showAndWait();
        }
    }

    @FXML
    private void controlarCerrarSesion() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/vista/LoginView.fxml"));
            Stage escenarioLogin = new Stage();
            escenarioLogin.setTitle("EuroBank - Iniciar Sesión");
            escenarioLogin.setScene(new Scene(root));
            escenarioLogin.setResizable(false);
            escenarioLogin.show();

            Stage escenarioActual = (Stage) btnCerrarSesion.getScene().getWindow();
            escenarioActual.close();
        } catch (IOException e) {
            System.out.println("Error al regresar al login.");
        }
    }
    
    public static Empleado getEmpleadoAutenticado() {
        return empleadoAutenticado;
    }
}