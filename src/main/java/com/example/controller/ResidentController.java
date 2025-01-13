package com.example.controller;

import com.example.entity.Payment;
import com.example.entity.Resident;
import com.example.entity.Room;
import com.example.service.PaymentService;
import com.example.service.ResidentService;
import com.example.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Optional;

@Controller
public class ResidentController {

    public ResidentController(ResidentService residentService, RoomService roomService) {
        this.residentService = residentService;
        this.roomService = roomService;
    }

    private final ResidentService residentService;
    private final RoomService roomService;
    @Autowired
     private  PaymentService paymentService;
    // Show the edit form with the current resident details
    @GetMapping("/resident/edit")
    public String showEditForm(Model model, Authentication authentication) {
        String email = authentication.getName();  // Get logged-in user's email
        Resident resident = residentService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Resident not found"));

        model.addAttribute("resident", resident);
        return "resident/update-resident";  // Name of the Thymeleaf template for updating info
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

        if (resident.getRoom() != null) {
            Long roomId = resident.getRoom().getId();

            // Fetch the room assigned to the resident
            Optional<Room> room = roomService.getRoomById(roomId);
            room.ifPresent(r -> model.addAttribute("room", r)); // If the room exists, add it to the model
        } else {
            model.addAttribute("error", "This resident has no room assigned.");
        }

        return "resident/resident-dashboard";
    }


    @GetMapping("/resident/payments/{residentId}")
    public String listPaymentsByResident(@PathVariable Long residentId, Model model) {
        List<Payment> payments = paymentService.getPaymentsByResident(residentId);

        model.addAttribute("payments", payments);
        return "payments/resident-list"; // Assuming there’s a Thymeleaf template named `resident-list.html`
    }
}
