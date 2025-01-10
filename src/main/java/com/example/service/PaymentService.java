package com.example.service;

import com.example.entity.Payment;
import com.example.entity.PaymentStatus;
import com.example.entity.Resident;
import com.example.repository.PaymentRepository;
import com.example.repository.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    @Autowired
    private EmailService emailService;
    @Autowired
    private PaymentRepository paymentRepository;
    private final ResidentRepository residentRepository;

    public PaymentService(ResidentRepository residentRepository) {
        this.residentRepository = residentRepository;
    }

    public Payment markPaymentAsCompleted(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.PAID);
        return paymentRepository.save(payment);
    }

    public List<Payment> getOverduePayments() {
        return paymentRepository.findByStatusAndPaymentDateBefore(PaymentStatus.PENDING, LocalDate.now());
    }

    public long getOverduePaymentsCount() {
        return paymentRepository.countByStatus(PaymentStatus.OVERDUE);
    }
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }



    public List<Payment> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }

    public Payment addPayment(Payment payment) {
        payment.setStatus(PaymentStatus.PENDING);
        return paymentRepository.save(payment);
    }

    public void updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(status);
        payment.setPaymentDate(LocalDate.now());
        paymentRepository.save(payment);
    }

    // Create a new payment
    public Payment createPayment(Long residentId, LocalDate dueDate, Double amount) {
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new RuntimeException("Resident not found"));

        Payment payment = new Payment();
        payment.setResident(resident);
        payment.setDueDate(dueDate);
        payment.setAmount(amount);

        return paymentRepository.save(payment);
    }

    // Mark payment as paid
    public Payment markPaymentAsPaid(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setPaymentDate(LocalDate.now());
        return paymentRepository.save(payment);
    }

    // Get all payments

    public Payment getPaymentById(Long paymentId) {
        Optional<Payment> payment = paymentRepository.findById(paymentId);
        return payment.orElse(null);
    }
    // Get payments by resident
    public List<Payment> getPaymentsByResident(Long residentId) {

        return paymentRepository.findByResidentId(residentId);
    }
    



    @Scheduled(cron = "0 0 0 * * ?")
    public void sendPaymentReminders() {
        List<Payment> overduePayments = paymentRepository.findByStatus(PaymentStatus.OVERDUE);
        for (Payment payment : overduePayments) {
            String email = payment.getResident().getEmail(); // Assuming you have an email field in Resident
            String subject = "Payment Reminder";
            String text = "Dear " + payment.getResident().getName() + ",\n\n" +
                    "This is a reminder that your payment for the amount of " + payment.getAmount() + " is overdue.\n" +
                    "Please make the payment as soon as possible.\n\n" +
                    "Thank you for your attention.";
            emailService.sendPaymentReminder(email, subject, text);
        }
    }
 public void deletePaymentById(Long id) {
        paymentRepository.deleteById(id);
    }
}
