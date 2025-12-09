package model;

import java.time.Instant;

public class Product {
    private int id;
    private String name;
    private double price;
    private Instant creationDatetime;
    private Category category;

    public Product() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Instant getCreationDatetime() { return creationDatetime; }
    public void setCreationDatetime(Instant creationDatetime) { this.creationDatetime = creationDatetime; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }


    public String getCategoryName() {
        return category != null ? category.getName() : "N/A";
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + String.format("%.2f", price) +
                ", creationDatetime=" + creationDatetime +
                ", categoryName='" + getCategoryName() + '\'' +
                '}';
    }
}