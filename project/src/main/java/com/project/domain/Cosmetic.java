package com.project.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
}
