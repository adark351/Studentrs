package com.example.repository;

import com.example.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResidentRepository extends JpaRepository<Resident, Long> {
    Optional<Resident> findByEmail(String email);
    List<Resident> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);

    boolean existsByEmail(String email);
}
