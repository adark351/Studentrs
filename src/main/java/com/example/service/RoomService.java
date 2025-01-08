package com.example.service;

import com.example.entity.Room;
import com.example.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    // Retrieve all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    public List<Room> getRoomsByResidentId(Long residentId) {
        return roomRepository.findByResidentId(residentId);
    }

    // Retrieve a room by its ID
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    // Add a new room
    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }

    // Update an existing room
    public Room updateRoom(Long id, Room updatedRoom) {
        return roomRepository.findById(id).map(room -> {
            room.setSize(updatedRoom.getSize());
            room.setEquipments(updatedRoom.getEquipments());
            room.setAvailable(updatedRoom.isAvailable());
            room.setResident(updatedRoom.getResident()); // Update the resident
            return roomRepository.save(room);
        }).orElseThrow(() -> new RuntimeException("Room not found"));
    }
    // Delete a room by its ID
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    // Get total number of rooms
    public long getTotalRooms() {
        return roomRepository.count();
    }

    // Get number of available rooms
    public long getAvailableRoomsCount() {
        return roomRepository.countByAvailable(true);
    }
}
