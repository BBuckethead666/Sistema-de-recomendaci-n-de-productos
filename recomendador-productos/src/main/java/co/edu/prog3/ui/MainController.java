package co.edu.prog3.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // ✅ Abrir tienda
    @FXML
    private void openStoreView() {
        loadView("/co/edu/prog3/ui/StoreView.fxml", "store.title");
    }

    // ✅ Abrir administrador
    @FXML
    private void openAdminView() {
        loadView("/co/edu/prog3/ui/AdminView.fxml", "admin.title");
    }

    // ✅ Cambiar idioma a español
    @FXML
    private void setSpanish() {
        Locale.setDefault(new Locale("es", "CO"));
        reloadMainView();
    }

    // ✅ Cambiar idioma a inglés
    @FXML
    private void setEnglish() {
        Locale.setDefault(Locale.ENGLISH);
        reloadMainView();
    }

    // ✅ Recargar vista principal con idioma actualizado
    private void reloadMainView() {
        loadView("/co/edu/prog3/ui/MainView.fxml", "main.title");
    }

    // ✅ Método central para cargar vistas
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

            // 🔑 En vez de crear un Scene nuevo, reemplazamos el root
            Scene scene = stage.getScene();
            if (scene == null) {
                scene = new Scene(root);
                stage.setScene(scene);
            } else {
                scene.setRoot(root);
            }

            stage.setTitle(bundle.getString(titleKey));

            // 🔑 Restaurar estado
            stage.setFullScreen(wasFullScreen);
            stage.setMaximized(wasMaximized);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
