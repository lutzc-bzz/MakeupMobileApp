package model;

public class Product {
    private String brand;
    private String name;
    private String price;
    private String currency;
    private String imageUrl;

    public Product(String brand, String name, String price, String currency, String imageUrl) {
        this.brand = brand;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.imageUrl = imageUrl;
    }

    public String getBrand() { return brand; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getCurrency() { return currency; }
    public String getImageUrl() { return imageUrl; }
}
