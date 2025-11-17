package co.edu.prog3.persistence;

import co.edu.prog3.model.Product;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CarritoJsonDaoTest {

    @Test
    public void testSaveAndLoadCarrito() throws IOException {
        CarritoJsonDao dao = new CarritoJsonDao();

        Product p = new Product("C001", "Producto Carrito", 50.0, "Test", "TestBrand", "images/test.png");

        // Guardar carrito en carpeta data/
        dao.save("carrito_test.json", List.of(p));

        // Volver a cargar
        List<Product> carrito = dao.load("carrito_test.json");

        assertEquals(1, carrito.size(), "El carrito debe tener un producto");
        assertEquals("C001", carrito.get(0).getId());
    }
}
