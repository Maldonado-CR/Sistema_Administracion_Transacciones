/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.controlador;

import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import uv.lis.sistema_administracion_transacciones.modelo.entidades.Sucursal;
import uv.lis.sistema_administracion_transacciones.modelo.excepciones.SucursalDuplicadaException;
import uv.lis.sistema_administracion_transacciones.modelo.excepciones.SucursalNoEncontradaException;
import uv.lis.sistema_administracion_transacciones.modelo.repositorio.SucursalRepositorio;
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
    private TableView<Sucursal> tablaSucursales;
    @FXML
    private TableColumn<Sucursal, String> colId;
    @FXML
    private TableColumn<Sucursal, String> colNombre;
    @FXML
    private TableColumn<Sucursal, String> colDireccion;
    @FXML
    private TableColumn<Sucursal, String> colTelefono;
    @FXML
    private TableColumn<Sucursal, String> colGerente;

    private final SucursalRepositorio sucursalRepo = new SucursalRepositorio();
    private final ObservableList<Sucursal> listaObservable = FXCollections.observableArrayList();

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final String PHONE_PATTERN = "^\\d{10}$";
    private static final String ID_PATTERN = "^[A-Za-z0-9_-]+$";

    @FXML
    public void initialize() {
        configurarUsuarioSesion();

        colId.setCellValueFactory(new PropertyValueFactory<>("numeroIdentificacion"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreSucursal"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccionSucursal"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colGerente.setCellValueFactory(new PropertyValueFactory<>("nombreGerente"));

        vincularSeleccionTabla();
        cargarTablaDesdeArchivo();
    }

    private void configurarUsuarioSesion() {
        Empleado usuarioActivo = MenuPrincipalController.getEmpleadoAutenticado();
        if (usuarioActivo != null) {
            txtGerente.setText(usuarioActivo.getDatosPersonales().getNombre());
        } else {
            txtGerente.setText("Gerente No Detectado");
        }
        txtGerente.setEditable(false);
        txtGerente.setDisable(true);
    }

    private void cargarTablaDesdeArchivo() {
        try {
            listaObservable.clear();
            listaObservable.addAll(sucursalRepo.obtenerTodos());
            tablaSucursales.setItems(listaObservable);
        } catch (Exception e) {
            mostrarAlerta(AlertType.ERROR, "Error de sistema", "No se pudieron recuperar las sucursales bancarias: " + e.getMessage());
        }
    }

    private void vincularSeleccionTabla() {
        tablaSucursales.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevoSeleccionado) -> {
            if (nuevoSeleccionado != null) {
                txtId.setText(nuevoSeleccionado.getNumeroIdentificacion());
                txtId.setEditable(false);
                txtNombre.setText(nuevoSeleccionado.getNombreSucursal());
                txtDireccion.setText(nuevoSeleccionado.getDireccionSucursal());
                txtTelefono.setText(nuevoSeleccionado.getTelefono());
                txtCorreo.setText(nuevoSeleccionado.getCorreoElectronico());
                txtGerente.setText(nuevoSeleccionado.getNombreGerente());
            }
        });
    }

    @FXML
    private void controlarAgregar() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String gerente = txtGerente.getText().trim();

        if (validarCamposVacios(id, nombre, direccion, telefono, correo)) return;
        if (validarFormatosNegocio(id, direccion, telefono, correo)) return;

        try {
            Sucursal nuevaSucursal = new Sucursal.Builder(id, nombre, direccion)
                    .telefono(telefono)
                    .correoElectronico(correo)
                    .nombreGerente(gerente)
                    .personaContacto("No Asignada")
                    .build();

            sucursalRepo.guardar(nuevaSucursal);
            mostrarAlerta(AlertType.INFORMATION, "Éxito", "La sucursal '" + nombre + "' ha sido registrada físicamente.");
            controlarLimpiar();
            cargarTablaDesdeArchivo();
        } catch (SucursalDuplicadaException ex) {
            mostrarAlerta(AlertType.ERROR, "Registro duplicado", ex.getMessage());
        } catch (Exception ex) {
            mostrarAlerta(AlertType.ERROR, "Error de escritura", "No se pudo procesar el almacenamiento: " + ex.getMessage());
        }
    }

    @FXML
    private void controlarEditar() {
        Sucursal seleccionado = tablaSucursales.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(AlertType.WARNING, "Sin selección", "Por favor, seleccione una sucursal de la tabla para modificar.");
            return;
        }

        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        String gerente = txtGerente.getText().trim();

        if (validarCamposVacios(id, nombre, direccion, telefono, correo)) return;
        if (validarFormatosNegocio(id, direccion, telefono, correo)) return;

        try {
            Sucursal sucursalActualizada = new Sucursal.Builder(seleccionado.getNumeroIdentificacion(), nombre, direccion)
                    .telefono(telefono)
                    .correoElectronico(correo)
                    .nombreGerente(gerente)
                    .personaContacto(seleccionado.getPersonaContacto())
                    .build();

            sucursalRepo.actualizar(sucursalActualizada);
            mostrarAlerta(AlertType.INFORMATION, "Actualización exitosa", "Los datos de la sucursal fueron modificados de forma correcta.");
            controlarLimpiar();
            cargarTablaDesdeArchivo();
        } catch (SucursalNoEncontradaException ex) {
            mostrarAlerta(AlertType.ERROR, "Error de actualización", ex.getMessage());
        } catch (Exception ex) {
            mostrarAlerta(AlertType.ERROR, "Error interno", "Ocurrió un problema al modificar el archivo físico.");
        }
    }

    @FXML
    private void controlarEliminar() {
        Sucursal seleccionado = tablaSucursales.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(AlertType.WARNING, "Sin selección", "Debe seleccionar la sucursal que desea dar de baja del sistema.");
            return;
        }

        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de que desea eliminar de forma permanente la sucursal '" + seleccionado.getNombreSucursal() + "'?");
        
        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            try {
                sucursalRepo.eliminar(seleccionado.getNumeroIdentificacion());
                mostrarAlerta(AlertType.INFORMATION, "Eliminación exitosa", "La infraestructura seleccionada se removió correctamente.");
                controlarLimpiar();
                cargarTablaDesdeArchivo();
            } catch (SucursalNoEncontradaException ex) {
                mostrarAlerta(AlertType.ERROR, "Error al eliminar", ex.getMessage());
            } catch (Exception ex) {
                mostrarAlerta(AlertType.ERROR, "Error interno", "No se pudo procesar la baja física de la sucursal.");
            }
        }
    }

    private boolean validarCamposVacios(String id, String nom, String dir, String tel, String corr) {
        if (id.isEmpty() || nom.isEmpty() || dir.isEmpty() || tel.isEmpty() || corr.isEmpty()) {
            mostrarAlerta(AlertType.WARNING, "Campos requeridos", "Todos los campos de texto son mandatorios para la operación.");
            return true;
        }
        return false;
    }

    private boolean validarFormatosNegocio(String id, String dir, String tel, String corr) {
        if (!id.matches(ID_PATTERN)) {
            mostrarAlerta(AlertType.ERROR, "ID inválido", "El ID no admite caracteres especiales ni espacios.");
            return true;
        }
        if (dir.matches("^\\d+$")) {
            mostrarAlerta(AlertType.ERROR, "Dirección inválida", "La dirección de la infraestructura no puede estar conformada únicamente por valores numéricos.");
            return true;
        }
        if (!tel.matches(PHONE_PATTERN)) {
            mostrarAlerta(AlertType.ERROR, "Teléfono inválido", "El campo telefónico corporativo debe contener exactamente 10 dígitos numéricos.");
            return true;
        }
        if (!corr.matches(EMAIL_PATTERN)) {
            mostrarAlerta(AlertType.ERROR, "Correo inválido", "El formato de correo institucional provisto es erróneo.");
            return true;
        }
        return false;
    }

    @FXML
    private void controlarLimpiar() {
        txtId.clear();
        txtId.setEditable(true);
        txtNombre.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        
        configurarUsuarioSesion();
        tablaSucursales.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}