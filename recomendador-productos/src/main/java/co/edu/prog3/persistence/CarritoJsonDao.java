package co.edu.prog3.persistence;

import co.edu.prog3.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CarritoJsonDao {

    private final ObjectMapper mapper = new ObjectMapper();

    public void save(String fileName, List<Product> carrito) throws IOException {
        File outFile = new File("data", fileName);
        outFile.getParentFile().mkdirs();
        mapper.writeValue(outFile, carrito);
    }

    public List<Product> load(String fileName) throws IOException {
        File inFile = new File("data", fileName);
        if (!inFile.exists()) {
            throw new IOException("Carrito file not found: " + fileName);
        }
        return mapper.readValue(inFile,
                mapper.getTypeFactory().constructCollectionType(List.class, Product.class));
    }
}
