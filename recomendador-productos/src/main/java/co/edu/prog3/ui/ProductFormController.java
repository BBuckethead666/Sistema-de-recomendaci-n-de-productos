package co.edu.prog3.ui;

import co.edu.prog3.model.Product;
import co.edu.prog3.model.ProductGraph;
import co.edu.prog3.persistence.ProductJsonDao;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ProductFormController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField brandField;
    @FXML
    private TextField imagePathField;

    private ProductGraph graph;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setGraph(ProductGraph graph) {
        this.graph = graph;
    }

    @FXML
    private void saveProduct() {
        try {
            // ✅ Generar ID automático con prefijo configurable
            String id = graph.generateId();
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            String category = categoryField.getText();
            String brand = brandField.getText();
            String imagePath = imagePathField.getText().isEmpty() ? "images/placeholder.png" : imagePathField.getText();

            Product newProduct = new Product(id, name, price, category, brand, imagePath);
            graph.addProduct(newProduct);

            ProductJsonDao dao = new ProductJsonDao();
            dao.save("productos.json", graph);

            stage.close(); // cerrar formulario
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
