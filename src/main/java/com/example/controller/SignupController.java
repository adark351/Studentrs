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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class SignupController {

    private static final Logger logger = LoggerFactory.getLogger(SignupController.class);

    @Autowired
    private AdminService adminService;

    @Autowired
    private ResidentService residentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        if (!model.containsAttribute("admin")) {
            model.addAttribute("admin", new Admin());
        }
        if (!model.containsAttribute("resident")) {
            model.addAttribute("resident", new Resident());
        }
        return "signup";
    }

    @PostMapping("/signup/admin")
    public String registerAdmin(
            @ModelAttribute("admin") Admin admin,
            RedirectAttributes redirectAttributes) {

        try {
            // Encode password before saving
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));

            // Save admin
            adminService.saveAdmin(admin);

            // Add success message
            redirectAttributes.addFlashAttribute("signupSuccess", "Admin registration successful!");
            return "redirect:/login";

        } catch (Exception e) {
            logger.error("Error during admin registration", e);
            redirectAttributes.addFlashAttribute("signupError", "Registration failed");
            return "redirect:/signup";
        }
    }

    @PostMapping("/signup/resident")
    public String registerResident(
            @ModelAttribute("resident") Resident resident,
            RedirectAttributes redirectAttributes) {

        try {
            // Log the incoming resident data for debugging
            logger.info("Attempting to register resident: {}", resident.getEmail());

            // Encode password before saving
            resident.setPassword(passwordEncoder.encode(resident.getPassword()));

            // Save resident - wrap with additional try/catch to pinpoint issues
            try {
                residentService.signupResident(resident);
                logger.info("Resident registration successful for: {}", resident.getEmail());
            } catch (Exception e) {
                logger.error("Error in residentService.signupResident(): ", e);
                throw e; // Re-throw to be caught by outer catch
            }

            // Add success message
            redirectAttributes.addFlashAttribute("signupSuccess", "Resident registration successful!");
            return "redirect:/login";

        } catch (Exception e) {
            logger.error("Error during resident registration for {}: {}",
                    resident.getEmail(), e.getMessage(), e);

            // Add more specific error message based on exception type
            String errorMsg = "Registration failed: " + e.getMessage();
            redirectAttributes.addFlashAttribute("signupError", errorMsg);
            redirectAttributes.addFlashAttribute("resident", resident);
            return "redirect:/signup";
        }
    }
}