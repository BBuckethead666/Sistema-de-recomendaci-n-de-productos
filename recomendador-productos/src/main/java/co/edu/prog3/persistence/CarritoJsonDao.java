package co.edu.prog3.persistence;

import co.edu.prog3.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Clase encargada de la persistencia del carrito de compras en formato JSON.
 * <p>
 * Utiliza Jackson para serializar y deserializar listas de objetos {@link Product},
 * permitiendo guardar y cargar el contenido del carrito desde la carpeta local "data".
 * </p>
 */
public class CarritoJsonDao {

    /** Mapper utilizado para manejar la conversión entre JSON y objetos Java. */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Guarda la lista de productos del carrito en un archivo JSON dentro de la carpeta "data".
     * Si la carpeta no existe, se crea automáticamente.
     *
     * @param fileName Nombre del archivo JSON donde se guardará la lista.
     * @param carrito Lista de productos a guardar.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    public void save(String fileName, List<Product> carrito) throws IOException {
        File outFile = new File("data", fileName);
        outFile.getParentFile().mkdirs();
        mapper.writeValue(outFile, carrito);
    }

    /**
     * Carga una lista de productos desde un archivo JSON ubicado en la carpeta "data".
     *
     * @param fileName Nombre del archivo JSON a leer.
     * @return Lista de objetos {@link Product} cargados desde el archivo.
     * @throws IOException Si el archivo no existe o si ocurre un error al leerlo.
     */
    public List<Product> load(String fileName) throws IOException {
        File inFile = new File("data", fileName);
        if (!inFile.exists()) {
            throw new IOException("Carrito file not found: " + fileName);
        }
        return mapper.readValue(
                inFile,
                mapper.getTypeFactory().constructCollectionType(List.class, Product.class)
        );
    }
}
