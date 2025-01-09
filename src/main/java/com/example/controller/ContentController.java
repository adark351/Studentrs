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

import java.util.List;

@Controller
public class ContentController {
    @Autowired
    private ResidentService residentService;
    @Autowired private RoomService roomService;

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
    @GetMapping("/admin/layout")
    public String adminLayout() {
        return "admin/admin-layout";  // This should match the name of your admin dashboard view (adminDashboard.html)
    }
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";}

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

    }

