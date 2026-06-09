/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.controlador;

import java.time.LocalDateTime;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import uv.lis.sistema_administracion_transacciones.logica.OperacionesBancariasServicio;
import uv.lis.sistema_administracion_transacciones.modelo.entidades.CuentaBancaria;
import uv.lis.sistema_administracion_transacciones.modelo.entidades.Transaccion;
import uv.lis.sistema_administracion_transacciones.modelo.excepciones.SaldoInsuficienteException;
import uv.lis.sistema_administracion_transacciones.modelo.excepciones.TransaccionFallidaException;
import uv.lis.sistema_administracion_transacciones.modelo.repositorio.CuentaRepositorio;
import uv.lis.sistema_administracion_transacciones.modelo.repositorio.TransaccionesRepositorio;

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
    private final CuentaRepositorio cuentaRepo = new CuentaRepositorio();
    private final TransaccionesRepositorio transaccionRepo = new TransaccionesRepositorio();

    @FXML
    public void initialize() {
        if (cmbTipoOperacion != null) {
            cmbTipoOperacion.getItems().addAll("Depósito", "Retiro", "Transferencia");
        }
        
        txtCuentaDestino.setDisable(true);
        
        cmbTipoOperacion.getSelectionModel().selectedItemProperty().addListener((obs, 
                viejo, nuevo) -> {
            if (nuevo != null) {
                if (nuevo.equals("Transferencia")) {
                    txtCuentaDestino.setDisable(false);
                    txtCuentaDestino.setPromptText("Ingresa cuenta destino");
                } else {
                    txtCuentaDestino.clear();
                    txtCuentaDestino.setDisable(true);
                    txtCuentaDestino.setPromptText("No aplica para " + nuevo.toLowerCase());
                }
            }
        });
    }

    @FXML
    private void controlarEjecutar() {
        String cuentaOrigenTexto = txtCuentaOrigen.getText().trim();
        String montoTexto = txtMonto.getText().trim();
        String cuentaDestinoTexto = txtCuentaDestino.getText().trim();
        String operacion = cmbTipoOperacion.getValue();

        if (cuentaOrigenTexto.isEmpty() || montoTexto.isEmpty() || operacion == null) {
            mostrarAlerta("Validación", "Por favor, llene todos los campos requeridos.", AlertType.WARNING);
        } else if (operacion.equals("Transferencia") && cuentaDestinoTexto.isEmpty()) {
            mostrarAlerta("Validación", "Para realizar una transferencia, "
                    + "debe ingresar la cuenta destino.", AlertType.WARNING);
        } else if (operacion.equals("Transferencia") && cuentaDestinoTexto.isEmpty()) {
            mostrarAlerta("Error lógico", "No se puede realizar una transferencia "
                    + "hacia la misma cuenta de origen.", AlertType.ERROR);
        } else {
            
            try {
                double monto = Double.parseDouble(montoTexto);
                
                CuentaBancaria cuentaOrigen = cuentaRepo.buscarPorId(cuentaOrigenTexto);
                
                String idTransaccion = "TX-" + System.currentTimeMillis();
                LocalDateTime ahora = LocalDateTime.now();
                
                
            if (operacion.equals("Retiro")) {
                operacionesService.realizarRetiro(cuentaOrigen, monto);
                cuentaRepo.actualizar(cuentaOrigen);
                
                Transaccion registro = new Transaccion.Builder(idTransaccion, monto, 
               ahora, "Retiro", cuentaOrigen, null).build();
                transaccionRepo.guardar(registro);
                
                mostrarAlerta("Transacción Completa", "Retiro realizado con éxito",
                        AlertType.INFORMATION);
                controlarLimpiar();
            } else if (operacion.equals("Depósito")) {
                operacionesService.realizarDeposito(cuentaOrigen, monto);
                cuentaRepo.actualizar(cuentaOrigen);
                
                Transaccion registro = new Transaccion.Builder(idTransaccion, monto, 
                        ahora, "Depósito", cuentaOrigen, null).build();
                transaccionRepo.guardar(registro);
                
                mostrarAlerta("Transacción Completa", "Depósito procesado con éxito.", 
                        AlertType.INFORMATION);
                controlarLimpiar();
            } else if (operacion.equals("Transferencia")) {
                CuentaBancaria cuentaDestino = cuentaRepo.buscarPorId(cuentaDestinoTexto);
                
                operacionesService.realizarRetiro(cuentaOrigen, monto);
                operacionesService.realizarDeposito(cuentaDestino, monto);
                
                cuentaRepo.actualizar(cuentaOrigen);
                cuentaRepo.actualizar(cuentaDestino);
                
                Transaccion registro = new Transaccion.Builder(idTransaccion, monto, 
                        ahora, "Transferencia", cuentaOrigen, null)
                            .cuentaDestino(cuentaDestino)
                            .build();
                    transaccionRepo.guardar(registro);
                
                mostrarAlerta("Transacción completo", "Transferencia realizada con "
                        + "éxito", AlertType.INFORMATION);
                controlarLimpiar();
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Formato Incorrecto", "El monto ingresado no es un número válido.", AlertType.ERROR);
        } catch (SaldoInsuficienteException | TransaccionFallidaException ex) {
            mostrarAlerta("Operación denegada", "Fondos insificientes o monto inválido "
                    + "para procesar la transacción.", AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Problema con la transacción. Verifique que las cuentas "
                    + "ingresadas existan en el sistema.", AlertType.ERROR);
        }
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