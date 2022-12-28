package com.project.domains;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class
User {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    @NotNull(message = "Username is required")
    @Size(min = 2, max = 20)
    private String username;

    @Column(name = "password")
    @NotNull(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Password must contain at least one letter and one number")
    @Size(min = 8, message = "Password must have at least eight characters")
    private String password;

    @Column(name = "is_admin")
    @NotNull(message = "Information if user is an admin is required")
    private Boolean is_admin = false;


    public User(String username, String password, Boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.is_admin = isAdmin;
    }

    public User() {};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(Boolean admin) {
        is_admin = admin;
    }

}
