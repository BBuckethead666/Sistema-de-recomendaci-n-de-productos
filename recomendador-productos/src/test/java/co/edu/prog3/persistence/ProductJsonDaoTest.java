package co.edu.prog3.persistence;

import co.edu.prog3.model.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ProductJsonDaoTest {

    private ProductJsonDao dao;
    private File tmpFile;

    @BeforeEach
    void setup() throws IOException {
        dao = new ProductJsonDao();
        tmpFile = File.createTempFile("productos-test", ".json");
        tmpFile.deleteOnExit();
    }

    @Test
    void saveAndLoadGraph() throws Exception {
        ProductGraph graph = new ProductGraph();
        graph.addProduct(new Product("P1","A",10,"C","B"));
        graph.addProduct(new Product("P2","B",20,"C2","B2"));
        graph.addRelation(new Relation("P1","P2", Relation.Type.CATEGORY, 1.0));

        dao.save(tmpFile.getAbsolutePath(), graph);

        ProductGraph loaded = dao.load(tmpFile.getAbsolutePath());
        assertNotNull(loaded.getProduct("P1"));
        assertNotNull(loaded.getProduct("P2"));
        assertFalse(loaded.neighbors("P1").isEmpty());
    }
}
