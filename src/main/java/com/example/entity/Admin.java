package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private String firstName;


    private String lastName;

    private String email;

    // Getters and Setters

}
