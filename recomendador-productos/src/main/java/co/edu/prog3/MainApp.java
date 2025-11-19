package co.edu.prog3;

import co.edu.prog3.ui.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Clase principal de la aplicación JavaFX.
 * Se encarga de cargar el archivo FXML, inicializar el controlador,
 * configurar la ventana principal y lanzar la interfaz gráfica.
 */
public class MainApp extends Application {

    /**
     * Método que se ejecuta al iniciar la aplicación JavaFX.
     * Carga los recursos de idioma, el archivo FXML, inyecta el Stage
     * en el controlador y configura la ventana principal.
     *
     * @param primaryStage ventana principal de la aplicación.
     * @throws Exception si ocurre un error al cargar el FXML o recursos.
     */
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

    /**
     * Método principal que ejecuta la aplicación.
     *
     * @param args argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
