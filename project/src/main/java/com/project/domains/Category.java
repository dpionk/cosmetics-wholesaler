package com.project.domains;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {

    @Column(name = "category_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name")
    @NotNull(message = "Name is required")
    @Size(min = 2, max = 20)
    private String name;


    public Category(String name) {
        this.name = name;
    }

    public Category() {}

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "category")
    Set<Cosmetic> cosmeticsWithCategory;

}
