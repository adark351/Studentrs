package com.example.controller;

import com.example.entity.Incident;
import com.example.entity.Resident;
import com.example.entity.Room;
import com.example.entity.Technician;
import com.example.repository.TechnicianRepository;
import com.example.service.IncidentService;
import com.example.service.ResidentService;
import com.example.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class ContentController {
    @Autowired
    private ResidentService residentService;
    @Autowired
    private RoomService roomService;

    public ContentController(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    private final TechnicianRepository technicianRepository;

    // This method handles requests for the login page
    @GetMapping("/login")
    public String login() {
        return "login";  // This should match the name of your login view (login.html)
    }

    // This method handles requests for the admin dashboard page
    @GetMapping("/admin")
    public String adminDashboard() {
        return "adminDashboard";  // This should match the name of your admin dashboard view (adminDashboard.html)
    }


    @GetMapping("/test")
        public String dtest () {
            return "search-form";
        }




    @GetMapping("/admin/layout")
    public String adminLayout() {
        return "admin/admin-layout";  // This should match the name of your admin dashboard view (adminDashboard.html)
    }


    @Autowired
    private IncidentService incidentService;

    // List all incidents
    @GetMapping("admin/incidents")
    public String listIncidents(Model model) {
        List<Incident> incidents = incidentService.getAllIncidents();
        List<Technician> technicians = technicianRepository.findAll(); // Fetch available technicians
        model.addAttribute("incidents", incidents);
        model.addAttribute("technicians", technicians);
        return "incident-list";
    }

    @PostMapping("admin/assign-technician")
    public String assignTechnician(@RequestParam Long incidentId, @RequestParam Long technicianId) {
        incidentService.assignTechnician(incidentId, technicianId);
        return "redirect:/admin/incidents"; // Redirect back to the incident list
    }


    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        // Search for rooms, residents, and ongoing requests (incidents)
        model.addAttribute("rooms", roomService.searchRooms(query));
        model.addAttribute("residents", residentService.searchResidents(query));
        model.addAttribute("incidents", incidentService.searchIncidents(query));
        return "search-results"; // The Thymeleaf template for displaying search results
    }
    @GetMapping("/autocomplete")
    @ResponseBody
    public List<String> autocomplete(@RequestParam("term") String term) {
        // Ensure the term is not null or empty
        if (term == null || term.trim().isEmpty()) {
            return Collections.emptyList(); // Return an empty list if the term is invalid
        }

        // Fetch matching rooms, residents, incidents, and incident types
        List<String> rooms = roomService.searchRooms(term)
                .stream()
                .map(Room::getEquipments)
                .filter(Objects::nonNull) // Ensure no null values
                .collect(Collectors.toList());

        List<String> residents = residentService.searchResidents(term)
                .stream()
                .map(Resident::getName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<String> incidents = incidentService.searchIncidents(term)
                .stream()
                .map(Incident::getDescription)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<String> incidentTypes = incidentService.searchIncidents(term)
                .stream()
                .map(incident -> incident.getType() != null ? incident.getType().toString() : null) // Ensure `IncidentType` is not null
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Combine results into a single list
        List<String> suggestions = new ArrayList<>();
        suggestions.addAll(rooms);
        suggestions.addAll(residents);
        suggestions.addAll(incidents);
        suggestions.addAll(incidentTypes);

        return suggestions;
    }



}

