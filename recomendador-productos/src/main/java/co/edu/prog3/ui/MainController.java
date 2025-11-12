package co.edu.prog3.ui;

import co.edu.prog3.model.Product;
import co.edu.prog3.model.ProductGraph;
import co.edu.prog3.persistence.ProductJsonDao;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController {

    // ===== Tabla central =====
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, Double> colPrice;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, String> colBrand;

    // ===== Botones inferiores =====
    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnRecommend;
    @FXML private Button btnApplyFilters;

    // ===== Menús =====
    @FXML private Menu menuFile;
    @FXML private MenuItem menuSave;
    @FXML private MenuItem menuExit;
    @FXML private Menu menuActions;
    @FXML private MenuItem menuAdd;
    @FXML private MenuItem menuEdit;
    @FXML private MenuItem menuDelete;
    @FXML private MenuItem menuLoad;

    // ===== Switch idioma =====
   

    // ===== Filtros =====
    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbCategory;
    @FXML private ComboBox<String> cmbBrand;

    // ===== Estado inferior =====
    @FXML private Label lblStatus;

    // ===== Modelo =====
    private final ProductJsonDao dao = new ProductJsonDao();
    private ProductGraph graph = new ProductGraph();
    private final String FILE = "productos.json";

    private ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    // ===== Inicialización =====
    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colPrice.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPrice()));
        colCategory.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        colBrand.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBrand()));

        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadProductsOnStartup();
        updateFilterOptions();
    }

    // ===== Filtros =====
    private void updateFilterOptions() {
        var products = graph.listProducts();
        var categories = products.stream().map(Product::getCategory).distinct().toList();
        cmbCategory.setItems(FXCollections.observableArrayList(categories));
        var brands = products.stream().map(Product::getBrand).distinct().toList();
        cmbBrand.setItems(FXCollections.observableArrayList(brands));
    }
    @FXML
    private void switchToSpanish() {
        bundle = ResourceBundle.getBundle("messages", new Locale("es"));
        applyTranslations(bundle);
    }

    @FXML
    private void switchToEnglish() {
        bundle = ResourceBundle.getBundle("messages", new Locale("en"));
        applyTranslations(bundle);
    }


    @FXML
    private void handleApplyFilters() {
        String search = txtSearch.getText().toLowerCase();
        String category = cmbCategory.getValue();
        String brand = cmbBrand.getValue();

        var filtered = graph.listProducts().stream()
            .filter(p -> search.isEmpty() || p.getName().toLowerCase().contains(search))
            .filter(p -> category == null || p.getCategory().equalsIgnoreCase(category))
            .filter(p -> brand == null || p.getBrand().equalsIgnoreCase(brand))
            .toList();

        productTable.setItems(FXCollections.observableArrayList(filtered));
        lblStatus.setText(bundleSafe("status.filtered", "Mostrando ") + filtered.size() + " productos");
    }

    // ===== CRUD =====
    @FXML
    private void handleAddProduct() {
        openProductForm(null, bundleSafe("menu.add", "Añadir Producto"));
    }

    @FXML
    private void handleEditProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openProductForm(selected, bundleSafe("menu.edit", "Editar Producto"));
        } else {
            showAlert(bundleSafe("error.select.title", "Error"),
                      bundleSafe("error.select.msg", "Selecciona un producto para editar."));
        }
    }

    @FXML
    private void handleDeleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle(bundleSafe("confirm.delete.title", "Confirmar eliminación"));
            confirm.setHeaderText(null);
            confirm.setContentText(bundleSafe("confirm.delete.msg", "¿Seguro que deseas eliminar el producto?"));

            var result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                graph.removeProduct(selected.getId());
                saveGraphSafe();
                productTable.setItems(FXCollections.observableArrayList(graph.listProducts()));
                showInfo(bundleSafe("info.delete.title", "Información"),
                         bundleSafe("info.delete.msg", "Producto eliminado correctamente."));
            }
        } else {
            showAlert(bundleSafe("error.select.title", "Error"),
                      bundleSafe("error.select.msg", "Selecciona un producto para eliminar."));
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
    private void handleLoadProducts() {
        loadProductsOnStartup();
    }

    // ===== Formulario modal =====
    private void openProductForm(Product product, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductForm.fxml"), bundle);
            Parent root = loader.load();

            ProductFormController controller = loader.getController();
            controller.setGraph(graph);
            controller.setDao(dao);
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
                updateFilterOptions();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo abrir el formulario: " + e.getMessage());
        }
    }

    // ===== Recomendaciones =====
    @FXML
    private void handleRecommend() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(bundleSafe("error.select.title", "Error"),
                      bundleSafe("error.select.msg", "Selecciona un producto para recomendar."));
            return;
        }

        var recommendations = graph.listProducts().stream()
            .filter(p -> !p.getId().equals(selected.getId()))
            .map(p -> new ScoredProduct(p, scoreProduct(selected, p)))
            .filter(sp -> sp.score > 0)
            .sorted((a, b) -> Double.compare(b.score, a.score))
            .map(sp -> sp.product)
            .limit(5)
            .toList();

        showRecommendationsModal(selected, recommendations);
    }

    private double scoreProduct(Product base, Product other) {
        double score = 0.0;
        if (base.getCategory().equalsIgnoreCase(other.getCategory())) score += 5.0;
        if (base.getBrand().equalsIgnoreCase(other.getBrand())) score += 3.0;
        double diff = Math.abs(base.getPrice() - other.getPrice());
        double relative = diff / Math.max(base.getPrice(), other.getPrice());
        score += (1.0 - relative) * 2.0;
        return score;
    }

    private static class ScoredProduct {
        Product product;
        double score;
        ScoredProduct(Product p, double s) { this.product = p; this.score = s; }
    }

    // ===== Internacionalización =====
   

        private void applyTranslations(ResourceBundle bundle) {
        menuFile.setText(bundle.getString("menu.file"));
        menuActions.setText(bundle.getString("menu.actions"));
        menuSave.setText(bundle.getString("menu.save"));
        menuExit.setText(bundle.getString("menu.exit"));
        menuAdd.setText(bundle.getString("menu.add"));
        menuEdit.setText(bundle.getString("menu.edit"));
        menuDelete.setText(bundle.getString("menu.delete"));
        menuLoad.setText(bundle.getString("menu.load"));

    

        // Botones inferiores
        btnAdd.setText(bundle.getString("button.add"));
        btnEdit.setText(bundle.getString("button.edit"));
        btnRecommend.setText(bundle.getString("button.recommend"));
        btnApplyFilters.setText(bundle.getString("filter.apply"));

        // Columnas de la tabla
        colId.setText(bundle.getString("column.id"));
        colName.setText(bundle.getString("column.name"));
        colPrice.setText(bundle.getString("column.price"));
        colCategory.setText(bundle.getString("column.category"));
        colBrand.setText(bundle.getString("column.brand"));

        // Placeholders de filtros
        txtSearch.setPromptText(bundle.getString("filter.search"));
        cmbCategory.setPromptText(bundle.getString("filter.category"));
        cmbBrand.setPromptText(bundle.getString("filter.brand"));

        // Estado inferior
        lblStatus.setText(bundle.getString("status.total") + graph.listProducts().size());

        // Título de la ventana
        Stage stage = (Stage) productTable.getScene().getWindow();
        stage.setTitle(bundle.getString("title"));
    }

    // ===== Métodos auxiliares =====
    private String bundleSafe(String key, String fallback) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return fallback;
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

    private void showRecommendationsModal(Product base, List<Product> recommendations) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundleSafe("recommend.title", "Recomendaciones"));
        alert.setHeaderText(bundleSafe("recommend.header", "Productos recomendados para ") + base.getName());
        StringBuilder sb = new StringBuilder();
        for (Product p : recommendations) {
            sb.append(p.getName()).append(" - ").append(p.getBrand()).append("\n");
        }
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }

    private void saveGraphSafe() {
        try {
            dao.save(FILE, graph);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo guardar el archivo JSON: " + e.getMessage());
        }
    }

    private void loadProductsOnStartup() {
        try {
            File file = new File(FILE);
            if (file.exists()) {
                graph = dao.load(FILE);
            } else {
                saveGraphSafe();
            }
            productTable.setItems(FXCollections.observableArrayList(graph.listProducts()));
            lblStatus.setText(bundleSafe("status.total", "Total productos: ") + graph.listProducts().size());
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(bundleSafe("error.load.title", "Error de carga"),
                      bundleSafe("error.load.msg", "No se pudo leer el archivo JSON"));
        }
    }
}
