package co.edu.prog3.persistence;

import co.edu.prog3.model.ProductGraph;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Clase encargada de gestionar la persistencia de datos en formato JSON
 * para objetos de tipo {@link ProductGraph}. Utiliza la librería Jackson
 * para realizar la serialización y deserialización de datos.
 *
 * <p>Este DAO permite cargar información desde un archivo externo si existe,
 * o desde los recursos internos del proyecto en caso contrario. También permite
 * guardar la estructura de productos en un archivo JSON.</p>
 */
public class ProductJsonDao {

    /** Mapper utilizado para convertir entre JSON y objetos Java. */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Carga un archivo JSON y lo convierte en un objeto {@link ProductGraph}.
     * <p>
     * Primero intenta cargar el archivo desde una ruta externa. Si no existe,
     * intenta cargarlo como un recurso desde el classpath.
     * </p>
     *
     * @param path Ruta del archivo JSON a cargar. Puede ser externa o interna.
     * @return El {@link ProductGraph} generado a partir del JSON.
     * @throws IOException Si ocurrió un error al acceder o leer el archivo.
     */
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

    /**
     * Guarda un objeto {@link ProductGraph} en un archivo externo en formato JSON.
     * El archivo se genera con formato legible (pretty print).
     *
     * @param path Ruta donde se almacenará el archivo JSON.
     * @param graph Objeto {@link ProductGraph} que será guardado.
     * @throws IOException Si ocurre un error durante la escritura del archivo.
     */
    public void save(String path, ProductGraph graph) throws IOException {
        File file = new File(path);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, graph);
    }
}
