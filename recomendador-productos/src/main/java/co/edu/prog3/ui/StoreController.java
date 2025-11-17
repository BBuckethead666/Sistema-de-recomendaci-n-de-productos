package co.edu.prog3.ui;

import co.edu.prog3.model.Product;
import co.edu.prog3.model.ProductGraph;
import co.edu.prog3.persistence.CarritoJsonDao;
import co.edu.prog3.persistence.ProductJsonDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.util.ResourceBundle;

public class StoreController {

    @FXML private GridPane catalogGrid;
    @FXML private ListView<String> cartList;
    @FXML private Label totalLabel; // ✅ nuevo para mostrar el total

    private ProductGraph graph;
    private List<Product> carrito;
    private Stage stage;

    public void setStage(Stage stage) { this.stage = stage; }

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
        updateTotal(); // inicializar total
    }

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

    private VBox createProductCard(Product product) {
        ResourceBundle bundle = ResourceBundle.getBundle("co.edu.prog3.ui.messages", Locale.getDefault());

        VBox box = new VBox(10);
        box.getStyleClass().add("product-card");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        // Manejo robusto de imágenes
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

        // ✅ Recomendaciones interactivas
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

    private void addToCart(Product product) {
        carrito.add(product);
        cartList.getItems().add(product.getName() + " - $" + product.getPrice());
        updateTotal(); // ✅ actualizar total
        try {
            CarritoJsonDao dao = new CarritoJsonDao();
            dao.save("carrito.json", carrito);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearCart() {
        carrito.clear();
        cartList.getItems().clear();
        updateTotal(); // ✅ resetear total
        try {
            CarritoJsonDao dao = new CarritoJsonDao();
            dao.save("carrito.json", carrito);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTotal() {
    ResourceBundle bundle = ResourceBundle.getBundle("co.edu.prog3.ui.messages", Locale.getDefault());
    double total = carrito.stream().mapToDouble(Product::getPrice).sum();
    totalLabel.setText(bundle.getString("label.total") + ": $" + total);
}


    private void showProductDetails(Product product) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Detalles del producto");
        alert.setHeaderText(product.getName());
        alert.setContentText(
                "Precio: $" + product.getPrice() + "\n" +
                "Categoría: " + product.getCategory() + "\n" +
                "Marca: " + product.getBrand()
        );
        alert.showAndWait();
    }

    @FXML
    private void checkout() {
        System.out.println("Checkout con " + carrito.size() + " productos. Total: $" +
                carrito.stream().mapToDouble(Product::getPrice).sum());
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

}
