/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.controlador;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import uv.lis.sistema_administracion_transacciones.modelo.entidades.Cliente;
import uv.lis.sistema_administracion_transacciones.modelo.entidades.CuentaBancaria;
import uv.lis.sistema_administracion_transacciones.modelo.entidades.TipoCuenta;
import uv.lis.sistema_administracion_transacciones.modelo.excepciones.ClienteNoEncontradoException;
import uv.lis.sistema_administracion_transacciones.modelo.repositorio.ClienteRepositorio;
import uv.lis.sistema_administracion_transacciones.modelo.repositorio.CuentaRepositorio;

/**
 *
 * @author Maria Jose
 */
public class CuentasController {
    @FXML private TextField txtNumeroCuenta;
    @FXML private ComboBox<TipoCuenta> comboTipo;
    @FXML private TextField txtRfcCliente;
    @FXML private TextField txtSaldoInicial;
    @FXML private TextField txtLimiteCredito;
    
    @FXML private TableView<CuentaBancaria> tablaCuentas;
    @FXML private TableColumn<CuentaBancaria, String> colNumeroCuenta;
    @FXML private TableColumn<CuentaBancaria, String> colTipo;
    @FXML private TableColumn<CuentaBancaria, String> colRfcCliente;
    @FXML private TableColumn<CuentaBancaria, String> colSaldo;
    @FXML private TableColumn<CuentaBancaria, String> colLimite;
    
    private final CuentaRepositorio cuentaRepo = new CuentaRepositorio();
    private final ClienteRepositorio clienteRepo = new ClienteRepositorio();
    private final ObservableList<CuentaBancaria> listaObservable = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        comboTipo.setItems(FXCollections.observableArrayList(TipoCuenta.values()));
        
        colNumeroCuenta.setCellValueFactory(new PropertyValueFactory<>("numeroCuenta"));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldoActual"));
        colLimite.setCellValueFactory(new PropertyValueFactory<>("limiteCredito"));
        
        colTipo.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getTipo().name()));
        colRfcCliente.setCellValueFactory(cellData -> new SimpleStringProperty(
        cellData.getValue().getClienteAsociado().getRfcCurp()));
        
        comboTipo.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
        if (nuevo == TipoCuenta.AHORROS) {
            txtLimiteCredito.setText("0.0");
            txtLimiteCredito.setDisable(true);
        } else {
            txtLimiteCredito.setDisable(false);
            }
        });
        
        vincularSeleccionTabla();
        cargarTablaDesdeArchivo();
    }
    
    private void cargarTablaDesdeArchivo() {
        try {
            listaObservable.clear();
            listaObservable.addAll(cuentaRepo.obtenerTodos());
            tablaCuentas.setItems(listaObservable);
        } catch (Exception ex) {
            mostrarAlerta(AlertType.ERROR, "Error de carga", "No se pudieron recuperar "
                    + "los registros de cuentas bancarias.");
        }
    }
    
    private void vincularSeleccionTabla() {
        tablaCuentas.getSelectionModel().selectedItemProperty().addListener((obs, 
                viejo, nuevoSeleccionado) -> {
            if (nuevoSeleccionado != null) {
                txtNumeroCuenta.setText(nuevoSeleccionado.getNumeroCuenta());
                txtNumeroCuenta.setEditable(false);
                comboTipo.setValue(nuevoSeleccionado.getTipo());
                txtRfcCliente.setText(nuevoSeleccionado.getClienteAsociado().getRfcCurp());
                txtSaldoInicial.setText(String.valueOf(nuevoSeleccionado.getSaldoActual()));
                txtLimiteCredito.setText(String.valueOf(nuevoSeleccionado.getLimiteCredito()));
            }
        });
    }
    
    @FXML
    private void controlarAgregar() {
        String numeroCuenta = txtNumeroCuenta.getText().trim();
        TipoCuenta tipo = comboTipo.getValue();
        String rfcCliente = txtRfcCliente.getText().trim();
        String saldoTxt = txtSaldoInicial.getText().trim();
        String limiteTxt = txtLimiteCredito.getText().trim();
        
        if (numeroCuenta.isEmpty() || tipo == null || rfcCliente.isEmpty() || 
                saldoTxt.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos obligatorios", "Llene todos "
                    + "los campos base.");
        } else if (!numeroCuenta.matches("\\d+")) {
            mostrarAlerta(AlertType.ERROR, "Formato inválido", "El número de cuenta "
                    + "solo debe contener números.");
        } else {
            
            try {
                double saldo = Double.parseDouble(saldoTxt);
                double limite = limiteTxt.isEmpty() ? 0.0 : Double.parseDouble(limiteTxt);
                
                if (saldo < 0 || limite < 0) {
                    mostrarAlerta(AlertType.ERROR, "Valores numéricos inválidos", 
                        "Los montos no pueden ser negativos.");
                } else {
                    Cliente clienteDueno = clienteRepo.buscarPorId(rfcCliente);
                    
                    CuentaBancaria nuevaCuenta = new CuentaBancaria.Builder(numeroCuenta, 
                    tipo, clienteDueno).saldoActual(saldo)
                          .limiteCredito(limite)
                          .build();
                    
                    cuentaRepo.guardar(nuevaCuenta);
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", "Cuenta creada "
                            + "exitosamente.");
                    limpiarCampos();
                    cargarTablaDesdeArchivo();
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta(AlertType.ERROR, "Error de formato", "El saldo y límite "
                        + "deben ser valores numéricos válidos (ejemplo. 1500.50).");
            } catch (ClienteNoEncontradoException ex) {
                mostrarAlerta(AlertType.ERROR, "Cliente no encontrado", "");
            } catch (Exception ex) {
                mostrarAlerta(AlertType.ERROR, "Error de sistema", "");
            }
        }
    }
    
    @FXML
    private void controlarEditar() {
        CuentaBancaria seleccionado = tablaCuentas.getSelectionModel().getSelectedItem();
        String saldoTxt = txtSaldoInicial.getText().trim();
        String limiteTxt = txtLimiteCredito.getText().trim();
        TipoCuenta tipo = comboTipo.getValue();
        
        if (seleccionado == null) {
            mostrarAlerta(AlertType.WARNING, "Sin selección", "Por favor, seleccione "
                    + "una cuenta de la tabla para modificar");
        } else if (saldoTxt.isEmpty() || tipo == null) {
            mostrarAlerta(AlertType.WARNING, "Campos obligatorios", "El saldo y "
                    + "el tipo de cuenta no pueden quedar vacíos.");
        } else {
            try {
                double saldo = Double.parseDouble(saldoTxt);
                double limite = limiteTxt.isEmpty() ? 0.0 : Double.parseDouble(limiteTxt);
                
                if (saldo < 0 || limite < 0) {
                    mostrarAlerta(AlertType.ERROR, "Valores numéricos inválidos", "Los montos "
                    + "no pueden ser negativos.");
                } else {            
                    CuentaBancaria cuentaActualizada = new CuentaBancaria.Builder(
                            seleccionado.getNumeroCuenta(), tipo, seleccionado.getClienteAsociado())
                            .saldoActual(saldo)
                            .limiteCredito(limite)
                            .build();
                    
                    cuentaRepo.actualizar(cuentaActualizada);
                    mostrarAlerta(AlertType.INFORMATION, "Éxito", "Datos de la "
                            + "cuenta actualizados correctamente");
                    limpiarCampos();
                    cargarTablaDesdeArchivo();
        }
    } catch (NumberFormatException ex) {
        mostrarAlerta(AlertType.ERROR, "Error de formato", "Verifique las entradas "
                + "numéricas de saldos o límites.");
    } catch (Exception ex) {
        mostrarAlerta(AlertType.ERROR, "Error de sistema", "No se pudo actualizar el "
                + "registro físico de la cuenta.");
            }
        }
    }
    
    @FXML
    private void controlarEliminar() {
        CuentaBancaria seleccionado = tablaCuentas.getSelectionModel().getSelectedItem();
        
        if (seleccionado == null) {
            mostrarAlerta(AlertType.WARNING, "Sin selección", "Por favor, seleccione "
                    + "una cuenta de la tabla para dar de baja.");
        } else {
            try {
                cuentaRepo.eliminar(seleccionado.getNumeroCuenta());
                mostrarAlerta(AlertType.INFORMATION, "Éxito", "La cuenta bancaria "
                        + "seleccionada ha sido eliminada.");
                limpiarCampos();
                cargarTablaDesdeArchivo();
            } catch (Exception ex) {
                mostrarAlerta(AlertType.ERROR, "Error de sistema", "Ocurrió un problema "
                        + "al procesar la baja de la cuenta bancaria.");
            }
        }
    }
    
    private void limpiarCampos() {
        txtNumeroCuenta.clear();
        comboTipo.setValue(null);
        txtRfcCliente.clear();
        txtLimiteCredito.clear();
    }
    
    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
}
