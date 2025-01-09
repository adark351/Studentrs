package com.example.controller;

import com.example.service.IncidentService;
import com.example.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/residents")
public class AdminResidentController {

    @Autowired
    private ResidentService residentService;

    public AdminResidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    private final IncidentService incidentService;

    /**
     * Display the list of all residents.
     */
    @GetMapping
    public String listResidents(Model model) {
        model.addAttribute("residents", residentService.getAllResidents());
        return "listResident";  // Name of the Thymeleaf template
    }

    /**
     * Delete a resident by ID.
     */
    @PostMapping("/delete/{id}")
    public String deleteResident(@PathVariable Long id) {
        residentService.deleteResident(id);
        return "redirect:/admin/residents";  // Redirect to the updated list of residents
    }


}
