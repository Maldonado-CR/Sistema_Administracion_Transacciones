/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uv.lis.sistema_administracion_transacciones;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author cinth
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
Parent root = FXMLLoader.load(getClass().getResource("/fxml/vista/LoginView.fxml"));
primaryStage.setTitle("EuroBank - Iniciar Sesión");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}