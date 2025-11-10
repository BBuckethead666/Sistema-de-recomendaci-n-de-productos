package co.edu.prog3.algorithms;

import co.edu.prog3.model.Product;

public class PriceMetric implements ISimilarityMetric {
    @Override public String name() { return "Price"; }
    @Override
    public double similarity(Product a, Product b) {
        double pa = a.getPrice();
        double pb = b.getPrice();
        double max = Math.max(pa, pb);
        if (max == 0) return 1.0;
        double diff = Math.abs(pa - pb) / max; // normaliza por el mayor precio
        double sim = 1.0 - diff;               // más cerca -> mayor similitud
        return Math.max(0.0, Math.min(sim, 1.0));
    }
}
