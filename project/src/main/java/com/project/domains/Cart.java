package com.project.domains;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.List;


@Entity
@Table(name = "carts")
public class Cart {

    @Column(name = "cart_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "Reference to user_id is required")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "cosmetics_in_cart",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "cosmetic_id"))
    private List<Cosmetic> cosmeticsInCart;

    public Cart(User user) {
        this.user = user;
    }

    public Cart() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Cosmetic> getCosmeticsInCart() {
        return this.cosmeticsInCart;
    }


    public void addCosmeticToCart(Cosmetic cosmetic) {
        this.cosmeticsInCart.add(cosmetic);
    }

    public void deleteCosmeticFromCart(Cosmetic cosmetic) {
        this.cosmeticsInCart.remove(cosmetic);
    }
}
