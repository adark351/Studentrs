package com.example.controller;

import com.example.entity.*;
import com.example.service.IncidentService;
import com.example.service.ResidentService;
import com.example.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/resident/incidents")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    public IncidentController(ResidentService residentService) {
        this.residentService = residentService;
    }

    private final ResidentService residentService;
    @Autowired
    private RoomService roomService;

    // Show the "Report Incident" form
    @GetMapping("/report")
    public String showReportIncidentForm(@RequestParam("roomId") Long roomId, Model model) {
        Room room = roomService.getRoomById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Prepare an empty Incident object for the form
        Incident incident = new Incident();
        incident.setRoom(room); // Pre-fill room information

        model.addAttribute("incident", incident);
        model.addAttribute("room", room);

        return "report-incident"; // The form template for reporting an incident
    }

    // Process the "Report Incident" form submission
    @PostMapping("/report")
    public String reportIncident(@RequestParam Long roomId,
                                 @RequestParam String description,
                                 @RequestParam IncidentType type) {
        Room room = roomService.getRoomById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + roomId));

        Resident resident = residentService.getLoggedInResident()
                .orElseThrow(() -> new RuntimeException("Logged-in resident not found"));

        Incident incident = new Incident();
        incident.setRoom(room);
        incident.setResident(resident);
        incident.setDescription(description);
        incident.setType(type); // Enum value: ELECTRICITY, HEATING, etc.
        incident.setStatus(IncidentStatus.PENDING); // Default status
        incident.setReportedAt(LocalDateTime.now()); // Set current timestamp

        incidentService.saveIncident(incident);

        return "redirect:/resident/dashboard";
    }


}
