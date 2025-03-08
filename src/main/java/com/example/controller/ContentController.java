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
@GetMapping
public String home() {
    return "index";
}
    // This method handles requests for the login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // This method handles requests for the admin dashboard page
    @GetMapping("/admin")
    public String adminDashboard() {
        return "admin/adminDashboard";  // This should match the name of your admin dashboard view (adminDashboard.html)
    }








    @Autowired
    private IncidentService incidentService;

    // List all incidents
    @GetMapping("admin/incidents")
    public String listIncidents(Model model) {
        List<Incident> incidents = incidentService.getAllIncidents();
        List<Technician> technicians = technicianRepository.findAll();// Fetch available technicians
        List<Room> rooms = roomService.getAllRooms();
        List<Resident> residents = residentService.getAllResidents();
        model.addAttribute("rooms", rooms);
        model.addAttribute("residents", residents);
        model.addAttribute("incidents", incidents);
        model.addAttribute("technicians", technicians);
        return "incident/incident-list";
    }
    @GetMapping("/admin/incidents/filter")
    public String listIncidents(@RequestParam(required = false) Long residentId,
                                @RequestParam(required = false) Long roomId,
                                Model model) {

        List<Incident> incidents = incidentService.filterIncidents(residentId, roomId);
        model.addAttribute("incidents", incidents);
        List<Room> rooms = roomService.getAllRooms();
        List<Resident> residents = residentService.getAllResidents();
        model.addAttribute("rooms", rooms);
        model.addAttribute("residents", residents);
        List<Technician> technicians = technicianRepository.findAll();
        model.addAttribute("technicians", technicians);
        return "incident/incident-list";
    }


    @PostMapping("admin/assign-technician")
    public String assignTechnician(@RequestParam Long incidentId, @RequestParam Long technicianId) {
        incidentService.assignTechnician(incidentId, technicianId);
        return "redirect:/admin/incidents"; // Redirect back to the incident list
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        if (query == null || query.trim().isEmpty()) {
            model.addAttribute("error", "Search query cannot be empty.");
            return "admin/search-results"; // Return the search results page with an error message
        }

        // Search for rooms, residents, and incidents
        List<Room> rooms = roomService.searchRooms(query);
        List<Resident> residents = residentService.searchResidents(query);
        List<Incident> incidents = new ArrayList<>();

        // Include incident description, type, and status in search
        incidents.addAll(incidentService.searchIncidents(query)); // Search by description
        incidents.addAll(incidentService.searchByType(query)); // Search by type
        incidents.addAll(incidentService.searchByStatus(query)); // Search by status

        // Remove duplicates from the incidents list (if any)
        incidents = incidents.stream().distinct().collect(Collectors.toList());

        // Add results to the model
        model.addAttribute("rooms", rooms);
        model.addAttribute("residents", residents);
        model.addAttribute("incidents", incidents);

        // If no results, add a message
        if (rooms.isEmpty() && residents.isEmpty() && incidents.isEmpty()) {
            model.addAttribute("message", "No results found for the query: " + query);
        }

        return "admin/search-results"; // The Thymeleaf template for displaying search results
    }

    @GetMapping("/autocomplete")
    @ResponseBody
    public List<String> autocomplete(@RequestParam("term") String term) {
        if (term == null || term.trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<String> rooms = roomService.searchRooms(term)
                .stream()
                .map(Room::getEquipments)
                .filter(Objects::nonNull)
                .toList();

        List<String> residents = residentService.searchResidents(term)
                .stream()
                .map(Resident::getName)
                .filter(Objects::nonNull)
                .toList();

        List<String> incidentDescriptions = incidentService.searchIncidents(term)
                .stream()
                .map(Incident::getDescription)
                .filter(Objects::nonNull)
                .toList();

        List<String> incidentTypes = incidentService.searchByType(term)
                .stream()
                .map(incident -> incident.getType().name()) // Assuming IncidentType is an Enum
                .toList();

        List<String> incidentStatuses = incidentService.searchByStatus(term)
                .stream()
                .map(incident -> incident.getStatus().name()) // Assuming IncidentStatus is an Enum
                .toList();

        List<String> suggestions = new ArrayList<>();
        suggestions.addAll(rooms);
        suggestions.addAll(residents);
        suggestions.addAll(incidentDescriptions);
        suggestions.addAll(incidentTypes);
        suggestions.addAll(incidentStatuses);

        return suggestions;
    }




}

