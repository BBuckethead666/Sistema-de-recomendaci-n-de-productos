package co.edu.prog3.ui;

import co.edu.prog3.model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class ProductFormController {

    @FXML private Label lblId;
    @FXML private Label lblName;
    @FXML private Label lblPrice;
    @FXML private Label lblCategory;
    @FXML private Label lblBrand;
    @FXML private Button btnSave;

    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtPrice;
    @FXML private TextField txtCategory;
    @FXML private TextField txtBrand;

    private Product product;
    private boolean saved = false;

    public void setProduct(Product p) {
        this.product = p;
        if (p != null) {
            txtId.setText(p.getId());
            txtName.setText(p.getName());
            txtPrice.setText(String.valueOf(p.getPrice()));
            txtCategory.setText(p.getCategory());
            txtBrand.setText(p.getBrand());
        }
    }

    public boolean isSaved() {
        return saved;
    }

    public Product getProduct() {
        return product;
    }

    @FXML
    private void handleSave() {
        product = new Product(
            txtId.getText(),
            txtName.getText(),
            Double.parseDouble(txtPrice.getText()),
            txtCategory.getText(),
            txtBrand.getText()
        );
        saved = true;
        Stage stage = (Stage) txtId.getScene().getWindow();
        stage.close();
    }

    public void applyTranslations(ResourceBundle bundle) {
        lblId.setText(bundle.getString("id"));
        lblName.setText(bundle.getString("name"));
        lblPrice.setText(bundle.getString("price"));
        lblCategory.setText(bundle.getString("category"));
        lblBrand.setText(bundle.getString("brand"));
        btnSave.setText(bundle.getString("save"));
    }
}
