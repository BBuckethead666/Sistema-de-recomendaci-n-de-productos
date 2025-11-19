package co.edu.prog3.ui;

import co.edu.prog3.model.Product;
import co.edu.prog3.model.ProductGraph;
import co.edu.prog3.persistence.ProductJsonDao;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador encargado del formulario para crear y registrar nuevos productos.
 * Permite ingresar datos del producto, generar un ID automáticamente y guardarlo
 * en el archivo JSON correspondiente.
 */
public class ProductFormController {

    /** Campo de texto para ingresar el nombre del producto. */
    @FXML
    private TextField nameField;

    /** Campo de texto para ingresar el precio del producto. */
    @FXML
    private TextField priceField;

    /** Campo de texto para ingresar la categoría del producto. */
    @FXML
    private TextField categoryField;

    /** Campo de texto para ingresar la marca del producto. */
    @FXML
    private TextField brandField;

    /** Campo de texto para ingresar o seleccionar la ruta de la imagen del producto. */
    @FXML
    private TextField imagePathField;

    /** Grafo que almacena y gestiona los productos. */
    private ProductGraph graph;

    /** Ventana actual del formulario. */
    private Stage stage;

    /**
     * Establece la ventana asociada al formulario.
     *
     * @param stage ventana del formulario.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Asigna el grafo donde se almacenarán los productos.
     *
     * @param graph estructura de datos que maneja los productos.
     */
    public void setGraph(ProductGraph graph) {
        this.graph = graph;
    }

    /**
     * Guarda un nuevo producto utilizando los datos ingresados en el formulario.
     * Genera automáticamente un ID, crea el objeto y lo registra en el grafo.
     * Finalmente lo persiste en un archivo JSON y cierra la ventana.
     */
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
