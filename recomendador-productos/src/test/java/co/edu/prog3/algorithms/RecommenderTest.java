package co.edu.prog3.algorithms;

import co.edu.prog3.model.Product;
import co.edu.prog3.model.ProductGraph;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecommenderTest {

    @Test
    public void testRecommendByCategoryOrBrand() {
        ProductGraph graph = new ProductGraph();

        Product p1 = new Product("P001", "Laptop", 1200.0, "Electrónica", "Lenovo", "images/laptop.png");
        Product p2 = new Product("P002", "Teléfono", 800.0, "Electrónica", "Samsung", "images/phone.png");
        Product p3 = new Product("P003", "Zapatos", 100.0, "Moda", "Nike", "images/shoes.png");
        Product p4 = new Product("P004", "Monitor", 300.0, "Electrónica", "Lenovo", "images/monitor.png");

        graph.addProduct(p1);
        graph.addProduct(p2);
        graph.addProduct(p3);
        graph.addProduct(p4);

        // Obtener recomendaciones para P001
        List<Product> neighbors = graph.neighbors("P001");

        // Validaciones
        assertTrue(neighbors.stream().anyMatch(p -> p.getId().equals("P002")),
                "Debe recomendar productos de la misma categoría (Electrónica)");
        assertTrue(neighbors.stream().anyMatch(p -> p.getId().equals("P004")),
                "Debe recomendar productos de la misma marca (Lenovo)");
        assertFalse(neighbors.stream().anyMatch(p -> p.getId().equals("P003")),
                "No debe recomendar productos de otra categoría/brand");
    }
}
