package model;

public class Product {
    private String brand;
    private String name;
    private String price;
    private String currency;
    private String imageUrl;
    private String description;
    private String productLink;
    private String category;
    private String rating;

    private String productType;
    private String ingredients;

    public Product(String brand, String name, String price, String currency, String imageUrl, String description, String productLink, String category, String rating, String productType, String ingredients) {
        this.brand = brand;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.imageUrl = imageUrl;
        this.description = description;
        this.productLink = productLink;
        this.category = category;
        this.rating = rating;
        this.productType = productType;
        this.ingredients = ingredients;
    }

    public String getBrand() { return brand; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getCurrency() { return currency; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; }
    public String getProductLink() { return productLink; }

    public String getCategory() {return category;}
    public String getRating() {return rating;}
    public String getIngredients() {return ingredients;}

    public String getProductType() {
        return productType;
    }
}
