package com.example.controller;

import com.example.entity.Resident;
import com.example.entity.Room;
import com.example.service.ResidentService;
import com.example.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private ResidentService residentService;

    // View all rooms
    @GetMapping
    public String viewRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRooms());
        return "admin/rooms";
    }

    // Show the form to add a new room
    @GetMapping("/add")
    public String showAddRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "admin/add-room";
    }

    // Add a new room
    @PostMapping("/add")
    public String addRoom(@ModelAttribute Room room) {
        roomService.addRoom(room);
        return "redirect:/admin/rooms";
    }

    // Delete a room
    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "redirect:/admin/rooms";
    }

    // Show the form to assign a room to a resident
    @GetMapping("/assign/{id}")
    public String showAssignRoomForm(@PathVariable Long id, Model model) {
        model.addAttribute("room", roomService.getRoomById(id).orElseThrow(() -> new RuntimeException("Room not found")));
        model.addAttribute("residents", residentService.getAllResidents());
        return "admin/assign-room";
    }

    // Assign a room to a resident
    @PostMapping("/assign")
    public String assignRoomToResident(@RequestParam Long roomId, @RequestParam Long residentId) {
        roomService.assignRoomToResident(roomId, residentId);
        return "redirect:/admin/rooms";
    }
    @GetMapping("/edit/{id}")
    public String showEditRoomForm(@PathVariable Long id, Model model) {
        Room room=roomService.getRoomById(id).orElseThrow(() -> new RuntimeException("Room not found"));
        List<Resident> residents = residentService.getAllResidents();  // Get all residents
        model.addAttribute("room", room);  // Add the room object to the model
        model.addAttribute("residents", residents);  // Add the list of residents to the model
        return "admin/edit-room";  // Return the view name to show the edit form
    }

    @PostMapping("/update")
    public String updateRoom(@ModelAttribute Room room) {
        roomService.updateRoom(room);  // Call the service method to update the room
        return "redirect:/admin/rooms";  // Redirect to the rooms list after the update
    }

}
