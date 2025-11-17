package co.edu.prog3.integration;

import co.edu.prog3.model.Product;
import co.edu.prog3.model.ProductGraph;
import co.edu.prog3.persistence.CarritoJsonDao;
import co.edu.prog3.persistence.ProductJsonDao;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {

    @Test
    public void testFullFlowCatalogAndCarrito() throws IOException {
        ProductJsonDao productDao = new ProductJsonDao();

        // Cargar catálogo inicial desde resources
        ProductGraph graph = productDao.load("co/edu/prog3/ui/productos.json");

        assertNotNull(graph);
        assertFalse(graph.listProducts().isEmpty(), "El catálogo no debería estar vacío");

        // Tomar el primer producto y añadirlo al carrito
        Product first = graph.listProducts().get(0);

        CarritoJsonDao carritoDao = new CarritoJsonDao();
        carritoDao.save("carrito.json", List.of(first));

        // Volver a cargar carrito desde data/
        List<Product> carrito = carritoDao.load("carrito.json");

        assertEquals(1, carrito.size(), "El carrito debe tener un producto");
        assertEquals(first.getId(), carrito.get(0).getId(), "El producto en el carrito debe coincidir");
    }
}
