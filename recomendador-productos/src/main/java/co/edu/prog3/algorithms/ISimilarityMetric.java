package co.edu.prog3.algorithms;

import co.edu.prog3.model.Product;

public interface ISimilarityMetric {
    String name();
    double similarity(Product a, Product b); // rango esperado [0, 1]
}
