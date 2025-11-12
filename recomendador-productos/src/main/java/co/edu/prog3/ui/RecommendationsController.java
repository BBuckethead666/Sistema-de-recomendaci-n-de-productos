package co.edu.prog3.ui;

import co.edu.prog3.model.Product;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.ResourceBundle;

public class RecommendationsController {

    @FXML private Label lblTitle;
    @FXML private TableView<Product> recommendTable;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, String> colBrand;
    @FXML private TableColumn<Product, Double> colPrice;

    public void setRecommendations(Product base, List<Product> recs, ResourceBundle bundle) {
    lblTitle.setText(bundle.getString("recommend.title") + " " + base.getName());

    colName.setText(bundle.getString("name"));
    colCategory.setText(bundle.getString("category"));
    colBrand.setText(bundle.getString("brand"));
    colPrice.setText(bundle.getString("price"));

    if (recs.isEmpty()) {
        lblTitle.setText(bundle.getString("recommend.title") + " " + base.getName()
                         + " (" + bundle.getString("no.recommendations") + ")");
    } else {
        recommendTable.setItems(FXCollections.observableArrayList(recs));
    }
}
    
}
