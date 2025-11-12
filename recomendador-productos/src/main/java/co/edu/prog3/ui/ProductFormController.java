package co.edu.prog3.ui;

import co.edu.prog3.model.Product;
import co.edu.prog3.model.ProductGraph;
import co.edu.prog3.persistence.ProductJsonDao;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class ProductFormController {

    // ===== Etiquetas =====
    @FXML private Label lblId;
    @FXML private Label lblName;
    @FXML private Label lblPrice;
    @FXML private Label lblCategory;
    @FXML private Label lblBrand;

    // ===== Campos =====
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtPrice;
    @FXML private TextField txtCategory;
    @FXML private TextField txtBrand;

    // ===== Botones =====
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    // ===== Estado =====
    private ProductGraph graph;
    private ProductJsonDao dao;
    private Product product;
    private boolean saved = false; // 👈 flag para saber si se guardó

    // ===== Setters =====
    public void setGraph(ProductGraph graph) { this.graph = graph; }
    public void setDao(ProductJsonDao dao) { this.dao = dao; }
    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            txtId.setText(product.getId());
            txtName.setText(product.getName());
            txtPrice.setText(String.valueOf(product.getPrice()));
            txtCategory.setText(product.getCategory());
            txtBrand.setText(product.getBrand());
        }
    }

    // ===== Métodos que faltaban =====
    public boolean isSaved() {
        return saved;
    }

    public Product getProduct() {
        return product;
    }

    // ===== Traducciones =====
    public void applyTranslations(ResourceBundle bundle) {
        lblId.setText(bundle.getString("form.id"));
        lblName.setText(bundle.getString("form.name"));
        lblPrice.setText(bundle.getString("form.price"));
        lblCategory.setText(bundle.getString("form.category"));
        lblBrand.setText(bundle.getString("form.brand"));

        btnSave.setText(bundle.getString("form.save"));
        btnCancel.setText(bundle.getString("form.cancel"));
    }

    // ===== Handlers =====
    @FXML
    private void handleSave() {
        if (product == null) {
            product = new Product(
                txtId.getText(),
                txtName.getText(),
                Double.parseDouble(txtPrice.getText()),
                txtCategory.getText(),
                txtBrand.getText()
            );
        } else {
            product.setName(txtName.getText());
            product.setPrice(Double.parseDouble(txtPrice.getText()));
            product.setCategory(txtCategory.getText());
            product.setBrand(txtBrand.getText());
        }

        saved = true; // 👈 marcar que se guardó

        try {
            dao.save("productos.json", graph);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        saved = false; // 👈 no se guardó
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
