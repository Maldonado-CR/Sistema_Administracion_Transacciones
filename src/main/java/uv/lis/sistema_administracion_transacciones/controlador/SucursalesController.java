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
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.Empleado;

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

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final String PHONE_PATTERN = "^\\d{10}$";

    @FXML
    public void initialize() {
        Empleado usuarioActivo = MenuPrincipalController.getEmpleadoAutenticado();
        if (usuarioActivo != null) {
            txtGerente.setText(usuarioActivo.getDatosPersonales().getNombre());
        } else {
            txtGerente.setText("Gerente No Detectado");
        }
        
        txtGerente.setEditable(false);
        txtGerente.setDisable(true);
    }

    @FXML
    private void controlarAgregar() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos requeridos", "Por favor, complete todos los campos del formulario de la sucursal.");
            return;
        }

        if (!telefono.matches(PHONE_PATTERN)) {
            mostrarAlerta(AlertType.ERROR, "Formato telefónico inválido", "El teléfono de la sucursal debe contener exactamente 10 dígitos numéricos.");
            return;
        }

        if (!correo.matches(EMAIL_PATTERN)) {
            mostrarAlerta(AlertType.ERROR, "Formato de correo inválido", "El correo electrónico institucional de la sucursal no cuenta con un formato válido.");
            return;
        }

        mostrarAlerta(AlertType.INFORMATION, "Éxito", "Sucursal '" + nombre + "' validada de forma correcta bajo la supervisión de: " + txtGerente.getText());
        controlarLimpiar();
    }

    @FXML
    private void controlarEditar() {
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (!telefono.matches(PHONE_PATTERN) || !correo.matches(EMAIL_PATTERN)) {
            mostrarAlerta(AlertType.ERROR, "Error de formato", "Verifique que los campos de contacto cumplan con las reglas de negocio.");
            return;
        }
        
        mostrarAlerta(AlertType.INFORMATION, "Modificación", "Cambios validados y listos para actualizar en el archivo.");
    }

    @FXML
    private void controlarEliminar() {
        mostrarAlerta(AlertType.INFORMATION, "Eliminación", "Baja de infraestructura procesada de manera correcta.");
    }

    @FXML
    private void controlarLimpiar() {
        txtId.clear();
        txtNombre.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        
        Empleado usuarioActivo = MenuPrincipalController.getEmpleadoAutenticado();
        if (usuarioActivo != null) {
            txtGerente.setText(usuarioActivo.getDatosPersonales().getNombre());
        }
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}