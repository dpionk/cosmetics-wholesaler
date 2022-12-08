package com.project.domain;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "carts")
public class Cart {

    @Column(name = "cart_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToMany
    @JoinTable(
            name = "cosmetics_in_cart",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "cosmetic_id"))
    private ArrayList<Cosmetic> cosmetics;

}
