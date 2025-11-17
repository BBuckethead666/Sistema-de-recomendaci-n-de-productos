package co.edu.prog3.model;

public class Product {
    private String id;
    private String name;
    private double price;
    private String category;
    private String brand;
    private String imagePath;

    // Constructor vacío (Jackson lo necesita)
    public Product() {}

    // Constructor completo (6 parámetros)
    public Product(String id, String name, double price, String category, String brand, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.imagePath = imagePath;
    }

    // Constructor alternativo (5 parámetros, con imagePath por defecto)
    public Product(String id, String name, double price, String category, String brand) {
        this(id, name, price, category, brand, "images/placeholder.png");
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public String toString() {
        return name + " (" + brand + ") - $" + price;
    }
}
