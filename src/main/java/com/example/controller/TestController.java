package com.example.controller;

import com.example.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/test")
public class TestController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/send-reminders")
    public String sendReminders() {
        paymentService.sendPaymentReminders();
        return "Payment reminders sent!";
    }
}
