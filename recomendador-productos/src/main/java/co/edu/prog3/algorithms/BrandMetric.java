package co.edu.prog3.algorithms;


import co.edu.prog3.model.Product;

public class BrandMetric implements ISimilarityMetric {
    @Override public String name() { return "Brand"; }
    @Override
    public double similarity(Product a, Product b) {
        return a.getBrand().equalsIgnoreCase(b.getBrand()) ? 1.0 : 0.0;
    }
}
