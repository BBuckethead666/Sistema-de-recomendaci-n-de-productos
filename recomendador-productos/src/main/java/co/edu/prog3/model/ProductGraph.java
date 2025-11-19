package co.edu.prog3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Representa una estructura tipo grafo simple que almacena productos y permite
 * realizar operaciones CRUD, generación de IDs y recomendaciones basadas
 * en similitud por marca o categoría.
 *
 * <p>La clase está diseñada para ser serializable con Jackson y funciona
 * como un contenedor central de productos para la tienda.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true) // ignora campos extra como "empty"
public class ProductGraph {

    /** Lista de productos almacenados en el grafo. */
    private List<Product> products;

    /** Contador interno utilizado para generar IDs únicos. */
    private int idCounter = 1;

    /** Prefijo utilizado para generar los IDs de cada producto. */
    private String idPrefix = "PRD";

    /**
     * Crea un grafo vacío de productos.
     */
    public ProductGraph() {
        this.products = new ArrayList<>();
    }

    /**
     * Obtiene la lista de productos almacenados.
     *
     * @return lista de productos.
     */
    public List<Product> getProducts() { return products; }

    /**
     * Establece la lista de productos del grafo.
     *
     * @param products lista de productos.
     */
    public void setProducts(List<Product> products) { this.products = products; }

    // -------------------- CRUD --------------------

    /**
     * Agrega un nuevo producto a la lista.
     *
     * @param p producto a agregar.
     */
    public void addProduct(Product p) { products.add(p); }

    /**
     * Actualiza un producto existente buscando por su ID.
     *
     * @param updated producto con nuevos datos.
     */
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

    /**
     * Elimina un producto por su ID.
     *
     * @param id ID del producto a eliminar.
     */
    public void removeProduct(String id) { products.removeIf(p -> p.getId().equals(id)); }

    /**
     * Busca un producto según su ID.
     *
     * @param id ID del producto buscado.
     * @return el producto si existe, o null si no.
     */
    public Product findProductById(String id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Devuelve una copia de la lista de productos.
     *
     * @return lista nueva de productos.
     */
    public List<Product> listProducts() { return new ArrayList<>(products); }

    // -------------------- Recommender --------------------

    /**
     * Obtiene un producto por ID.
     *
     * @param id ID del producto.
     * @return producto correspondiente o null.
     */
    public Product getProduct(String id) { return findProductById(id); }

    /**
     * Genera recomendaciones basadas en similitudes de categoría o marca.
     *
     * @param id ID del producto base.
     * @return lista de productos vecinos.
     */
    public List<Product> neighbors(String id) {
        Product base = getProduct(id);
        if (base == null) return new ArrayList<>();

        return products.stream()
                .filter(p -> !p.getId().equals(id))
                .filter(p -> p.getCategory().equalsIgnoreCase(base.getCategory())
                          || p.getBrand().equalsIgnoreCase(base.getBrand()))
                .toList();
    }

    /**
     * Recomendaciones únicamente basadas en la marca.
     *
     * @param id ID del producto base.
     * @return lista de productos con la misma marca.
     */
    public List<Product> neighborsByBrand(String id) {
        Product base = getProduct(id);
        if (base == null) return new ArrayList<>();

        return products.stream()
                .filter(p -> !p.getId().equals(id))
                .filter(p -> p.getBrand().equalsIgnoreCase(base.getBrand()))
                .toList();
    }

    /**
     * Recomendaciones únicamente basadas en categoría.
     *
     * @param id ID del producto base.
     * @return lista de productos con la misma categoría.
     */
    public List<Product> neighborsByCategory(String id) {
        Product base = getProduct(id);
        if (base == null) return new ArrayList<>();

        return products.stream()
                .filter(p -> !p.getId().equals(id))
                .filter(p -> p.getCategory().equalsIgnoreCase(base.getCategory()))
                .toList();
    }

    // -------------------- ID Generator --------------------

    /**
     * Genera un nuevo ID único de producto con el prefijo configurado.
     *
     * @return nuevo ID generado.
     */
    public String generateId() {
        String newId;
        do {
            newId = idPrefix + String.format("%03d", idCounter++);
        } while (findProductById(newId) != null);
        return newId;
    }

    /**
     * Reinicia el contador interno de IDs.
     */
    public void resetIdCounter() { idCounter = 1; }

    /**
     * Establece un nuevo prefijo para la generación de IDs.
     *
     * @param prefix prefijo deseado.
     */
    public void setIdPrefix(String prefix) { this.idPrefix = prefix; }

    // -------------------- Utilidades --------------------

    /**
     * Verifica si el grafo no contiene productos.
     *
     * @return true si está vacío, false si no.
     */
    @JsonIgnore
    public boolean isEmpty() { return products.isEmpty(); }

    /**
     * Obtiene la cantidad de productos en el grafo.
     *
     * @return tamaño de la lista de productos.
     */
    public int size() { return products.size(); }

    /**
     * Limpia todos los productos y reinicia el contador de IDs.
     */
    public void clear() {
        products.clear();
        resetIdCounter();
    }
}
