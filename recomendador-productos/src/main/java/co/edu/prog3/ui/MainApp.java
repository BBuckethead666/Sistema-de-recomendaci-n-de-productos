package co.edu.prog3.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // 👇 Cargar el archivo FXML con internacionalización
        ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
        FXMLLoader loader = new FXMLLoader(
    getClass().getResource("/co/edu/prog3/ui/MainView.fxml"), bundle
);


        Parent root = loader.load(); // 👈 aquí se define root

        Scene scene = new Scene(root);
        // 👇 aplicar CSS global
        scene.getStylesheets().add(
    getClass().getResource("/co/edu/prog3/ui/style.css").toExternalForm()
);


        stage.setScene(scene);
        stage.setTitle(bundle.getString("title") + " - " + bundle.getLocale().getDisplayLanguage());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
