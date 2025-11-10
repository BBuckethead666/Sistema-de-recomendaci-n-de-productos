package co.edu.prog3.ui;

import co.edu.prog3.model.Product;
import co.edu.prog3.model.ProductGraph;
import co.edu.prog3.persistence.ProductJsonDao;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, String> colBrand;

    @FXML private Menu menuFile;
    @FXML private MenuItem menuSave;
    @FXML private MenuItem menuExit;
    @FXML private Menu menuLanguage;
    @FXML private MenuItem menuSpanish;
    @FXML private MenuItem menuEnglish;
    @FXML private Menu menuActions;
    @FXML private MenuItem menuAdd;
    @FXML private MenuItem menuEdit;
    @FXML private MenuItem menuDelete;
    @FXML private MenuItem menuLoad;

    private final ProductJsonDao dao = new ProductJsonDao();
    private ProductGraph graph = new ProductGraph();
    private final String FILE = "productos.json";

    private ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    @FXML
    private void initialize() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getId()));
        colName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getName()));
        colPrice.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getPrice()));
        colCategory.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategory()));
        colBrand.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getBrand()));

        applyTranslations();
    }

    @FXML
    private void handleLoadProducts() {
        try {
            File file = new File(FILE);
            if (file.exists()) {
                graph = dao.load(FILE);
            } else {
                saveGraphSafe();
            }
            productTable.setItems(FXCollections.observableArrayList(graph.listProducts()));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo leer el archivo JSON: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddProduct() {
        openProductForm(null, bundle.getString("add"));
    }

    @FXML
    private void handleEditProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openProductForm(selected, bundle.getString("edit"));
        } else {
            showAlert("Error", "Selecciona un producto para editar.");
        }
    }

    @FXML
    private void handleDeleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            graph.removeProduct(selected.getId());
            saveGraphSafe();
            productTable.setItems(FXCollections.observableArrayList(graph.listProducts()));
        } else {
            showAlert("Error", "Selecciona un producto para eliminar.");
        }
    }

    @FXML
    private void handleSave() {
        saveGraphSafe();
        showInfo("Info", "Productos guardados correctamente.");
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void switchToSpanish() {
        bundle = ResourceBundle.getBundle("messages", new Locale("es"));
        applyTranslations();
    }

    @FXML
    private void switchToEnglish() {
        bundle = ResourceBundle.getBundle("messages", new Locale("en"));
        applyTranslations();
    }

    private void openProductForm(Product product, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductForm.fxml"), bundle);
            Parent root = loader.load();

            ProductFormController controller = loader.getController();
            controller.setProduct(product);
            controller.applyTranslations(bundle);

            Stage dialog = new Stage();
            dialog.setTitle(title);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.showAndWait();

            if (controller.isSaved()) {
                Product p = controller.getProduct();
                if (product != null) {
                    graph.removeProduct(product.getId());
                }
                graph.addProduct(p);
                saveGraphSafe();
                productTable.setItems(FXCollections.observableArrayList(graph.listProducts()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el formulario: " + e.getMessage());
        }
    }

        private void applyTranslations() {
            menuFile.setText(bundleSafe("menu.file", "Archivo"));
            menuSave.setText(bundleSafe("menu.save", "Guardar"));
            menuExit.setText(bundleSafe("menu.exit", "Salir"));

            menuActions.setText(bundleSafe("menu.actions", "Acciones"));
            menuAdd.setText(bundleSafe("add", "Agregar"));
            menuEdit.setText(bundleSafe("edit", "Editar"));
            menuDelete.setText(bundleSafe("delete", "Eliminar"));
            menuLoad.setText(bundleSafe("load", "Cargar Productos"));

            menuLanguage.setText(bundleSafe("menu.language", "Idioma"));
            menuSpanish.setText(bundleSafe("menu.spanish", "Español"));
            menuEnglish.setText(bundleSafe("menu.english", "Inglés"));
        }

    private String bundleSafe(String key, String fallback) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return fallback;
        }
    }

    private void saveGraphSafe() {
        try {
            dao.save(FILE, graph);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo guardar el archivo JSON: " + e.getMessage());
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
