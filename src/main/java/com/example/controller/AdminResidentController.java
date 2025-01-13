package com.example.controller;

import com.example.entity.Resident;
import com.example.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/residents")
public class AdminResidentController {

    private final ResidentService residentService;

    public AdminResidentController(ResidentService residentService) {
        this.residentService = residentService;
    }

    /**
     * Display the list of all residents.
     */
    @GetMapping
    public String listResidents(Model model) {
        model.addAttribute("residents", residentService.getAllResidents());
        return "admin/listResident";  // Name of the Thymeleaf template
    }

    /**
     * Delete a resident by ID.
     */
    @PostMapping("/delete/{id}")
    public String deleteResident(@PathVariable Long id) {
        residentService.deleteResident(id);
        return "redirect:/admin/residents";  // Redirect to the updated list of residents
    }
    @GetMapping("/residents/edit/{id}")
    public String showEditForm(Model model, @PathVariable Long id) {
        Resident resident = residentService.getResidentById(id)
                .orElseThrow(() -> new RuntimeException("Resident not found"));
        model.addAttribute("resident", resident);
        return "resident/update-resident";  // Name of the Thymeleaf template for updating info
    }

}
