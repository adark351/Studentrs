//package com.example.controller;
//import org.springframework.ui.Model;
//import com.example.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class LoginController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/log")
//    public String showLoginPage() {
//        return "log";
//    }
//
//    @PostMapping("/log")
//    public String login(@RequestParam String identifier, @RequestParam String password, @RequestParam String userType, Model model) {
//        try {
//            UserDetails userDetails = userService.authenticate(identifier, password, userType);
//            model.addAttribute("user", userDetails);
//            return "welcome";
//        } catch (UsernameNotFoundException e) {
//            model.addAttribute("error", "Invalid credentials");
//            return "log";
//        }
//    }
//}
