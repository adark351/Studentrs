package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Exclude fields not explicitly included
@ToString(exclude = "resident") // Prevent circular references in toString()
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size;
    private String equipments;
    private RoomAvailability available;


    @OneToOne(mappedBy = "room")
    private Resident resident; // Bidirectional mapping
    // Getters and setters

}
