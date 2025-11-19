package co.edu.prog3.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controlador principal de la aplicación. Se encarga de gestionar
 * la navegación entre vistas, el cambio de idioma y la configuración
 * inicial del escenario (Stage) principal.
 */
public class MainController {

    /**
     * Ventana principal de la aplicación.
     */
    private Stage stage;

    /**
     * Asigna el escenario principal utilizado por la aplicación.
     *
     * @param stage instancia de la ventana principal
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Abre la vista de la tienda y la carga en la ventana principal.
     */
    @FXML
    private void openStoreView() {
        loadView("/co/edu/prog3/ui/StoreView.fxml", "store.title");
    }

    /**
     * Abre la vista del panel de administración.
     */
    @FXML
    private void openAdminView() {
        loadView("/co/edu/prog3/ui/AdminView.fxml", "admin.title");
    }

    /**
     * Cambia el idioma de la aplicación a español (Colombia)
     * y recarga la vista principal.
     */
    @FXML
    private void setSpanish() {
        Locale.setDefault(new Locale("es", "CO"));
        reloadMainView();
    }

    /**
     * Cambia el idioma de la aplicación a inglés
     * y recarga la vista principal.
     */
    @FXML
    private void setEnglish() {
        Locale.setDefault(Locale.ENGLISH);
        reloadMainView();
    }

    /**
     * Recarga la vista principal para aplicar el idioma seleccionado.
     */
    private void reloadMainView() {
        loadView("/co/edu/prog3/ui/MainView.fxml", "main.title");
    }

    /**
     * Carga una vista FXML, asigna su respectivo controlador,
     * actualiza el contenido del Scene y mantiene el estado
     * de la ventana (pantalla completa o maximizada).
     *
     * @param fxmlFile ruta del archivo FXML a cargar
     * @param titleKey clave del título localizada mediante ResourceBundle
     */
    private void loadView(String fxmlFile, String titleKey) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("co.edu.prog3.ui.messages", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile), bundle);
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof MainController) {
                ((MainController) controller).setStage(stage);
            } else if (controller instanceof StoreController) {
                ((StoreController) controller).setStage(stage);
            } else if (controller instanceof AdminController) {
                ((AdminController) controller).setStage(stage);
            }

            // 🔑 Guardar estado de pantalla completa/maximizado
            boolean wasFullScreen = stage.isFullScreen();
            boolean wasMaximized = stage.isMaximized();

            // 🔑 Reemplazar el root de la escena existente
            Scene scene = stage.getScene();
            if (scene == null) {
                scene = new Scene(root);
                stage.setScene(scene);
            } else {
                scene.setRoot(root);
            }

            stage.setTitle(bundle.getString(titleKey));

            // 🔑 Restaurar estado previo
            stage.setFullScreen(wasFullScreen);
            stage.setMaximized(wasMaximized);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
