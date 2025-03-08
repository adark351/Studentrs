package com.example.service;

import com.example.entity.Resident;
import com.example.entity.Room;
import com.example.repository.ResidentRepository;
import com.example.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResidentService {

    @Autowired
    private ResidentRepository residentRepository;
    @Autowired
    private RoomRepository roomRepository;
    /**
     * Retrieve all residents.
     */
    public List<Resident> getAllResidents() {
        return residentRepository.findAll();
    }

    /**
     * Retrieve a resident by their ID.
     */
    public Optional<Resident> getResidentById(Long id) {
        return residentRepository.findById(id);
    }
    public Resident getResidentByIde(Long id) {
        return residentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resident not found"));
    }
    /**
     * Update an existing resident.
     */
    public Resident updateResident(Long id, Resident updatedResident) {
        return residentRepository.findById(id).map(resident -> {
            resident.setName(updatedResident.getName());
            resident.setEmail(updatedResident.getEmail());
            resident.setPhone(updatedResident.getPhone());
            return residentRepository.save(resident);
        }).orElseThrow(() -> new RuntimeException("Resident not found"));
    }




    public Resident signupResident(Resident resident) {
        Optional<Resident> existingResident = residentRepository.findByEmail(resident.getEmail());
        if (existingResident.isPresent()) {
            throw new RuntimeException("Email already in use.");
        }
        return residentRepository.save(resident);
    }
    /**
     * Delete a resident by their ID.
     */
    @Transactional
    public void deleteResident(Long id) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resident not found"));

        // Remove the association with the room, if any
        Room room = resident.getRoom();
        if (room != null) {
            room.setResident(null);
            roomRepository.save(room);
        }

        // Delete the resident
        residentRepository.deleteById(id);
    }

    public Optional<Resident> findByEmail(String email) {
        return residentRepository.findByEmail(email);
    }
    public Optional<Resident> getLoggedInResident() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return residentRepository.findByEmail(user.getUsername());
        }

        return Optional.empty();
    }

    public List<Resident> searchResidents(String query) {
        return residentRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
    }

}

