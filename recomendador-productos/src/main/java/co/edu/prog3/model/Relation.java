package co.edu.prog3.model;

public class Relation {
    public enum Type { CATEGORY, BRAND, PRICE }

    private String fromId;
    private String toId;
    private Type type;
    private double weight;

    // Constructor vacío requerido por Jackson
    public Relation() {}

    public Relation(String fromId, String toId, Type type, double weight) {
        this.fromId = fromId;
        this.toId = toId;
        this.type = type;
        this.weight = weight;
    }

    // Getters y setters
    public String getFromId() { return fromId; }
    public void setFromId(String fromId) { this.fromId = fromId; }

    public String getToId() { return toId; }
    public void setToId(String toId) { this.toId = toId; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
}
