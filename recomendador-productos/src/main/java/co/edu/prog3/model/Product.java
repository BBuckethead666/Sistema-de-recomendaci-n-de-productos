package co.edu.prog3.model;

/**
 * Representa un producto dentro del sistema.
 * Contiene información asociada como el ID, nombre, precio,
 * categoría, marca y la ruta de la imagen correspondiente.
 */
public class Product {

    /** Identificador único del producto. */
    private String id;

    /** Nombre del producto. */
    private String name;

    /** Precio del producto. */
    private double price;

    /** Categoría a la que pertenece el producto. */
    private String category;

    /** Marca del producto. */
    private String brand;

    /** Ruta del archivo de imagen del producto. */
    private String imagePath;

    /**
     * Constructor vacío requerido por Jackson para la deserialización.
     */
    public Product() {}

    /**
     * Constructor completo que inicializa todos los atributos del producto.
     *
     * @param id Identificador único del producto.
     * @param name Nombre del producto.
     * @param price Precio del producto.
     * @param category Categoría del producto.
     * @param brand Marca del producto.
     * @param imagePath Ruta de la imagen asociada al producto.
     */
    public Product(String id, String name, double price, String category, String brand, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.imagePath = imagePath;
    }

    /**
     * Constructor alternativo con ruta de imagen por defecto.
     *
     * @param id Identificador único del producto.
     * @param name Nombre del producto.
     * @param price Precio del producto.
     * @param category Categoría del producto.
     * @param brand Marca del producto.
     */
    public Product(String id, String name, double price, String category, String brand) {
        this(id, name, price, category, brand, "images/placeholder.png");
    }

    /** @return ID del producto. */
    public String getId() { return id; }

    /**
     * Establece el ID del producto.
     *
     * @param id Nuevo ID.
     */
    public void setId(String id) { this.id = id; }

    /** @return Nombre del producto. */
    public String getName() { return name; }

    /**
     * Establece el nombre del producto.
     *
     * @param name Nombre nuevo.
     */
    public void setName(String name) { this.name = name; }

    /** @return Precio del producto. */
    public double getPrice() { return price; }

    /**
     * Establece el precio del producto.
     *
     * @param price Nuevo precio.
     */
    public void setPrice(double price) { this.price = price; }

    /** @return Categoría del producto. */
    public String getCategory() { return category; }

    /**
     * Establece la categoría del producto.
     *
     * @param category Nueva categoría.
     */
    public void setCategory(String category) { this.category = category; }

    /** @return Marca del producto. */
    public String getBrand() { return brand; }

    /**
     * Establece la marca del producto.
     *
     * @param brand Nueva marca.
     */
    public void setBrand(String brand) { this.brand = brand; }

    /** @return Ruta de la imagen del producto. */
    public String getImagePath() { return imagePath; }

    /**
     * Establece la ruta de la imagen del producto.
     *
     * @param imagePath Nueva ruta de imagen.
     */
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    /**
     * Retorna una representación en texto del producto.
     *
     * @return Cadena con el nombre, la marca y el precio del producto.
     */
    @Override
    public String toString() {
        return name + " (" + brand + ") - $" + price;
    }
}
