package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@Data
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) // Each payment must have a resident
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;

    @Column(nullable = false) // Due date is required
    private LocalDate dueDate;

    private LocalDate paymentDate; // Set only when payment is made

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private Double amount;

    @PrePersist
    @PreUpdate
    private void updateStatus() {
        if (paymentDate != null) {
            status = PaymentStatus.PAID; // If payment date exists, it's paid
        } else if (LocalDate.now().isAfter(dueDate)) {
            status = PaymentStatus.OVERDUE; // If today is after the due date and not paid
        } else {
            status = PaymentStatus.PENDING; // Default is pending
        }
    }
    public String getFormattedPaymentDate() {
        if (paymentDate == null) {
            return "Not Paid";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return paymentDate instanceof LocalDate ?
                ((LocalDate) paymentDate).format(formatter) :
                new SimpleDateFormat("yyyy-MM-dd").format(paymentDate);
    }

}
