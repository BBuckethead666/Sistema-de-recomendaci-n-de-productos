package co.edu.prog3.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductGraphTest {

    @Test
    public void testAddAndFindProduct() {
        ProductGraph graph = new ProductGraph();
        Product p = new Product("P001", "Laptop", 1200.0, "Electrónica", "Lenovo", "images/laptop.png");

        graph.addProduct(p);

        assertEquals(1, graph.size());
        assertNotNull(graph.findProductById("P001"));
        assertEquals("Laptop", graph.findProductById("P001").getName());
    }

    @Test
    public void testUpdateProduct() {
        ProductGraph graph = new ProductGraph();
        Product p = new Product("P001", "Laptop", 1200.0, "Electrónica", "Lenovo", "images/laptop.png");
        graph.addProduct(p);

        Product updated = new Product("P001", "Laptop Pro", 1500.0, "Electrónica", "Lenovo", "images/laptop.png");
        graph.updateProduct(updated);

        assertEquals("Laptop Pro", graph.findProductById("P001").getName());
        assertEquals(1500.0, graph.findProductById("P001").getPrice());
    }

    @Test
    public void testRemoveProduct() {
        ProductGraph graph = new ProductGraph();
        Product p = new Product("P001", "Laptop", 1200.0, "Electrónica", "Lenovo", "images/laptop.png");
        graph.addProduct(p);

        graph.removeProduct("P001");

        assertTrue(graph.isEmpty());
    }
}
