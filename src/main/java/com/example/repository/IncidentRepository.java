package com.example.repository;

import com.example.entity.Incident;
import com.example.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByResidentId(Long residentId);
    void deleteByRoom(Room room);
    List<Incident> findByDescriptionContainingIgnoreCase(String description);
    @Query("SELECT i FROM Incident i WHERE LOWER(i.status) LIKE LOWER(CONCAT('%', :status, '%'))")
    List<Incident> findByStatus(@Param("status") String status);

    @Query("SELECT i FROM Incident i WHERE LOWER(i.type) LIKE LOWER(CONCAT('%', :type, '%'))")
    List<Incident> findByType(@Param("type") String type);
}
