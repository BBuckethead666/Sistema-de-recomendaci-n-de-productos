package co.edu.prog3;

import co.edu.prog3.ui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class MainApp extends Application {

    @Override
public void start(Stage primaryStage) throws Exception {
    ResourceBundle bundle = ResourceBundle.getBundle("co.edu.prog3.ui.messages", Locale.getDefault());
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/prog3/ui/MainView.fxml"), bundle);
    Parent root = loader.load();

    MainController controller = loader.getController();
    controller.setStage(primaryStage); // inyectamos el Stage

    Scene scene = new Scene(root);
    primaryStage.setTitle(bundle.getString("main.title"));
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true); // o setFullScreen(true)
    primaryStage.show();
}


    public static void main(String[] args) {
        launch(args);
    }
}
