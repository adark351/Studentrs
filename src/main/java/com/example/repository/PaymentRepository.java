package com.example.repository;

import com.example.entity.Payment;
import com.example.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find all overdue payments (status is PENDING and past due date)
    List<Payment> findByStatusAndPaymentDateBefore(PaymentStatus status, LocalDate currentDate);

    // Count overdue payments
    long countByStatus(PaymentStatus status);

    // Find payments by resident
    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByResidentId(Long residentId);

    Optional<Payment> findById(Long id);


}
