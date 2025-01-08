package com.example.controller;

import com.example.entity.Payment;
import com.example.entity.PaymentStatus;
import com.example.entity.Resident;
import com.example.service.PaymentReceiptService;
import com.example.service.PaymentService;
import com.example.service.ResidentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ResidentService residentService;

    // List all payments
    @GetMapping
    public String listPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "payments";
    }

    // List payments by resident
    @GetMapping("/resident/{residentId}")
    public String listPaymentsByResident(@PathVariable Long residentId, Model model) {
        model.addAttribute("payments", paymentService.getPaymentsByResident(residentId));
        return "payments";
    }

    // Add a new payment (GET - show form)
    @GetMapping("/add")
    public String showAddPaymentForm(Model model) {
        model.addAttribute("payment", new Payment());
        model.addAttribute("residents", residentService.getAllResidents());
        return "add-payment";
    }

    // Add a new payment (POST - process form)
    @PostMapping("/add")
    public String addPayment(@RequestParam Long residentId, @RequestParam Double amount, @RequestParam LocalDate paymentDate) {
        Resident resident = residentService.getResidentById(residentId)
                .orElseThrow(() -> new RuntimeException("Resident not found"));

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.PENDING); // Or set the correct status
        payment.setResident(resident); // Associate the resident
        payment.setPaymentDate(paymentDate);

        paymentService.savePayment(payment);
        return "redirect:/admin/payments";
    }



    // Update payment status
    @PostMapping("/{id}/status")
    public String updatePaymentStatus(@PathVariable Long id, @RequestParam PaymentStatus status) {
        paymentService.updatePaymentStatus(id, status);
        return "redirect:/admin/payments";
    }
    @Autowired
    private PaymentReceiptService paymentReceiptService;

    // Other existing methods...

    // Generate and download payment receipt
    @GetMapping("/{paymentId}/receipt")
    public void downloadReceipt(@PathVariable Long paymentId, HttpServletResponse response) throws IOException {
        Payment payment = paymentService.getPaymentById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Generate and send the receipt as a PDF file
        try {
            paymentReceiptService.generateReceipt(payment, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to generate the receipt");
        }
    }
}
