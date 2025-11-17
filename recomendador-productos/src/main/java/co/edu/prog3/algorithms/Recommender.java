package co.edu.prog3.algorithms;

import co.edu.prog3.model.Product;
import co.edu.prog3.model.ProductGraph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase que implementa un sistema de recomendación básico
 * usando relaciones de categoría y marca entre productos.
 */
public class Recommender {

    private ProductGraph graph;

    public Recommender(ProductGraph graph) {
        this.graph = graph;
    }

    /**
     * Recomienda productos relacionados con el producto dado.
     * Relación: misma categoría o misma marca.
     */
    public List<Product> recommendByNeighbors(String productId, int limit) {
        List<Product> neighbors = graph.neighbors(productId);
        return neighbors.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Recomienda productos similares basados en categoría.
     */
    public List<Product> recommendByCategory(String productId, int limit) {
        Product base = graph.getProduct(productId);
        if (base == null) return Collections.emptyList();

        return graph.listProducts().stream()
                .filter(p -> !p.getId().equals(productId))
                .filter(p -> p.getCategory().equalsIgnoreCase(base.getCategory()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Recomienda productos similares basados en marca.
     */
    public List<Product> recommendByBrand(String productId, int limit) {
        Product base = graph.getProduct(productId);
        if (base == null) return Collections.emptyList();

        return graph.listProducts().stream()
                .filter(p -> !p.getId().equals(productId))
                .filter(p -> p.getBrand().equalsIgnoreCase(base.getBrand()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Recomienda productos aleatorios (fallback).
     */
    public List<Product> recommendRandom(int limit) {
        List<Product> all = graph.listProducts();
        Collections.shuffle(all);
        return all.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
}
