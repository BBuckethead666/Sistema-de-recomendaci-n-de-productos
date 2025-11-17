package co.edu.prog3.persistence;

import co.edu.prog3.model.ProductGraph;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ProductJsonDao {

    private final ObjectMapper mapper = new ObjectMapper();

    // ✅ Cargar desde archivo externo o desde resources
    public ProductGraph load(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            return mapper.readValue(file, ProductGraph.class);
        } else {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
                if (is == null) throw new IOException("No se encontró el recurso: " + path);
                return mapper.readValue(is, ProductGraph.class);
            }
        }
    }

    // ✅ Guardar en archivo externo
    public void save(String path, ProductGraph graph) throws IOException {
        File file = new File(path);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, graph);
    }
}
