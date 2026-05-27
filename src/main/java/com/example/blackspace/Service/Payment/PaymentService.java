package com.example.blackspace.Service.Payment;

import com.example.blackspace.Model.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {

    List<Payment> getAllPayments();

    Payment savePayment(Payment payment);

    Payment updatePayment(Payment payment);

    void deletePayment(Long id);

    Payment getPaymentById(Long id);

    // New method
    List<Payment> getPaymentsByUsername(String username);



    Optional<Payment> getLatestPayment(String username);

    boolean isSubscriptionEnabled(String username);






    Payment getActiveSubscription(String username);























}
