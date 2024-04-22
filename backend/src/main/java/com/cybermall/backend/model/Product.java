package com.cybermall.backend.model;

/**
 * Represents a product in the catalog.
 */
public class Product {

    private Long id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String category;
    private Integer numberOfViews;

    public Product() {}

    // Include a constructor because it's needed for API deserialization
    public Product(Long id, String name, String description, double price, String imageUrl, String category, Integer numberOfViews) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.numberOfViews = numberOfViews;
    }

    public Long getProductId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getNumberOfViews() {
        return this.numberOfViews;
    }

    public void setNumberOfViews(Integer numberOfViews) {
        this.numberOfViews = numberOfViews;
    }
}
