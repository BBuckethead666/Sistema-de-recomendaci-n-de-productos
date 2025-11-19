package co.edu.prog3.ui;

import co.edu.prog3.model.Product;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador encargado de administrar la vista de recomendaciones.
 * Muestra una lista de productos recomendados basados en un producto seleccionado.
 */
public class RecommendationsController {

    /** Etiqueta que muestra el título de la sección de recomendaciones. */
    @FXML private Label lblTitle;

    /** Tabla donde se listan los productos recomendados. */
    @FXML private TableView<Product> recommendTable;

    /** Columna que muestra el nombre del producto recomendado. */
    @FXML private TableColumn<Product, String> colName;

    /** Columna que muestra la categoría del producto recomendado. */
    @FXML private TableColumn<Product, String> colCategory;

    /** Columna que muestra la marca del producto recomendado. */
    @FXML private TableColumn<Product, String> colBrand;

    /** Columna que muestra el precio del producto recomendado. */
    @FXML private TableColumn<Product, Double> colPrice;

    /**
     * Configura y muestra las recomendaciones basadas en un producto determinado.
     *
     * @param base   producto base del cual se derivan las recomendaciones.
     * @param recs   lista de productos recomendados.
     * @param bundle archivo de recursos para internacionalización.
     */
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
