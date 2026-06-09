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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import uv.lis.sistema_administracion_transacciones.logica.AutenticacionServicio;
import uv.lis.sistema_administracion_transacciones.modelo.usuarios.Empleado;

/**
 *
 * @author Maria Jose
 */
public class LoginController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContrasenia;
    @FXML
    private Button btnIngresar;

    private final AutenticacionServicio autenticacionServicio = new AutenticacionServicio();

    @FXML
    private void controlarIngresar() {
        String usuario = txtUsuario.getText().trim();
        String contrasenia = txtContrasenia.getText().trim();

        if (usuario.isEmpty() || contrasenia.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Por favor, introduzca su usuario y contraseña.", AlertType.WARNING);
            return;
        }

        Empleado empleadoAutenticado = autenticacionServicio.autenticar(usuario, contrasenia);

        if (empleadoAutenticado != null) {
            mostrarAlerta("Bienvenido", "Acceso concedido como: " + empleadoAutenticado.obtenerRol(), AlertType.INFORMATION);
            cargarMenuPrincipal(empleadoAutenticado);
        } else {
            mostrarAlerta("Error de autenticación", "Usuario o contraseña incorrectos.", AlertType.ERROR);
        }
    }

    private void cargarMenuPrincipal(Empleado empleado) {
        try {
            FXMLLoader cargador = new FXMLLoader(getClass().getResource("/fxml/vista/MenuPrincipalView.fxml"));
            Parent root = cargador.load();
            
            MenuPrincipalController menuController = cargador.getController();
            menuController.inicializarPermisosRol(empleado);
            
            Stage escenarioPrincipal = new Stage();
            escenarioPrincipal.setTitle("EuroBank - Menú Principal");
            escenarioPrincipal.setScene(new Scene(root));
            escenarioPrincipal.show();
            
            Stage escenarioLogin = (Stage) btnIngresar.getScene().getWindow();
            escenarioLogin.close();
        } catch (IOException e) {
            mostrarAlerta("Error de sistema", "No se pudo cargar el menú principal.", AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}