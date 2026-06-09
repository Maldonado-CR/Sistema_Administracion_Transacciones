/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import uv.lis.sistema_administracion_transacciones.logica.OperacionesBancariasServicio;

/**
 *
 * @author Maria Jose
 */
public class TransaccionesController {

    @FXML
    private TextField txtCuentaOrigen;
    @FXML
    private TextField txtMonto;
    @FXML
    private TextField txtCuentaDestino;
    @FXML
    private ComboBox<String> cmbTipoOperacion;
    @FXML
    private Label lblSaldoInformativo;

    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnEjecutar;

    private final OperacionesBancariasServicio operacionesService = new OperacionesBancariasServicio();

    @FXML
    public void initialize() {
        if (cmbTipoOperacion != null) {
            cmbTipoOperacion.getItems().addAll("Depósito", "Retiro", "Transferencia");
        }
    }

    @FXML
    private void controlarEjecutar() {
        String cuenta = txtCuentaOrigen.getText().trim();
        String montoTexto = txtMonto.getText().trim();
        String operacion = cmbTipoOperacion.getValue();

        if (cuenta.isEmpty() || montoTexto.isEmpty() || operacion == null) {
            mostrarAlerta("Validación", "Por favor, llene todos los campos requeridos.", AlertType.WARNING);
            return;
        }

        try {
            double monto = Double.parseDouble(montoTexto);
            if (monto <= 0) {
                mostrarAlerta("Error de datos", "El monto ingresado debe ser un número positivo.", AlertType.ERROR);
                return;
            }
            
            mostrarAlerta("Transacción Completa", "Operación de " + operacion + " realizada con éxito.", AlertType.INFORMATION);
            controlarLimpiar();

        } catch (NumberFormatException e) {
            mostrarAlerta("Formato Incorrecto", "El monto ingresado no es un número válido.", AlertType.ERROR);
        }
    }

    @FXML
    private void controlarLimpiar() {
        txtCuentaOrigen.clear();
        txtMonto.clear();
        txtCuentaDestino.clear();
        cmbTipoOperacion.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}