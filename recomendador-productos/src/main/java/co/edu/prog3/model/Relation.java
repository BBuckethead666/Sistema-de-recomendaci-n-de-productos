package co.edu.prog3.model;

/**
 * Representa una relación entre dos productos dentro del grafo,
 * permitiendo establecer conexiones basadas en categoría, marca o precio.
 */
public class Relation {

    /**
     * Tipos de relación posibles entre productos.
     * CATEGORY: productos de la misma categoría.
     * BRAND: productos de la misma marca.
     * PRICE: productos relacionados por rango de precio.
     */
    public enum Type { CATEGORY, BRAND, PRICE }

    /** ID del producto origen en la relación. */
    private String fromId;

    /** ID del producto destino en la relación. */
    private String toId;

    /** Tipo de relación entre los productos. */
    private Type type;

    /** Peso o fuerza de la relación. */
    private double weight;

    /**
     * Constructor vacío requerido por Jackson para la deserialización.
     */
    public Relation() {}

    /**
     * Crea una nueva relación entre dos productos.
     *
     * @param fromId ID del producto origen.
     * @param toId ID del producto destino.
     * @param type Tipo de relación.
     * @param weight Peso de la relación.
     */
    public Relation(String fromId, String toId, Type type, double weight) {
        this.fromId = fromId;
        this.toId = toId;
        this.type = type;
        this.weight = weight;
    }

    /**
     * Obtiene el ID del producto origen.
     *
     * @return ID de origen.
     */
    public String getFromId() { return fromId; }

    /**
     * Establece el ID del producto origen.
     *
     * @param fromId Nuevo ID de origen.
     */
    public void setFromId(String fromId) { this.fromId = fromId; }

    /**
     * Obtiene el ID del producto destino.
     *
     * @return ID de destino.
     */
    public String getToId() { return toId; }

    /**
     * Establece el ID del producto destino.
     *
     * @param toId Nuevo ID de destino.
     */
    public void setToId(String toId) { this.toId = toId; }

    /**
     * Obtiene el tipo de relación.
     *
     * @return Tipo de relación.
     */
    public Type getType() { return type; }

    /**
     * Establece el tipo de relación.
     *
     * @param type Nuevo tipo de relación.
     */
    public void setType(Type type) { this.type = type; }

    /**
     * Obtiene el peso de la relación.
     *
     * @return Peso de la relación.
     */
    public double getWeight() { return weight; }

    /**
     * Establece el peso de la relación.
     *
     * @param weight Nuevo peso.
     */
    public void setWeight(double weight) { this.weight = weight; }
}
