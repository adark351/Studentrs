package com.example.controller;

import com.example.entity.Payment;
import com.example.service.PaymentReceiptService;
import com.example.service.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
private final PaymentReceiptService paymentReceiptService;

    public PaymentController(PaymentReceiptService paymentReceiptService) {
        this.paymentReceiptService = paymentReceiptService;
    }

    // List all payments
    @GetMapping
    public String listPayments(Model model) {
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        return "payments/list"; // Assuming there’s a Thymeleaf template named `list.html`
    }

    // View payments by resident
    @GetMapping("/resident/{residentId}")
    public String listPaymentsByResident(@PathVariable Long residentId, Model model) {
        List<Payment> payments = paymentService.getPaymentsByResident(residentId);

        model.addAttribute("payments", payments);
        return "payments/resident-list"; // Assuming there’s a Thymeleaf template named `resident-list.html`
    }

    // Show form to create a new payment
    @GetMapping("/new")
    public String showCreatePaymentForm() {
        return "payments/create"; // Assuming there’s a Thymeleaf template named `create.html`
    }

    // Create a new payment
    @PostMapping
    public String createPayment(
            @RequestParam Long residentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
            @RequestParam Double amount) {
        paymentService.createPayment(residentId, dueDate, amount);
        return "redirect:/admin/payments";
    }

    // Mark a payment as paid
    @PostMapping("/{id}/pay")
    public String markPaymentAsPaid(@PathVariable Long id) {
        paymentService.markPaymentAsPaid(id);
        return "redirect:/admin/payments";
    }
    @GetMapping("/{paymentId}/receipt")
    public void downloadReceipt(@PathVariable Long paymentId, HttpServletResponse response) {
        try {
            // Fetch the payment by ID
            Payment payment = paymentService.getPaymentById(paymentId);
            if (payment != null) {
                // Generate the receipt
                paymentReceiptService.generateReceipt(payment, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Payment not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating receipt");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

}
