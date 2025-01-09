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
    @Autowired
    private PasswordEncoder passwordEncoder;
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

    /**
     * Add a new resident.
     */
    public Resident addResident(Resident resident) {
        return residentRepository.save(resident);
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

        // Encode the password before saving
        resident.setPassword(passwordEncoder.encode(resident.getPassword()));
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
    public Resident assignRoom(Long residentId, Long roomId) {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new RuntimeException("Resident not found"));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Ensure the room is available
        if (room.getResident() != null) {
            throw new RuntimeException("Room is already occupied");
        }

        // Update the room and resident relationship
        resident.setRoom(room);
        room.setResident(resident);

        return residentRepository.save(resident);
    }

    public Resident removeRoom(Long residentId) {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new RuntimeException("Resident not found"));

        Room room = resident.getRoom();
        if (room != null) {
            room.setResident(null); // Detach the resident from the room
            resident.setRoom(null); // Detach the room from the resident
        }

        return residentRepository.save(resident);
    }
    }

