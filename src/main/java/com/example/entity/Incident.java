package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resident_id", nullable = true)
    private Resident resident;

    @Enumerated(EnumType.STRING)
    private IncidentType type;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    private LocalDateTime reportedAt;

    private LocalDateTime resolvedAt;
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
    private Room room;
    @ManyToOne
    private Technician technician;
}
