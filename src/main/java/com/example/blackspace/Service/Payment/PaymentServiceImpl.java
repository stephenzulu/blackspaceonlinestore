package com.example.blackspace.Service.Payment;


import com.example.blackspace.Model.Payment;
import com.example.blackspace.Repository.PaymentRepository;
import com.example.blackspace.SMS.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private static final String ADMIN_EMAIL = "blackspaceonlinestore9@gmail.com";

    private final PaymentRepository paymentRepository;
    private final EmailService emailService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, EmailService emailService) {
        this.paymentRepository = paymentRepository;
        this.emailService = emailService;
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAllByOrderByCreatedtimeDesc();
    }

    @Override
    @Transactional
    public Payment savePayment(Payment payment) {
        payment.setCreatedtime(LocalDateTime.now());
        payment.setStatus("SUCCESS");
        Payment saved = paymentRepository.save(payment);
        sendPaymentNotificationEmail(saved);
        return saved;
    }

    private void sendPaymentNotificationEmail(Payment payment) {
        try {
            String timestamp = payment.getCreatedtime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String html = "<div style='font-family:Arial,sans-serif;max-width:600px;margin:0 auto;'>"
                    + "<div style='background:linear-gradient(135deg,#E8611D,#ff8c42);color:#fff;padding:25px;border-radius:12px 12px 0 0;text-align:center;'>"
                    + "<h2 style='margin:0;'>&#128176; New Payment Received</h2>"
                    + "<p style='margin:5px 0 0;opacity:0.9;'>BlackSpace Online Store</p></div>"
                    + "<div style='padding:25px;background:#fff;border:1px solid #eee;border-radius:0 0 12px 12px;'>"
                    + "<table style='width:100%;border-collapse:collapse;'>"
                    + "<tr style='border-bottom:1px solid #f0f0f0;'>"
                    + "<td style='padding:12px 8px;font-weight:bold;color:#555;width:40%;'>Payment ID:</td>"
                    + "<td style='padding:12px 8px;'>" + payment.getId() + "</td></tr>"
                    + "<tr style='border-bottom:1px solid #f0f0f0;'>"
                    + "<td style='padding:12px 8px;font-weight:bold;color:#555;'>Username:</td>"
                    + "<td style='padding:12px 8px;'>" + payment.getUsername() + "</td></tr>"
                    + "<tr style='border-bottom:1px solid #f0f0f0;'>"
                    + "<td style='padding:12px 8px;font-weight:bold;color:#555;'>Amount:</td>"
                    + "<td style='padding:12px 8px;font-size:1.2em;font-weight:bold;color:#27ae60;'>" + payment.getAmount() + "</td></tr>"
                    + "<tr style='border-bottom:1px solid #f0f0f0;'>"
                    + "<td style='padding:12px 8px;font-weight:bold;color:#555;'>Payment Method:</td>"
                    + "<td style='padding:12px 8px;'>" + (payment.getPaymentMethod() != null ? payment.getPaymentMethod() : "N/A") + "</td></tr>"
                    + "<tr style='border-bottom:1px solid #f0f0f0;'>"
                    + "<td style='padding:12px 8px;font-weight:bold;color:#555;'>Payment Type:</td>"
                    + "<td style='padding:12px 8px;'>" + (payment.getPaymentType() != null ? payment.getPaymentType() : "N/A") + "</td></tr>"
                    + "<tr style='border-bottom:1px solid #f0f0f0;'>"
                    + "<td style='padding:12px 8px;font-weight:bold;color:#555;'>Transaction Ref:</td>"
                    + "<td style='padding:12px 8px;font-family:monospace;'>" + (payment.getTransactionReference() != null ? payment.getTransactionReference() : "N/A") + "</td></tr>"
                    + "<tr style='border-bottom:1px solid #f0f0f0;'>"
                    + "<td style='padding:12px 8px;font-weight:bold;color:#555;'>Subscription ID:</td>"
                    + "<td style='padding:12px 8px;'>" + (payment.getSubscriptionid() != null ? payment.getSubscriptionid() : "N/A") + "</td></tr>"
                    + "<tr style='border-bottom:1px solid #f0f0f0;'>"
                    + "<td style='padding:12px 8px;font-weight:bold;color:#555;'>Status:</td>"
                    + "<td style='padding:12px 8px;'><span style='background:#27ae60;color:#fff;padding:4px 12px;border-radius:20px;font-size:0.85em;'>" + payment.getStatus() + "</span></td></tr>"
                    + "<tr>"
                    + "<td style='padding:12px 8px;font-weight:bold;color:#555;'>Date:</td>"
                    + "<td style='padding:12px 8px;'>" + timestamp + "</td></tr>"
                    + "</table>"
                    + "</div></div>";

            emailService.sendHtmlEmail(ADMIN_EMAIL, "Payment Received: " + payment.getAmount() + " from " + payment.getUsername(), html);
        } catch (Exception e) {
            log.error("Failed to send payment notification email", e);
        }
    }

    @Override
    @Transactional
    public Payment updatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
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
