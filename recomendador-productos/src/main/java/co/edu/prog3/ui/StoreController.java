package co.edu.prog3.ui;

import co.edu.prog3.model.Product;
import co.edu.prog3.model.ProductGraph;
import co.edu.prog3.persistence.CarritoJsonDao;
import co.edu.prog3.persistence.ProductJsonDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador encargado de gestionar la vista de la tienda.
 * Administra la carga del catálogo, la visualización de productos,
 * el manejo del carrito de compras y la navegación hacia otras vistas.
 */
public class StoreController {

    /** Contenedor en cuadrícula donde se muestran los productos del catálogo. */
    @FXML private GridPane catalogGrid;

    /** Lista visual donde se presentan los productos añadidos al carrito. */
    @FXML private ListView<String> cartList;

    /** Etiqueta que muestra el total acumulado del carrito. */
    @FXML private Label totalLabel;

    /** Grafo que contiene los productos y sus relaciones de recomendación. */
    private ProductGraph graph;

    /** Lista que almacena los productos agregados al carrito. */
    private List<Product> carrito;

    /** Ventana principal usada para navegación. */
    private Stage stage;

    /**
     * Establece el Stage principal para permitir navegación entre vistas.
     *
     * @param stage ventana principal.
     */
    public void setStage(Stage stage) { this.stage = stage; }

    /**
     * Inicializa el controlador.
     * Carga los productos desde un archivo JSON, genera el catálogo visual
     * y actualiza el total del carrito.
     */
    @FXML
    public void initialize() {
        carrito = new ArrayList<>();
        try {
            ProductJsonDao dao = new ProductJsonDao();
            File externalFile = new File("productos_data.json");

            if (externalFile.exists()) {
                graph = dao.load("productos_data.json");
            } else {
                graph = dao.load("co/edu/prog3/ui/productos.json");
                dao.save("productos_data.json", graph);
            }
        } catch (IOException e) {
            e.printStackTrace();
            graph = new ProductGraph();
        }
        loadCatalog();
        updateTotal();
    }

    /**
     * Carga visualmente todos los productos en el catálogo,
     * generando tarjetas individuales dentro del GridPane.
     */
    private void loadCatalog() {
        catalogGrid.getChildren().clear();
        int col = 0, row = 0;
        for (Product product : graph.listProducts()) {
            VBox card = createProductCard(product);
            catalogGrid.add(card, col, row);
            col++;
            if (col > 2) { col = 0; row++; }
        }
    }

    /**
     * Crea una tarjeta visual (VBox) para mostrar la información de un producto,
     * su imagen, precio y botones de acciones como agregar al carrito
     * y ver recomendaciones relacionadas.
     *
     * @param product producto que será mostrado.
     * @return un contenedor VBox con el diseño de la tarjeta.
     */
    private VBox createProductCard(Product product) {
        ResourceBundle bundle = ResourceBundle.getBundle("co.edu.prog3.ui.messages", Locale.getDefault());

        VBox box = new VBox(10);
        box.getStyleClass().add("product-card");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        try {
            if (product.getImagePath() != null && !product.getImagePath().isBlank()) {
                URL resource = getClass().getResource(product.getImagePath());
                if (resource != null) {
                    imageView.setImage(new Image(resource.toExternalForm()));
                } else {
                    File file = new File(product.getImagePath());
                    if (file.exists()) {
                        imageView.setImage(new Image(file.toURI().toString()));
                    } else {
                        imageView.setImage(new Image(getClass().getResource("images/placeholder.png").toExternalForm()));
                    }
                }
            } else {
                imageView.setImage(new Image(getClass().getResource("images/placeholder.png").toExternalForm()));
            }
        } catch (Exception e) {
            imageView.setImage(new Image(getClass().getResource("images/placeholder.png").toExternalForm()));
        }

        Label nameLabel = new Label(product.getName());
        Label priceLabel = new Label(bundle.getString("label.price") + ": $" + product.getPrice());

        Button addButton = new Button(bundle.getString("button.addToCart"));
        addButton.setOnAction(event -> addToCart(product));

        box.getChildren().addAll(imageView, nameLabel, priceLabel, addButton);

        List<Product> recomendados = graph.neighbors(product.getId());
        if (!recomendados.isEmpty()) {
            Label recLabel = new Label(bundle.getString("label.recommended"));
            VBox recBox = new VBox(5);
            recBox.getStyleClass().add("recommendations");

            recomendados.stream().limit(3).forEach(r -> {
                HBox recItemBox = new HBox(5);

                Label recItem = new Label(r.getName());

                Button addRecButton = new Button(bundle.getString("button.addToCart"));
                addRecButton.setOnAction(event -> addToCart(r));

                Button viewRecButton = new Button(bundle.getString("button.viewProduct"));
                viewRecButton.setOnAction(event -> showProductDetails(r));

                recItemBox.getChildren().addAll(recItem, addRecButton, viewRecButton);
                recBox.getChildren().add(recItemBox);
            });

            box.getChildren().addAll(recLabel, recBox);
        }

        return box;
    }

    /**
     * Agrega un producto al carrito, actualiza la lista visual,
     * recalcula el total y guarda el carrito en un archivo JSON.
     *
     * @param product producto a agregar.
     */
    private void addToCart(Product product) {
        carrito.add(product);
        cartList.getItems().add(product.getName() + " - $" + product.getPrice());
        updateTotal();
        try {
            CarritoJsonDao dao = new CarritoJsonDao();
            dao.save("carrito.json", carrito);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Limpia el carrito con confirmación del usuario.
     * Actualiza la vista y guarda el carrito vacío en JSON.
     */
    @FXML
    private void clearCart() {
        ResourceBundle bundle = ResourceBundle.getBundle("co.edu.prog3.ui.messages", Locale.getDefault());

        if (carrito.isEmpty()) {
            showInfo(bundle.getString("alert.emptyCart"));
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle(bundle.getString("alert.confirmTitle"));
        confirm.setHeaderText(null);
        confirm.setContentText(bundle.getString("alert.confirmClear"));
        Optional<javafx.scene.control.ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            carrito.clear();
            cartList.getItems().clear();
            updateTotal();
            try {
                CarritoJsonDao dao = new CarritoJsonDao();
                dao.save("carrito.json", carrito);
            } catch (IOException e) {
                e.printStackTrace();
            }
            showInfo(bundle.getString("alert.cleared"));
        }
    }

    /**
     * Calcula el total del carrito y actualiza la etiqueta correspondiente.
     */
    private void updateTotal() {
        ResourceBundle bundle = ResourceBundle.getBundle("co.edu.prog3.ui.messages", Locale.getDefault());
        double total = carrito.stream().mapToDouble(Product::getPrice).sum();
        totalLabel.setText(bundle.getString("label.total") + ": $" + total);
    }

    /**
     * Muestra una ventana informativa con detalles del producto seleccionado.
     *
     * @param product producto a detallar.
     */
    private void showProductDetails(Product product) {
        ResourceBundle bundle = ResourceBundle.getBundle("co.edu.prog3.ui.messages", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("alert.productDetails"));
        alert.setHeaderText(product.getName());
        alert.setContentText(
                bundle.getString("label.price") + ": $" + product.getPrice() + "\n" +
                bundle.getString("label.category") + ": " + product.getCategory() + "\n" +
                bundle.getString("label.brand") + ": " + product.getBrand()
        );
        alert.showAndWait();
    }

    /**
     * Finaliza la compra, muestra el total y limpia el carrito.
     */
    @FXML
    private void checkout() {
        ResourceBundle bundle = ResourceBundle.getBundle("co.edu.prog3.ui.messages", Locale.getDefault());

        if (carrito.isEmpty()) {
            showInfo(bundle.getString("alert.checkoutEmpty"));
        } else {
            double total = carrito.stream().mapToDouble(Product::getPrice).sum();
            String message = bundle.getString("alert.checkoutSuccess").replace("${0}", String.valueOf(total));
            showInfo(message);
            carrito.clear();
            cartList.getItems().clear();
            updateTotal();
            try {
                CarritoJsonDao dao = new CarritoJsonDao();
                dao.save("carrito.json", carrito);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Regresa a la vista principal cargando nuevamente el MainView.fxml.
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
     * Muestra un mensaje informativo en una ventana emergente.
     *
     * @param message mensaje a mostrar.
     */
    private void showInfo(String message) {
        ResourceBundle bundle = ResourceBundle.getBundle("co.edu.prog3.ui.messages", Locale.getDefault());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("alert.infoTitle"));
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
