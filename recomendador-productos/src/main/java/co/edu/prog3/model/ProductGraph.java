package co.edu.prog3.model;

import java.util.*;

public class ProductGraph {
    private Map<String, Product> products = new HashMap<>();
    private Map<String, Set<Relation>> adjacency = new HashMap<>();

    // Constructor vacío requerido por Jackson
    public ProductGraph() {}

    // Métodos CRUD
    public void addProduct(Product p) {
        products.put(p.getId(), p);
        adjacency.putIfAbsent(p.getId(), new HashSet<>());
    }

    public Product getProduct(String id) {
        return products.get(id);
    }

    public List<Product> listProducts() {
        return new ArrayList<>(products.values());
    }

    public void addRelation(Relation r) {
        adjacency.putIfAbsent(r.getFromId(), new HashSet<>());
        adjacency.putIfAbsent(r.getToId(), new HashSet<>());
        adjacency.get(r.getFromId()).add(r);
        adjacency.get(r.getToId()).add(new Relation(r.getToId(), r.getFromId(), r.getType(), r.getWeight()));
    }

    public Set<Relation> neighbors(String id) {
        return adjacency.getOrDefault(id, Set.of());
    }

    public void removeProduct(String id) {
        products.remove(id);
        adjacency.remove(id);
        for (Set<Relation> rels : adjacency.values()) {
            rels.removeIf(r -> r.getToId().equals(id));
        }
    }

    public void removeRelation(String fromId, String toId, Relation.Type type) {
        adjacency.getOrDefault(fromId, Set.of()).removeIf(r -> r.getToId().equals(toId) && r.getType() == type);
        adjacency.getOrDefault(toId, Set.of()).removeIf(r -> r.getToId().equals(fromId) && r.getType() == type);
    }

    // Getters y setters para Jackson
    public Map<String, Product> getProducts() { return products; }
    public void setProducts(Map<String, Product> products) { this.products = products; }

    public Map<String, Set<Relation>> getAdjacency() { return adjacency; }
    public void setAdjacency(Map<String, Set<Relation>> adjacency) { this.adjacency = adjacency; }
}
