package com.project.domains;

import javax.persistence.*;

import java.util.Map;

@Entity
@Table(name = "carts")
public class Cart {

    @Column(name = "cart_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "cosmetics_in_cart",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "cosmetic_id"))
    private Map<Integer, Cosmetic> cosmeticsInCart;

}
