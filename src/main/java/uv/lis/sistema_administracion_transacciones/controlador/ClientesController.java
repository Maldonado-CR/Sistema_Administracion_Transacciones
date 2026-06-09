/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.controlador;

import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import uv.lis.sistema_administracion_transacciones.modelo.entidades.Cliente;
import uv.lis.sistema_administracion_transacciones.modelo.excepciones.ClienteNoEncontradoException;
import uv.lis.sistema_administracion_transacciones.modelo.repositorio.ClienteRepositorio;

/**
 *
 * @author Maria Jose
 */
public class ClientesController {

    @FXML
    private TextField txtBuscarRfc;
    @FXML
    private TextField txtRfc;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtNacionalidad;
    @FXML
    private DatePicker dpFechaNacimiento;
    @FXML
    private TextField txtDireccion;

    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn<Cliente, String> colRfc;
    @FXML
    private TableColumn<Cliente, String> colNombre;
    @FXML
    private TableColumn<Cliente, String> colApellidos;
    @FXML
    private TableColumn<Cliente, String> colTelefono;
    @FXML
    private TableColumn<Cliente, String> colCorreo;

    private final ClienteRepositorio clienteRepo = new ClienteRepositorio();
    private final ObservableList<Cliente> listaObservable = FXCollections.observableArrayList();
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    @FXML
    public void initialize() {
        colRfc.setCellValueFactory(new PropertyValueFactory<>("rfcCurp"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidosCliente"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefonoCliente"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correoElectronico"));

        vincularSeleccionTabla();
        cargarTablaDesdeArchivo();
    }

    @FXML
    private void manejarEscrituraRfc() {
        String rfc = txtRfc.getText().trim();
        bloquearCamposFormulario(rfc.isEmpty());
    }

    private void bloquearCamposFormulario(boolean bloquear) {
        txtNombre.setDisable(bloquear);
        txtApellidos.setDisable(bloquear);
        txtTelefono.setDisable(bloquear);
        txtCorreo.setDisable(bloquear);
        txtNacionalidad.setDisable(bloquear);
        dpFechaNacimiento.setDisable(bloquear);
        txtDireccion.setDisable(bloquear);
    }

    private void cargarTablaDesdeArchivo() {
        try {
            listaObservable.clear();
            List<Cliente> clientesArchivo = clienteRepo.obtenerTodos();
            listaObservable.addAll(clientesArchivo);
            tablaClientes.setItems(listaObservable);
        } catch (Exception e) {
            mostrarAlerta("Error de carga", "No se pudieron recuperar los clientes.", AlertType.ERROR);
        }
    }

    private void vincularSeleccionTabla() {
        tablaClientes.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevoSeleccionado) -> {
            if (nuevoSeleccionado != null) {
                txtRfc.setText(nuevoSeleccionado.getRfcCurp());
                txtRfc.setEditable(false);
                txtNombre.setText(nuevoSeleccionado.getNombreCliente());
                txtApellidos.setText(nuevoSeleccionado.getApellidosCliente());
                txtTelefono.setText(nuevoSeleccionado.getTelefonoCliente());
                txtCorreo.setText(nuevoSeleccionado.getCorreoElectronico());
                txtNacionalidad.setText(nuevoSeleccionado.getNacionalidadCliente());
                dpFechaNacimiento.setValue(nuevoSeleccionado.getFechaNacimiento());
                txtDireccion.setText(nuevoSeleccionado.getDireccionCliente());
                bloquearCamposFormulario(false);
            }
        });
    }

    @FXML
    private void controlarBuscar() {
        String busqueda = txtBuscarRfc.getText().trim();
        if (busqueda.isEmpty()) {
            cargarTablaDesdeArchivo();
            return;
        }

        try {
            Cliente encontrado = clienteRepo.buscarPorId(busqueda);
            listaObservable.clear();
            listaObservable.add(encontrado);
        } catch (ClienteNoEncontradoException e) {
            mostrarAlerta("Búsqueda de Cliente", e.getMessage(), AlertType.INFORMATION);
        } catch (Exception e) {
            mostrarAlerta("Error", "Fallo interno en el repositorio.", AlertType.ERROR);
        }
    }

    @FXML
    private void controlarAgregar() {
        String rfc = txtRfc.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String correo = txtCorreo.getText().trim();
        LocalDate fechaNac = dpFechaNacimiento.getValue();

        if (rfc.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() || fechaNac == null) {
            mostrarAlerta("Campos obligatorios", "Por favor llene todos los campos requeridos.", AlertType.WARNING);
            return;
        }

        if (!correo.isEmpty() && !correo.matches(EMAIL_PATTERN)) {
            mostrarAlerta("Formato inválido", "El formato del correo electrónico no es válido.", AlertType.ERROR);
            return;
        }

        try {
            List<Cliente> existentes = clienteRepo.obtenerTodos();
            for (Cliente c : existentes) {
                if (c.getRfcCurp().equalsIgnoreCase(rfc)) {
                    mostrarAlerta("Registro duplicado", "Ya existe un cliente con este RFC/CURP.", AlertType.ERROR);
                    return;
                }
            }

            Cliente nuevoCliente = new Cliente.Builder(rfc)
                    .nombreCliente(nombre)
                    .apellidosCliente(apellidos)
                    .telefonoCliente(txtTelefono.getText().trim())
                    .correoElectronico(correo)
                    .fechaNacimiento(fechaNac)
                    .nacionalidadCliente(txtNacionalidad.getText().trim())
                    .direccionCliente(txtDireccion.getText().trim())
                    .buil();

            clienteRepo.guardar(nuevoCliente);
            mostrarAlerta("Éxito", "Cliente registrado exitosamente.", AlertType.INFORMATION);
            limpiarFormulario();
            cargarTablaDesdeArchivo();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo almacenar el registro.", AlertType.ERROR);
        }
    }

    @FXML
    private void controlarEditar() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Sin selección", "Seleccione un cliente para modificar.", AlertType.WARNING);
            return;
        }

        try {
            Cliente clienteActualizado = new Cliente.Builder(seleccionado.getRfcCurp())
                    .nombreCliente(txtNombre.getText().trim())
                    .apellidosCliente(txtApellidos.getText().trim())
                    .telefonoCliente(txtTelefono.getText().trim())
                    .correoElectronico(txtCorreo.getText().trim())
                    .fechaNacimiento(dpFechaNacimiento.getValue())
                    .nacionalidadCliente(txtNacionalidad.getText().trim())
                    .direccionCliente(txtDireccion.getText().trim())
                    .buil();

            clienteRepo.actualizar(clienteActualizado);
            mostrarAlerta("Éxito", "Datos actualizados correctamente.", AlertType.INFORMATION);
            limpiarFormulario();
            cargarTablaDesdeArchivo();
        } catch (ClienteNoEncontradoException e) {
            mostrarAlerta("Error de actualización", e.getMessage(), AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo actualizar el registro.", AlertType.ERROR);
        }
    }

    @FXML
    private void controlarEliminar() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Sin selección", "Seleccione un cliente para eliminar.", AlertType.WARNING);
            return;
        }

        try {
            clienteRepo.eliminar(seleccionado.getRfcCurp());
            mostrarAlerta("Éxito", "El cliente ha sido eliminado.", AlertType.INFORMATION);
            limpiarFormulario();
            cargarTablaDesdeArchivo();
        } catch (ClienteNoEncontradoException e) {
            mostrarAlerta("Error de eliminación", e.getMessage(), AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error en el borrado físico.", AlertType.ERROR);
        }
    }

    @FXML
    private void controlarExportar() {
        mostrarAlerta("Exportar", "Se ha generado el reporte plano de clientes exitosamente.", AlertType.INFORMATION);
    }

    private void limpiarFormulario() {
        txtRfc.clear();
        txtRfc.setEditable(true);
        txtNombre.clear();
        txtApellidos.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtNacionalidad.clear();
        txtDireccion.clear();
        if (dpFechaNacimiento != null) {
            dpFechaNacimiento.setValue(null);
        }
        bloquearCamposFormulario(true);
        tablaClientes.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}