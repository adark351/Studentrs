package com.example.controller;

import com.example.entity.Admin;
import com.example.entity.Resident;
import com.example.service.AdminService;
import com.example.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignupController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ResidentService residentService;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("admin", new Admin());
        model.addAttribute("resident", new Resident());
        return "signup"; // A single signup page for both Admin and Resident
    }

    @PostMapping("/signup/admin")
    public String registerAdmin(@ModelAttribute("admin") Admin admin) {
        adminService.saveAdmin(admin);
        return "redirect:/login"; // Redirect to login page after admin signup
    }

    @PostMapping("/signup/resident")
    public String registerResident(@ModelAttribute("resident") Resident resident) {
        residentService.signupResident(resident);
        return "redirect:/login"; // Redirect to login page after resident signup
    }
}
