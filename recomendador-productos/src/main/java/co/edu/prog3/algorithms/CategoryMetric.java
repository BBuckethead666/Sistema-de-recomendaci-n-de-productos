package co.edu.prog3.algorithms;

import co.edu.prog3.model.Product;

public class CategoryMetric implements ISimilarityMetric {
    @Override public String name() { return "Category"; }
    @Override
    public double similarity(Product a, Product b) {
        return a.getCategory().equalsIgnoreCase(b.getCategory()) ? 1.0 : 0.0;
    }
}
