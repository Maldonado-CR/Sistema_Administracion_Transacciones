/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones.controlador;

import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import uv.lis.sistema_administracion_transacciones.modelo.repositorio.EmpleadoRepositorio;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.Cajero;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.Credenciales;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.DatosPersonales;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.EjecutivoCuenta;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.Empleado;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.Gerente;

/**
 *
 * @author cinth
 */
public class EmpleadosController {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtSalario;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtContrasenia;
    @FXML
    private ComboBox<String> cmbRol;

    @FXML
    private Label lblEspec1;
    @FXML
    private TextField txtEspec1;
    @FXML
    private Label lblEspec2;
    @FXML
    private TextField txtEspec2;

    @FXML
    private TableView<Empleado> tablaEmpleados;
    @FXML
    private TableColumn<Empleado, String> colId;
    @FXML
    private TableColumn<Empleado, String> colNombre;
    @FXML
    private TableColumn<Empleado, String> colCorreo;
    @FXML
    private TableColumn<Empleado, Double> colSalario;
    @FXML
    private TableColumn<Empleado, String> colRol;

    private final EmpleadoRepositorio empleadoRepo = new EmpleadoRepositorio();
    private final ObservableList<Empleado> listaObservable = FXCollections.observableArrayList();
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    @FXML
    public void initialize() {
        if (cmbRol != null) {
            cmbRol.getItems().addAll("Cajero", "Ejecutivo", "Gerente");
        }

        colId.setCellValueFactory(new PropertyValueFactory<>("idEmpleado"));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDatosPersonales().getNombre()));
        colCorreo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAccesos().getUsuario()));
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));
        colRol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().obtenerRol()));

        vincularSelectionTabla();
        cargarTabla();
    }

    @FXML
    private void configurarCamposDinamicos() {
        String rol = cmbRol.getValue();
        if (rol == null) {
            cambiarVisibilidadCampos(false);
            bloquearCamposBase(true);
            return;
        }

        bloquearCamposBase(false);
        cambiarVisibilidadCampos(true);

        if (rol.equals("Cajero")) {
            lblEspec1.setText("Horario:");
            txtEspec1.setPromptText("Ej: Matutino / Vespertino");
            lblEspec2.setText("N° Ventanilla:");
            txtEspec2.setPromptText("Ej: 3");
        } else if (rol.equals("Ejecutivo")) {
            lblEspec1.setText("Clientes Asig:");
            txtEspec1.setPromptText("Ej: 15");
            lblEspec2.setText("Especialidad:");
            txtEspec2.setPromptText("Ej: PYMES / Corporativo");
        } else if (rol.equals("Gerente")) {
            lblEspec1.setText("Nivel Acceso:");
            txtEspec1.setPromptText("Ej: Regional / Nacional");
            lblEspec2.setText("Años Experiencia:");
            txtEspec2.setPromptText("Ej: 5");
        }
    }

    private void cambiarVisibilidadCampos(boolean visible) {
        lblEspec1.setVisible(visible);
        txtEspec1.setVisible(visible);
        lblEspec2.setVisible(visible);
        txtEspec2.setVisible(visible);
    }

    private void bloquearCamposBase(boolean bloquear) {
        txtId.setDisable(bloquear);
        txtNombre.setDisable(bloquear);
        txtSalario.setDisable(bloquear);
        txtCorreo.setDisable(bloquear);
        txtContrasenia.setDisable(bloquear);
    }
    
    private void cargarTabla() {
        try {
            listaObservable.clear();
            List<Empleado> empleados = empleadoRepo.obtenerTodos();
            listaObservable.addAll(empleados);
            tablaEmpleados.setItems(listaObservable);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar la lista de empleados.", AlertType.ERROR);
        }
    }

    private void vincularSelectionTabla() {
        tablaEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevoSeleccionado) -> {
            if (nuevoSeleccionado != null) {
                txtId.setText(nuevoSeleccionado.getIdEmpleado());
                txtId.setEditable(false); 
                txtNombre.setText(nuevoSeleccionado.getDatosPersonales().getNombre());
                txtSalario.setText(String.valueOf(nuevoSeleccionado.getSalario()));
                txtCorreo.setText(nuevoSeleccionado.getAccesos().getUsuario());
                txtContrasenia.setText("********"); 
                txtContrasenia.setEditable(false); 
                cmbRol.setValue(nuevoSeleccionado.obtenerRol());

                if (nuevoSeleccionado instanceof Cajero) {
                    Cajero c = (Cajero) nuevoSeleccionado;
                    txtEspec1.setText(c.getHorarioTrabajo());
                    txtEspec2.setText(String.valueOf(c.getNumeroVentanilla()));
                } else if (nuevoSeleccionado instanceof EjecutivoCuenta) {
                    EjecutivoCuenta e = (EjecutivoCuenta) nuevoSeleccionado;
                    txtEspec1.setText(String.valueOf(e.getNumeroClientesAsignados()));
                    txtEspec2.setText(e.getEspecializacion());
                } else if (nuevoSeleccionado instanceof Gerente) {
                    Gerente g = (Gerente) nuevoSeleccionado;
                    txtEspec1.setText(g.getNivelAcceso());
                    txtEspec2.setText(String.valueOf(g.getAnosExperiencia()));
                }

                bloquearCamposBase(false);
                cambiarVisibilidadCampos(true);
            }
        });
    }

    @FXML
    private void controlarAgregar() {
        String id = txtId.getText().trim();
        String nombre = txtNombre.getText().trim();
        String salarioTxt = txtSalario.getText().trim();
        String correo = txtCorreo.getText().trim();
        String pass = txtContrasenia.getText().trim();
        String rol = cmbRol.getValue();
        String esp1 = txtEspec1.getText().trim();
        String esp2 = txtEspec2.getText().trim();

        if (id.isEmpty() || nombre.isEmpty() || salarioTxt.isEmpty() || correo.isEmpty() || pass.isEmpty() || rol == null || esp1.isEmpty() || esp2.isEmpty()) {
            mostrarAlerta("Campos requeridos", "Por favor llene todos los campos del formulario.", AlertType.WARNING);
            return;
        }

        if (!correo.matches(EMAIL_PATTERN)) {
            mostrarAlerta("Formato inválido", "El formato del correo electrónico no es válido.", AlertType.ERROR);
            return;
        }

        try {
            List<Empleado> existentes = empleadoRepo.obtenerTodos();
            for (Empleado emp : existentes) {
                if (emp.getIdEmpleado().equalsIgnoreCase(id)) {
                    mostrarAlerta("ID Duplicado", "Ya existe un empleado con el ID: " + id, AlertType.ERROR);
                    return;
                }
                if (emp.getAccesos().getUsuario().equalsIgnoreCase(correo)) {
                    mostrarAlerta("Correo Duplicado", "Este correo ya está asignado.", AlertType.ERROR);
                    return;
                }
            }

            double salario = Double.parseDouble(salarioTxt);
            DatosPersonales dp = new DatosPersonales(nombre, "EuroBank Branch", LocalDate.of(1995, 1, 1), "M");
            Credenciales cred = new Credenciales(correo, pass);

            Empleado nuevo;
            if (rol.equals("Cajero")) {
                nuevo = new Cajero(id, salario, dp, cred, esp1, Integer.parseInt(esp2));
            } else if (rol.equals("Ejecutivo")) {
                nuevo = new EjecutivoCuenta(id, salario, dp, cred, Integer.parseInt(esp1), esp2);
            } else {
                nuevo = new Gerente(id, salario, dp, cred, esp1, Integer.parseInt(esp2));
            }

            empleadoRepo.guardar(nuevo);
            mostrarAlerta("Éxito", "Empleado registrado con sus datos específicos reales.", AlertType.INFORMATION);
            controlarLimpiar();
            cargarTabla();
        } catch (NumberFormatException e) {
            mostrarAlerta("Formato", "Verifique que los campos numéricos sean válidos.", AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar el empleado: " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void controlarEditar() {
        Empleado seleccionado = tablaEmpleados.getSelectionModel().getSelectedItem();
        String correoActualizado = txtCorreo.getText().trim();
        
        if (seleccionado == null) {
            mostrarAlerta("Sin selección", "Por favor, seleccione un empleado de "
                    + "la tabla para modificar.", AlertType.WARNING);
        } else if (seleccionado.getIdEmpleado().equals("EMP00")) {
            mostrarAlerta("Restricción", "El usuario administrador raíz del sistema "
                    + "no puede ser modificado.", AlertType.WARNING);
        } else if (!correoActualizado.matches(EMAIL_PATTERN)) {
            mostrarAlerta("Formato inválido", "El formato del correo electrónico/usuario "
                    + "no es válido (ejemplo: usuario@eurobank.com", AlertType.ERROR);
        } else {
            
            try {
                double salario = Double.parseDouble(txtSalario.getText().trim());
                DatosPersonales dp = new DatosPersonales(
                        txtNombre.getText().trim(),
                        seleccionado.getDatosPersonales().getDireccion(),
                        seleccionado.getDatosPersonales().getFechaNacimiento(),
                        seleccionado.getDatosPersonales().getGenero()
                );
                
                Credenciales credencialesActualizadas = seleccionado.getAccesos();
                credencialesActualizadas.setUsuario(correoActualizado);

                String esp1 = txtEspec1.getText().trim();
                String esp2 = txtEspec2.getText().trim();
                String rol = cmbRol.getValue();

                Empleado empleadoActualizado;
                if (rol.equals("Cajero")) {
                    empleadoActualizado = new Cajero(seleccionado.getIdEmpleado(), salario, dp, credencialesActualizadas, esp1, Integer.parseInt(esp2));
                } else if (rol.equals("Ejecutivo")) {
                    empleadoActualizado = new EjecutivoCuenta(seleccionado.getIdEmpleado(), salario, dp, credencialesActualizadas, Integer.parseInt(esp1), esp2);
                } else {
                    empleadoActualizado = new Gerente(seleccionado.getIdEmpleado(), salario, dp, credencialesActualizadas, esp1, Integer.parseInt(esp2));
                }
                
                empleadoRepo.actualizar(empleadoActualizado);
                mostrarAlerta("Éxito", "Los datos del empleado han sido actualizados de forma correcta.", AlertType.INFORMATION);
                controlarLimpiar();
                cargarTabla();
                
        } catch (NumberFormatException e) {
            mostrarAlerta("Formato", "Verifique que los campos numéricos específicos "
                    + "tengan valores válidos.", AlertType.ERROR);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo actualizar el registro físico del "
                    + "empleado.", AlertType.ERROR);
            }
        }
    }

    @FXML
    private void controlarEliminar() {
        Empleado seleccionado = tablaEmpleados.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Sin selección", "Por favor, seleccione un empleado de la tabla para dar de baja.", AlertType.WARNING);
            return;
        }

        if (seleccionado.getIdEmpleado().equals("EMP00")) {
            mostrarAlerta("Restricción", "Acceso denegado. El usuario administrador raíz no puede ser dado de baja del sistema.", AlertType.WARNING);
            return;
        }

        try {
            empleadoRepo.eliminar(seleccionado.getIdEmpleado());
            mostrarAlerta("Éxito", "El empleado ha sido dado de baja de manera definitiva.", AlertType.INFORMATION);
            controlarLimpiar();
            cargarTabla();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo dar de baja al empleado: " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void controlarLimpiar() {
        txtId.clear();
        txtId.setEditable(true);
        txtNombre.clear();
        txtSalario.clear();
        txtCorreo.clear();
        txtContrasenia.clear();
        txtContrasenia.setEditable(true);
        txtEspec1.clear();
        txtEspec2.clear();
        if (cmbRol != null) {
            cmbRol.setValue(null);
        }
        cambiarVisibilidadCampos(false);
        bloquearCamposBase(true);
        tablaEmpleados.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}