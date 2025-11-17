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

public class AdminController {

    @FXML private ListView<String> productList;
    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField categoryField;
    @FXML private TextField brandField;
    @FXML private TextField imagePathField;

    private ProductGraph graph;
    private Stage stage;

    public void setStage(Stage stage) { this.stage = stage; }

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

        // 🔑 Listener para cargar datos al seleccionar
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

    /** Refresca la lista de productos en el ListView */
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

    @FXML
    private void addProduct() {
        try {
            String id = graph.generateId();
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            String category = categoryField.getText();
            String brand = brandField.getText();
            String imagePath = imagePathField.getText().isEmpty() ? "images/placeholder.png" : imagePathField.getText();

            Product newProduct = new Product(id, name, price, category, brand, imagePath);
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

    /** 🔑 Método para actualizar producto existente */
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

    private void clearForm() {
        nameField.clear();
        priceField.clear();
        categoryField.clear();
        brandField.clear();
        imagePathField.clear();
        productList.getSelectionModel().clearSelection();
    }

    /** 🔑 Método auxiliar para mostrar notificaciones internacionalizadas */
    private void showInfo(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle("co.edu.prog3.ui.messages", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("alert.infoTitle"));
        alert.setHeaderText(null);
        alert.setContentText(bundle.getString(key));
        alert.showAndWait();
    }
}
