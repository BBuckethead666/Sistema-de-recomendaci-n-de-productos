package co.edu.prog3.algorithms;

import co.edu.prog3.model.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecommenderTest {

    private ProductGraph graph;
    private Recommender recommender;

    @BeforeEach
    void setup() {
        graph = new ProductGraph();
        graph.addProduct(new Product("P1","A",100,"Ropa","M1"));
        graph.addProduct(new Product("P2","B",110,"Ropa","M1"));
        graph.addProduct(new Product("P3","C",300,"Calzado","M2"));
        graph.addProduct(new Product("P4","D",105,"Ropa","M3"));

        graph.addRelation(new Relation("P1","P2", Relation.Type.CATEGORY, 1.0));
        graph.addRelation(new Relation("P2","P3", Relation.Type.BRAND, 0.4));
        graph.addRelation(new Relation("P1","P4", Relation.Type.CATEGORY, 0.9));

        recommender = new Recommender(graph);
        recommender.addMetric(new CategoryMetric(), 0.5);
        recommender.addMetric(new BrandMetric(), 0.3);
        recommender.addMetric(new PriceMetric(), 0.2);
    }

    @Test
    void collectCandidatesBfsDepth1() {
        var recs = recommender.recommendRanked("P1", 1, 10);
        List<String> ids = recs.stream().map(r -> r.getProduct().getId()).toList();
        assertTrue(ids.contains("P2"));
        assertTrue(ids.contains("P4"));
        assertFalse(ids.contains("P3"), "Profundidad 1 no debe incluir P3");
    }

    @Test
    void bfsDepth2IncludesIndirect() {
        var recs = recommender.recommendRanked("P1", 2, 10);
        List<String> ids = recs.stream().map(r -> r.getProduct().getId()).toList();
        assertTrue(ids.contains("P3"), "Profundidad 2 debe incluir P3");
    }

    @Test
    void rankingOrdersByScore() {
        var recs = recommender.recommendRanked("P1", 2, 10);
        assertFalse(recs.isEmpty());
        for (int i = 1; i < recs.size(); i++) {
            assertTrue(recs.get(i-1).getScore() >= recs.get(i).getScore(),
                       "Scores deben estar ordenados descendentemente");
        }
    }

    @Test
    void topKLimitsResults() {
        var recs = recommender.recommendRanked("P1", 2, 1);
        assertTrue(recs.size() <= 1);
    }
}
