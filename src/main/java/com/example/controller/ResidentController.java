package com.example.controller;

import com.example.entity.Resident;
import com.example.entity.Room;
import com.example.service.ResidentService;
import com.example.service.RoomService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class ResidentController {

    public ResidentController(ResidentService residentService, RoomService roomService) {
        this.residentService = residentService;
        this.roomService = roomService;
    }

    private final ResidentService residentService;
    private final RoomService roomService;

    // Show the edit form with the current resident details
    @GetMapping("/resident/edit")
    public String showEditForm(Model model, Authentication authentication) {
        String email = authentication.getName();  // Get logged-in user's email
        Resident resident = residentService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Resident not found"));

        model.addAttribute("resident", resident);
        return "update-resident";  // Name of the Thymeleaf template for updating info
    }

    // Handle the update request and save the changes
    @PostMapping("/resident/update")
    public String updateResident(@ModelAttribute Resident updatedResident, Authentication authentication) {
        String email = authentication.getName();  // Get logged-in user's email
        Resident resident = residentService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Resident not found"));

        // Ensure we are updating the correct resident based on the email
        updatedResident.setId(resident.getId());
        residentService.updateResident(resident.getId(), updatedResident);

        return "redirect:/resident/dashboard";  // Redirect back to the dashboard after updating
    }
    @GetMapping("resident/dashboard")
    public String residentDashboard(Model model, Authentication authentication) {
        String email = authentication.getName(); // Get the logged-in user's email
        Resident resident = residentService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Resident not found"));

        model.addAttribute("resident", resident);
        long id = resident.getId();
        List<Room> room = roomService.getRoomsByResidentId(id); // Fetch the room assigned to the resident
        model.addAttribute("resident", resident);
        model.addAttribute("room", room);
        return "resident-dashboard";
    }
}
