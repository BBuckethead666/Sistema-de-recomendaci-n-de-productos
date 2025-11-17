package co.edu.prog3.persistence;

import co.edu.prog3.model.Product;
import co.edu.prog3.model.ProductGraph;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ProductJsonDaoTest {

    @Test
    public void testSaveAndLoadProducts() throws IOException {
        ProductJsonDao dao = new ProductJsonDao();

        // Crear catálogo de prueba
        ProductGraph graph = new ProductGraph();
        graph.addProduct(new Product("T001", "Producto Test", 99.9, "Test", "TestBrand", "images/test.png"));

        // Guardar en carpeta data/
        dao.save("test_products.json", graph);

        // Volver a cargar desde resources (productos.json)
        ProductGraph loaded = dao.load("co/edu/prog3/ui/productos.json");

        assertNotNull(loaded);
        assertFalse(loaded.listProducts().isEmpty(), "El catálogo cargado no debe estar vacío");
    }
}
