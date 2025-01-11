package com.example.service;

import com.example.entity.Resident;
import com.example.entity.Room;
import com.example.repository.ResidentRepository;
import com.example.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ResidentRepository residentRepository;
    private final  ResidentService residentService;

    public RoomService(ResidentService residentService) {
        this.residentService = residentService;
    }

    // Get all rooms
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
   public long getAllRoomsCount() {
        return roomRepository.count();
    }
    public long getAvailableRoomsCount() {
        return roomRepository.countByAvailable(true);
    }
    public List<Room> searchRooms(String query) {
        return roomRepository.findBySizeContainingIgnoreCaseOrEquipmentsContainingIgnoreCase(query, query);
    }


    // Add a new room
    public Room addRoom(Room room) {
        room.setAvailable(true); // Set default availability
        return roomRepository.save(room);
    }

    // Get a room by its ID
    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }



    public void updateRoom(Room room) {
        Room existingRoom = roomRepository.findById(room.getId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Set the room's properties
        existingRoom.setSize(room.getSize());
        existingRoom.setEquipments(room.getEquipments());
        existingRoom.setAvailable(room.isAvailable());

        // Set the resident if the id is provided (i.e., not null)
        if (room.getResident() != null && room.getResident().getId() != null) {
            Resident resident = residentService.getResidentByIde(room.getResident().getId());
            existingRoom.setResident(resident);
        }

        roomRepository.save(existingRoom);  // Save the updated room
    }

    // Delete a room
// In RoomService or Controller
    @Transactional
    public void deleteRoom(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Unlink the room from resident, if any
        if (room.getResident() != null) {
            room.getResident().setRoom(null);  // Remove the reference to the room
        }

        // Now delete the room
        roomRepository.delete(room);
    }

    // Assign a room to a resident
    public void assignRoomToResident(Long roomId, Long residentId) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        Optional<Resident> residentOptional = residentRepository.findById(residentId);

        if (roomOptional.isPresent() && residentOptional.isPresent()) {
            Room room = roomOptional.get();
            Resident resident = residentOptional.get();

            // Unassign the current resident if the room is already occupied
            if (room.getResident() != null) {
                Resident currentResident = room.getResident();
                currentResident.setRoom(null);
                residentRepository.save(currentResident);
            }

            // Assign the room to the new resident
            room.setResident(resident);
            resident.setRoom(room);

            // Save both entities
            roomRepository.save(room);
            residentRepository.save(resident);
        } else {
            throw new RuntimeException("Room or Resident not found.");
        }
    }
}
