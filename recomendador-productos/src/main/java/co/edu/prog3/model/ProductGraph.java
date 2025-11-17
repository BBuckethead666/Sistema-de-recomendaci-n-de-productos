package co.edu.prog3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true) // ignora campos extra como "empty"
public class ProductGraph {

    private List<Product> products;
    private int idCounter = 1;
    private String idPrefix = "PRD"; // prefijo configurable

    public ProductGraph() {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    // -------------------- CRUD --------------------
    public void addProduct(Product p) { products.add(p); }

    public void updateProduct(Product updated) {
        Optional<Product> existing = products.stream()
                .filter(p -> p.getId().equals(updated.getId()))
                .findFirst();
        existing.ifPresent(p -> {
            p.setName(updated.getName());
            p.setPrice(updated.getPrice());
            p.setCategory(updated.getCategory());
            p.setBrand(updated.getBrand());
            p.setImagePath(updated.getImagePath());
        });
    }

    public void removeProduct(String id) { products.removeIf(p -> p.getId().equals(id)); }

    public Product findProductById(String id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Product> listProducts() { return new ArrayList<>(products); }

    // -------------------- Recommender --------------------
    public Product getProduct(String id) { return findProductById(id); }

    public List<Product> neighbors(String id) {
        Product base = getProduct(id);
        if (base == null) return new ArrayList<>();

        return products.stream()
                .filter(p -> !p.getId().equals(id))
                .filter(p -> p.getCategory().equalsIgnoreCase(base.getCategory())
                          || p.getBrand().equalsIgnoreCase(base.getBrand()))
                .toList();
    }

    // -------------------- ID Generator --------------------
    public String generateId() {
        String newId;
        do {
            newId = idPrefix + String.format("%03d", idCounter++);
        } while (findProductById(newId) != null);
        return newId;
    }

    public void resetIdCounter() { idCounter = 1; }
    public void setIdPrefix(String prefix) { this.idPrefix = prefix; }

    // -------------------- Utilidades --------------------
    @JsonIgnore
    public boolean isEmpty() { return products.isEmpty(); }

    public int size() { return products.size(); }

    public void clear() {
        products.clear();
        resetIdCounter();
    }
}
