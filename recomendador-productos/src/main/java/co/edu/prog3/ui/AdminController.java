package co.edu.prog3.ui;

import co.edu.prog3.model.Product;
import co.edu.prog3.model.ProductGraph;
import co.edu.prog3.persistence.ProductJsonDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador encargado de gestionar la vista de administración de productos.
 * Permite agregar, actualizar, eliminar y visualizar productos registrados
 * en el grafo de productos {@link ProductGraph}.
 */
public class AdminController {

    /** Lista de productos visibles en la interfaz. */
    @FXML private ListView<String> productList;

    /** Campo de texto para el nombre del producto. */
    @FXML private TextField nameField;

    /** Campo de texto para el precio del producto. */
    @FXML private TextField priceField;

    /** Campo de texto para la categoría del producto. */
    @FXML private TextField categoryField;

    /** Campo de texto para la marca del producto. */
    @FXML private TextField brandField;

    /** Campo de texto para la ruta de la imagen del producto. */
    @FXML private TextField imagePathField;

    /** Estructura que almacena y gestiona los productos. */
    private ProductGraph graph;

    /** Ventana principal donde se mostrarán las vistas. */
    private Stage stage;

    /**
     * Establece la ventana principal.
     *
     * @param stage ventana principal de la aplicación
     */
    public void setStage(Stage stage) { this.stage = stage; }

    /**
     * Inicializa el controlador cargando los datos desde archivo JSON,
     * creando el grafo si no existe e inicializando los eventos de selección.
     */
    @FXML
    public void initialize() {
        try {
            ProductJsonDao dao = new ProductJsonDao();
            File externalFile = new File("productos_data.json");

            if (externalFile.exists()) {
                graph = dao.load("productos_data.json");
            } else {
                graph = dao.load("co/edu/prog3/ui/productos.json");
                dao.save("productos_data.json", graph);
            }
            graph.setIdPrefix("ITEM");
        } catch (IOException e) {
            e.printStackTrace();
            graph = new ProductGraph();
            graph.setIdPrefix("ITEM");
        }
        refreshList();

        productList.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                String id = selected.split(" - ")[0];
                Product p = graph.getProduct(id);
                if (p != null) {
                    nameField.setText(p.getName());
                    priceField.setText(String.valueOf(p.getPrice()));
                    categoryField.setText(p.getCategory());
                    brandField.setText(p.getBrand());
                    imagePathField.setText(p.getImagePath());
                }
            }
        });
    }

    /**
     * Actualiza la lista visual de productos con los datos del grafo.
     */
    private void refreshList() {
        if (productList == null || graph == null) return;
        productList.getItems().clear();
        if (graph.listProducts() != null) {
            for (Product p : graph.listProducts()) {
                String nombre = (p.getName() != null) ? p.getName() : "(sin nombre)";
                double precio = p.getPrice();
                productList.getItems().add(p.getId() + " - " + nombre + " ($" + precio + ")");
            }
        }
    }

    /**
     * Agrega un nuevo producto basado en los campos del formulario
     * y lo guarda en el archivo JSON.
     */
    @FXML
    private void addProduct() {
        try {
            String id = graph.generateId();
            Product newProduct = new Product(
                id,
                nameField.getText(),
                Double.parseDouble(priceField.getText()),
                categoryField.getText(),
                brandField.getText(),
                imagePathField.getText().isEmpty() ? "images/placeholder.png" : imagePathField.getText()
            );
            graph.addProduct(newProduct);

            ProductJsonDao dao = new ProductJsonDao();
            dao.save("productos_data.json", graph);

            refreshList();
            clearForm();
            showInfo("alert.productAdded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza los datos del producto seleccionado en la lista.
     */
    @FXML
    private void updateProduct() {
        String selected = productList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String id = selected.split(" - ")[0];
            Product p = graph.getProduct(id);
            if (p != null) {
                try {
                    p.setName(nameField.getText());
                    p.setPrice(Double.parseDouble(priceField.getText()));
                    p.setCategory(categoryField.getText());
                    p.setBrand(brandField.getText());
                    p.setImagePath(imagePathField.getText().isEmpty() ? "images/placeholder.png" : imagePathField.getText());

                    ProductJsonDao dao = new ProductJsonDao();
                    dao.save("productos_data.json", graph);

                    refreshList();
                    clearForm();
                    showInfo("alert.productUpdated");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Elimina un producto seleccionado después de confirmar la acción con el usuario.
     */
    @FXML
    private void deleteProduct() {
        String selected = productList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            ResourceBundle bundle = ResourceBundle.getBundle("co.edu.prog3.ui.messages", Locale.getDefault());
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle(bundle.getString("alert.confirmTitle"));
            confirm.setHeaderText(null);
            confirm.setContentText(bundle.getString("alert.confirmDelete"));
            Optional<javafx.scene.control.ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
                String id = selected.split(" - ")[0];
                graph.removeProduct(id);
                try {
                    ProductJsonDao dao = new ProductJsonDao();
                    dao.save("productos_data.json", graph);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                refreshList();
                clearForm();
                showInfo("alert.productDeleted");
            }
        }
    }

    /**
     * Regresa a la vista principal restaurando el estado previo de pantalla.
     */
    @FXML
    private void goBack() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("co.edu.prog3.ui.messages", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/prog3/ui/MainView.fxml"), bundle);
            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.setStage(stage);

            boolean wasFullScreen = stage.isFullScreen();
            boolean wasMaximized = stage.isMaximized();

            Scene scene = stage.getScene();
            if (scene == null) {
                scene = new Scene(root);
                stage.setScene(scene);
            } else {
                scene.setRoot(root);
            }

            stage.setTitle(bundle.getString("main.title"));
            stage.setFullScreen(wasFullScreen);
            stage.setMaximized(wasMaximized);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Limpia los campos del formulario y la selección de la lista.
     */
    private void clearForm() {
        nameField.clear();
        priceField.clear();
        categoryField.clear();
        brandField.clear();
        imagePathField.clear();
        productList.getSelectionModel().clearSelection();
    }

    /**
     * Muestra un mensaje informativo mediante un alert.
     *
     * @param key clave del mensaje a mostrar desde el archivo de recursos
     */
    private void showInfo(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle("co.edu.prog3.ui.messages", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("alert.infoTitle"));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString(key));
        alert.showAndWait();
    }
}
