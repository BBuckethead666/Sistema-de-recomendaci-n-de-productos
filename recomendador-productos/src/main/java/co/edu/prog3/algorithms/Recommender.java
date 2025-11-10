package co.edu.prog3.algorithms;

import co.edu.prog3.model.Product;
import co.edu.prog3.model.ProductGraph;
import co.edu.prog3.model.Relation;

import java.util.*;

public class Recommender {
    private final ProductGraph graph;
    private final List<ISimilarityMetric> metrics = new ArrayList<>();
    private final Map<String, Double> weights = new HashMap<>(); // nombre métrica -> peso

    public Recommender(ProductGraph graph) {
        this.graph = graph;
    }

    public void addMetric(ISimilarityMetric metric, double weight) {
        metrics.add(metric);
        weights.put(metric.name(), weight);
    }

    // BFS para recolectar candidatos hasta cierta profundidad
    private List<Product> collectCandidatesBFS(String productId, int maxDepth) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        Map<String, Integer> depth = new HashMap<>();

        visited.add(productId);
        queue.add(productId);
        depth.put(productId, 0);

        List<Product> candidates = new ArrayList<>();

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int currentDepth = depth.get(current);
            if (currentDepth >= maxDepth) continue;

            for (Relation r : graph.neighbors(current)) {
                String neighborId = r.getToId();
                if (!visited.contains(neighborId)) {
                    visited.add(neighborId);
                    queue.add(neighborId);
                    depth.put(neighborId, currentDepth + 1);

                    Product candidate = graph.getProduct(neighborId);
                    if (candidate != null && !neighborId.equals(productId)) {
                        candidates.add(candidate);
                    }
                }
            }
        }
        return candidates;
    }

    // Calcula el score ponderado combinando métricas en [0,1]
    private Recommendation scoreFor(Product source, Product candidate) {
        Map<String, Double> breakdown = new LinkedHashMap<>();
        double weightedSum = 0.0;
        double weightTotal = 0.0;

        for (ISimilarityMetric m : metrics) {
            double w = weights.getOrDefault(m.name(), 1.0);
            double sim = m.similarity(source, candidate);
            breakdown.put(m.name(), sim);
            weightedSum += w * sim;
            weightTotal += w;
        }

        double score = weightTotal > 0 ? (weightedSum / weightTotal) : 0.0;
        return new Recommendation(candidate, score, breakdown);
    }

    // API principal: ranking con BFS y combinación de métricas
    public List<Recommendation> recommendRanked(String productId, int maxDepth, int topK) {
        Product source = graph.getProduct(productId);
        if (source == null) return List.of();

        List<Product> candidates = collectCandidatesBFS(productId, maxDepth);
        List<Recommendation> recs = new ArrayList<>(candidates.size());
        for (Product c : candidates) {
            recs.add(scoreFor(source, c));
        }

        recs.sort(Comparator.comparingDouble(Recommendation::getScore).reversed());
        if (topK > 0 && recs.size() > topK) {
            return recs.subList(0, topK);
        }
        return recs;
    }
}
