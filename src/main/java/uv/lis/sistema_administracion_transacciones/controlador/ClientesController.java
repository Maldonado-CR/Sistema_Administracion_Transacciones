/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.controlador;

import java.time.LocalDate;
import java.time.Period;
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
    
    private static final String ID_PATTERN = "^[A-Za-z0-9_-]+$";
    private static final String NOMBRE_PATTERN = "^[A-Za-zÁ-ÿñÑ\\s]+$";
    private static final String PHONE_PATTERN = "^\\d{10}$";
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

    private boolean validarCamposVacios(String rfc, String nombre, String apellidos, String direccion, String telefono, String correo, LocalDate fechaNac, String nacionalidad) {
        if (rfc.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || correo.isEmpty() || fechaNac == null || nacionalidad.isEmpty()) {
            mostrarAlerta("Campos obligatorios", "Por favor llene todos los campos requeridos del formulario.", AlertType.WARNING);
            return true;
        }
        return false;
    }

    private boolean validarFormatos(String rfc, String nombre, String apellidos, String direccion, String telefono, String correo, LocalDate fechaNac) {
        if (!rfc.matches(ID_PATTERN)) {
            mostrarAlerta("RFC/CURP inválido", "El campo RFC/CURP no admite espacios ni caracteres especiales.", AlertType.ERROR);
            return true;
        }
        if (!nombre.matches(NOMBRE_PATTERN) || !apellidos.matches(NOMBRE_PATTERN)) {
            mostrarAlerta("Nombre inválido", "El nombre y apellidos solo deben contener caracteres alfabéticos.", AlertType.ERROR);
            return true;
        }
        if (direccion.matches("^\\d+$") || direccion.length() < 5) {
            mostrarAlerta("Dirección inválida", "Ingrese una ubicación física válida (calle, número y colonia).", AlertType.ERROR);
            return true;
        }
        if (!telefono.matches(PHONE_PATTERN)) {
            mostrarAlerta("Teléfono inválido", "El teléfono debe contener exactamente 10 dígitos numéricos.", AlertType.ERROR);
            return true;
        }
        if (!correo.matches(EMAIL_PATTERN)) {
            mostrarAlerta("Formato inválido", "El formato del correo electrónico provisto no es válido.", AlertType.ERROR);
            return true;
        }
        if (fechaNac.isAfter(LocalDate.now())) {
            mostrarAlerta("Fecha inválida", "La fecha de nacimiento no puede ser una fecha futura.", AlertType.ERROR);
            return true;
        }
        int edad = Period.between(fechaNac, LocalDate.now()).getYears();
        if (edad < 18) {
            mostrarAlerta("Restricción de edad", "Regla de Negocio: El cliente debe ser mayor de edad (18 años o más) para ser registrado.", AlertType.ERROR);
            return true;
        }
        return false;
    }

    @FXML
    private void controlarAgregar() {
        String rfc = txtRfc.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String nacionalidad = txtNacionalidad.getText().trim();
        String direccion = txtDireccion.getText().trim();
        LocalDate fechaNac = dpFechaNacimiento.getValue();

        if (validarCamposVacios(rfc, nombre, apellidos, direccion, telefono, correo, fechaNac, nacionalidad)) {
            return;
        }
        if (validarFormatos(rfc, nombre, apellidos, direccion, telefono, correo, fechaNac)) {
            return;
        }

        try {
            List<Cliente> existentes = clienteRepo.obtenerTodos();
            for (Cliente c : existentes) {
                if (c.getRfcCurp().equalsIgnoreCase(rfc)) {
                    mostrarAlerta("Registro duplicado", "Ya existe un cliente con este RFC/CURP.", AlertType.ERROR);
                    return;
                }
                if (c.getCorreoElectronico().equalsIgnoreCase(correo)) {
                    mostrarAlerta("Correo duplicado", "El correo electrónico ya se encuentra registrado por otro cliente.", AlertType.ERROR);
                    return;
                }
            }

            Cliente nuevoCliente = new Cliente.Builder(rfc)
                    .nombreCliente(nombre)
                    .apellidosCliente(apellidos)
                    .telefonoCliente(telefono)
                    .correoElectronico(correo)
                    .fechaNacimiento(fechaNac)
                    .nacionalidadCliente(nacionalidad)
                    .direccionCliente(direccion)
                    .buil();

            clienteRepo.guardar(nuevoCliente);
            mostrarAlerta("Éxito", "Cliente registrado exitosamente de forma física.", AlertType.INFORMATION);
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

        String rfc = txtRfc.getText().trim();
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String nacionalidad = txtNacionalidad.getText().trim();
        String direccion = txtDireccion.getText().trim();
        LocalDate fechaNac = dpFechaNacimiento.getValue();

        if (validarCamposVacios(rfc, nombre, apellidos, direccion, telefono, correo, fechaNac, nacionalidad)) {
            return;
        }
        if (validarFormatos(seleccionado.getRfcCurp(), nombre, apellidos, direccion, telefono, correo, fechaNac)) {
            return;
        }

        try {
            List<Cliente> existentes = clienteRepo.obtenerTodos();
            for (Cliente c : existentes) {
                if (!c.getRfcCurp().equalsIgnoreCase(seleccionado.getRfcCurp()) && c.getCorreoElectronico().equalsIgnoreCase(correo)) {
                    mostrarAlerta("Correo duplicado", "El correo electrónico ya pertenece a otro cliente registrado.", AlertType.ERROR);
                    return;
                }
            }

            Cliente clienteActualizado = new Cliente.Builder(seleccionado.getRfcCurp())
                    .nombreCliente(nombre)
                    .apellidosCliente(apellidos)
                    .telefonoCliente(telefono)
                    .correoElectronico(correo)
                    .fechaNacimiento(fechaNac)
                    .nacionalidadCliente(nacionalidad)
                    .direccionCliente(direccion)
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