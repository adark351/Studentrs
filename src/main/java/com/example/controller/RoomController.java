package com.example.controller;

import com.example.entity.Room;
import com.example.service.RoomService;
import com.example.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class RoomController {
    @Autowired
    private ResidentService residentService;
    @Autowired
    private RoomService roomService;

//    @GetMapping("admin/rooms/add")
//    public String showAddRoomForm(Model model) {
//        model.addAttribute("room", new Room());
//        return "admin/add-room";  // This is the view where you add room details
//    }

    @PostMapping("admin/rooms/add")
    public String addRoom(@ModelAttribute Room room) {
        roomService.addRoom(room);
        return "redirect:/admin/rooms";  // Redirect to the list of rooms after successful creation
    }
    @GetMapping("/admin/rooms/add")
    public String showAddRoomForm(Model model)
    { model.addAttribute("room", new Room()); model.addAttribute("residents", residentService.getAllResidents());
        return "admin/add-room"; // Name of the Thymeleaf template
         }
    @PostMapping("/admin/rooms/delete/{id}")
    public String deleteRoom(@PathVariable Long id)
    { roomService.deleteRoom(id);
        return "redirect:/admin/rooms"; // Redirect to the list of rooms after successful deletion
         }
}

