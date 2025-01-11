package com.example.repository;
import com.example.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    long countByAvailable(boolean available);
    List<Room> findByResidentId(Long residentId);
    long countByResidentIsNotNull();
    List<Room> findAll();
    List<Room> findBySizeContainingIgnoreCaseOrEquipmentsContainingIgnoreCase(String size, String equipments);
}
