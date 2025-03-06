package com.example.repository;

import com.example.entity.Room;
import com.example.entity.RoomAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    // Count rooms based on availability status
    long countByAvailable(RoomAvailability available);

    // Find rooms by resident ID
    List<Room> findByResidentId(Long residentId);
    // Search rooms by size or equipment (case-insensitive)
    List<Room> findBySizeContainingIgnoreCaseOrEquipmentsContainingIgnoreCase(String size, String equipments);

    // Find rooms by availability
    List<Room> findByAvailable(RoomAvailability available);
}
