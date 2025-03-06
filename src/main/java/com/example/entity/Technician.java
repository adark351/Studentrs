
package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Technician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Name of the technician

    @Column(nullable = false, unique = true)
    private String email; // Email of the technician

    @Column(nullable = false)
    private String phoneNumber; // Phone number of the technician

    @Column(nullable = false)
    private String specialization; // Area of expertise of the technician

}
