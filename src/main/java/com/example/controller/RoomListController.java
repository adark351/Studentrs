package com.example.controller;

import com.example.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomListController {

    @Autowired
    private RoomService roomService;

    @GetMapping("admin/rooms")
    public String viewRooms(Model model) {
        // Add rooms to the model
        model.addAttribute("rooms", roomService.getAllRooms());
        return "admin/rooms"; // Return the name of the Thymeleaf template (rooms.html)
    }
}
