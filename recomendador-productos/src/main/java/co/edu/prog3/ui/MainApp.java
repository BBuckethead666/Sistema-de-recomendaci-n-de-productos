package co.edu.prog3.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Recomendador de Productos");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // mostrará el error real en consola
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
