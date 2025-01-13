package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Exclude fields not explicitly included
@ToString(exclude = "room") // Prevent circular references in toString()
public class Resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    private String name;
    private String email;
    private String phone;
    @OneToOne
    @JoinColumn(name = "room_id") // Foreign key in Resident table
    private Room room;
}
