package com.example.blackspace.Service.Payment;


import com.example.blackspace.Model.Payment;
import com.example.blackspace.Repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAllByOrderByCreatedtimeDesc();
    }

    @Override
    public Payment savePayment(Payment payment) {
        payment.setCreatedtime(LocalDateTime.now());
        payment.setStatus("SUCCESS");
        return paymentRepository.save(payment);
    }

    @Override
    public Payment updatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Override
    public List<Payment> getPaymentsByUsername(String username) {
        return paymentRepository.findByUsernameOrderByCreatedtimeDesc(username);
    }






    @Override
    public Optional<Payment> getLatestPayment(String username) {
        return paymentRepository.findFirstByUsernameOrderByCreatedtimeDesc(username);
    }

    @Override
    public boolean isSubscriptionEnabled(String username) {

        Optional<Payment> paymentOpt =
                paymentRepository.findFirstByUsernameOrderByCreatedtimeDesc(username);

        if (paymentOpt.isEmpty()) {
            return false;
        }

        Payment payment = paymentOpt.get();

        return
                "SUBSCRIPTION".equalsIgnoreCase(payment.getPaymentType())
                        && payment.getEnabled() != null
                        && payment.getEnabled() == 1;
    }




    @Override
    public Payment getActiveSubscription(String username) {
        return paymentRepository
                .findFirstByUsernameAndEnabledOrderByCreatedtimeDesc(username, 1)
                .orElse(null);
    }





}
