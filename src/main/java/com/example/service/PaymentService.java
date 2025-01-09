package com.example.service;

import com.example.entity.Payment;
import com.example.entity.PaymentStatus;
import com.example.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
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

    public Payment markPaymentAsCompleted(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.COMPLETED);
        return paymentRepository.save(payment);
    }

    public List<Payment> getOverduePayments() {
        return paymentRepository.findByStatusAndPaymentDateBefore(PaymentStatus.PENDING, LocalDate.now());
    }

    public long getOverduePaymentsCount() {
        return paymentRepository.countByStatusAndPaymentDateBefore(PaymentStatus.PENDING, LocalDate.now());
    }
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByResident(Long residentId) {
        return paymentRepository.findByResidentId(residentId);
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
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);  // Returns Optional<Payment>
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
}
