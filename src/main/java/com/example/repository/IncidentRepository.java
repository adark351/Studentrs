package com.example.repository;

import com.example.entity.Incident;
import com.example.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByResidentId(Long residentId);
    void deleteByRoom(Room room);
    List<Incident> findByDescriptionContainingIgnoreCase(String description);

}
