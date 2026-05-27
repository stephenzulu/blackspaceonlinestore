package com.example.blackspace.Repository;

import com.example.blackspace.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Fetch payments by username
    List<Payment> findByUsername(String username);


    // Get the latest enabled subscription payment for a given username


    Optional<Payment> findFirstByUsernameOrderByCreatedtimeDesc(String username);



    List<Payment> findByUsernameAndPaymentTypeAndEnabled(
            String username,
            String paymentType,
            Integer enabled
    );






    Optional<Payment> findFirstByUsernameAndEnabledOrderByCreatedtimeDesc(
            String username,
            Integer enabled
    );




}
