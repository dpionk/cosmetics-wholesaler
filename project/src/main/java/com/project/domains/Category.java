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
    @Size(min = 2)
    private String name;

    @OneToMany(mappedBy = "category")
    Set<Cosmetic> cosmeticsWithCategory;

}
