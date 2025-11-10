package co.edu.prog3.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class ProductGraphTest {

    private ProductGraph graph;

    @BeforeEach
    void setup() {
        graph = new ProductGraph();
    }

    @Test
    void addAndGetProduct() {
        Product p = new Product("P1", "Camiseta", 50000, "Ropa", "MarcaA");
        graph.addProduct(p);
        Product found = graph.getProduct("P1");
        assertNotNull(found);
        assertEquals("Camiseta", found.getName());
    }

    @Test
    void addAndListProducts() {
        graph.addProduct(new Product("P1", "A", 10, "C", "B"));
        graph.addProduct(new Product("P2", "B", 20, "C2", "B2"));
        final List<Product> all = graph.listProducts();
        assertEquals(2, all.size());
    }

    @Test
    void removeProductAlsoRemovesRelations() {
        Product p1 = new Product("P1", "A", 10, "C", "B");
        Product p2 = new Product("P2", "B", 20, "C2", "B2");
        graph.addProduct(p1);
        graph.addProduct(p2);
        graph.addRelation(new Relation("P1", "P2", Relation.Type.CATEGORY, 1.0));
        assertFalse(graph.neighbors("P1").isEmpty(), "P1 debe tener relaciones antes de eliminar P2");
        graph.removeProduct("P2");
        assertTrue(graph.neighbors("P1").isEmpty(), "Relaciones hacia P2 deben eliminarse");
        assertNull(graph.getProduct("P2"));
    }

    @Test
    void addAndRemoveRelation() {
        graph.addProduct(new Product("P1","A",0,"x","y"));
        graph.addProduct(new Product("P2","B",0,"x","y"));
        graph.addRelation(new Relation("P1","P2", Relation.Type.BRAND, 0.8));
        assertEquals(1, graph.neighbors("P1").size());
        graph.removeRelation("P1","P2", Relation.Type.BRAND);
        assertEquals(0, graph.neighbors("P1").size());
    }
}
