package com.project.domains;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "cosmetics")
public class Cosmetic {

    @Column(name = "cosmetic_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private String image_url;

    @Column(name = "cosmetic_name")
    @NotNull(message = "Name is required")
    @Size(min = 2)
    private String name;

    @Column(name = "cosmetic_price")
    @NotNull(message = "Price is required")
    private Float price;

    @ManyToOne
    Category category;

    @ManyToMany(mappedBy = "cosmeticsInCart")
    Set<Cart> carts;

    public Cosmetic(String name, Float price) {
        this.name = name;
        this.price = price;
    }

    public Cosmetic(String name, Float price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Cosmetic(String image_url, String name, Float price, Category category) {
        this.image_url = image_url;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Cosmetic() { }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void deleteCartfromSet(Cart car) {
        if (this.carts.contains(car)) {
            this.carts.remove(car);
        }
    }

}
