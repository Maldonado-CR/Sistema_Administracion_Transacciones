/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author cinth
 */
public class SucursalesController {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtGerente;

    @FXML
    private TableView<Object> tablaSucursales;
    @FXML
    private TableColumn<Object, String> colId;
    @FXML
    private TableColumn<Object, String> colNombre;
    @FXML
    private TableColumn<Object, String> colDireccion;
    @FXML
    private TableColumn<Object, String> colTelefono;
    @FXML
    private TableColumn<Object, String> colGerente;

    @FXML
    public void initialize() {
        // Estructura lista para mapear propiedades cuando definan la entidad Sucursal
    }

    @FXML
    private void controlarAgregar() {
        mostrarAlerta("Infraestructura", "Módulo de Sucursales simulado. Registro omitido temporalmente.");
    }

    @FXML
    private void controlarEditar() {
        mostrarAlerta("Infraestructura", "Modificación procesada en memoria.");
    }

    @FXML
    private void controlarEliminar() {
        mostrarAlerta("Infraestructura", "Operación simulada con éxito.");
    }

    @FXML
    private void controlarLimpiar() {
        txtId.clear();
        txtNombre.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtGerente.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}