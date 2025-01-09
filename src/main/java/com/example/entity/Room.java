package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size;
    private String equipments;
    private boolean available;


    @OneToOne(mappedBy = "room", cascade = CascadeType.ALL)
    private Resident resident; // Bidirectional mapping
    // Getters and setters

}
