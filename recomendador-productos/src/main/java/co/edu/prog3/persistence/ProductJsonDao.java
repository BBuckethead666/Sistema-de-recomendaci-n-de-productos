package co.edu.prog3.persistence;

import co.edu.prog3.model.ProductGraph;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * DAO para persistencia de ProductGraph en JSON.
 */
public class ProductJsonDao {
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Guarda el grafo en un archivo JSON.
     * @param filename ruta del archivo destino
     * @param graph grafo de productos
     * @throws IOException si ocurre un error de escritura
     */
    public void save(String filename, ProductGraph graph) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filename), graph);
    }

    /**
     * Carga un grafo desde un archivo JSON.
     * @param filename ruta del archivo origen
     * @return grafo cargado
     * @throws IOException si ocurre un error de lectura
     */
    public ProductGraph load(String filename) throws IOException {
        return mapper.readValue(new File(filename), ProductGraph.class);
    }
}
