package com.example.controller;

import com.example.dto.DashboardStats;
import com.example.service.IncidentService;
import com.example.service.RoomService;
import com.example.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DashboardController {


    private final RoomService roomService;
    private final IncidentService incidentService;
    private final PaymentService paymentService;

    public DashboardController(IncidentService incidentService, PaymentService paymentService, RoomService roomService) {
        this.incidentService = incidentService;
        this.paymentService = paymentService;
        this.roomService = roomService;
    }

    @GetMapping("/admin/dashboard")
    @ResponseBody
    public DashboardStats getDashboardData() {
        // Get the required data from the services
        long totalRooms = roomService.getAllRoomsCount();
        long availableRooms = roomService.getAvailableRoomsCount();
        long pendingRequestsCount = incidentService.getIncidentsCount();
        long overduePaymentsCount = paymentService.getOverduePaymentsCount();

        // Create and return a DashboardStats object containing the data
        return new DashboardStats(totalRooms, availableRooms, pendingRequestsCount, overduePaymentsCount);
    }

    // DashboardStats class to hold the data

}
