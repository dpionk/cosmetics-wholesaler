package com.project.domains;

import javax.persistence.*;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class
User {

    @Column(name = "user_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_username")
    @NotNull(message = "Username is required")
    @Size(min = 2)
    private String username;

    @Column(name = "user_password")
    @NotNull(message = "Password is required")
    @Size(min = 8)
    private String password;

    @Column(name = "user_is_admin")
    @NotNull
    @AssertFalse
    private Boolean isAdmin;

    @OneToOne
    @JoinColumn(name = "cart_id")
    @NotNull(message = "Reference to cart_id is required")
    private Cart cart;

}
