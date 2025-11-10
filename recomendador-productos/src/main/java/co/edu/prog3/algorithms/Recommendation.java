package co.edu.prog3.algorithms;

import co.edu.prog3.model.Product;
import java.util.Map;

public class Recommendation {
    private final Product product;
    private final double score;
    private final Map<String, Double> breakdown; // métrica -> valor

    public Recommendation(Product product, double score, Map<String, Double> breakdown) {
        this.product = product;
        this.score = score;
        this.breakdown = breakdown;
    }
    public Product getProduct() { return product; }
    public double getScore() { return score; }
    public Map<String, Double> getBreakdown() { return breakdown; }

    @Override
    public String toString() {
        return product.getName() + " (score: " + String.format("%.3f", score) + ")";
    }
}
