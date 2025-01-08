package com.example.entity;

import jakarta.persistence.*;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size;
    private String equipments;
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "resident_id") private Resident resident;

    // Getters and setters

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setEquipments(String equipments) {
        this.equipments = equipments;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getEquipments() {
        return equipments;
    }

    public Long getId() {
        return id;
    }

    public String getSize() {
        return size;
    }
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }
}
