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
    private String id;

    @Column(name = "cosmetic_name")
    @NotNull(message = "Name is required")
    @Size(min = 2)
    private String name;

    @Column(name = "cosmetic_price")
    @NotNull(message = "Price is required")
    private Float price;

    @ManyToMany
    @JoinTable(
            name = "cosmetic_categories",
            joinColumns = @JoinColumn(name = "cosmetic_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    Set<Category> categories;

    public Cosmetic(String id, String name, Float price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
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
}
