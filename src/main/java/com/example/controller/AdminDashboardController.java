package com.example.controller;



import com.example.entity.Incident;
import com.example.service.IncidentService;
import com.example.service.MaintenanceRequestService;
import com.example.service.PaymentService;
import com.example.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private MaintenanceRequestService maintenanceRequestService;

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private IncidentService incidentService;

    @GetMapping
    public Map<String, Object> getDashboardData() {
        Map<String, Object> dashboardData = new HashMap<>();
        dashboardData.put("totalRooms", roomService.getTotalRooms());
        dashboardData.put("availableRooms", roomService.getAvailableRoomsCount());
       dashboardData.put("pendingRequestsCount", incidentService.getIncidentsCount());
       dashboardData.put("overduePaymentsCount", paymentService.getOverduePaymentsCount());
        return dashboardData;
    }


}
