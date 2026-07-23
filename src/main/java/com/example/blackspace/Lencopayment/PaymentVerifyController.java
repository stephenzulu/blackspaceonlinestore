package com.example.blackspace.Lencopayment;

import com.example.blackspace.Model.Subscription;
import com.example.blackspace.Model.User;
import com.example.blackspace.Repository.user.UserRepository;
import com.example.blackspace.SMS.EmailService;
import com.example.blackspace.Service.Store.StoreService;
import com.example.blackspace.Service.Subscription.SubscriptionService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class PaymentVerifyController {

    private static final Logger log = LoggerFactory.getLogger(PaymentVerifyController.class);

    private final SubscriptionService subscriptionService;
    private final StoreService storeService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public PaymentVerifyController(SubscriptionService subscriptionService, StoreService storeService,
                                   EmailService emailService, UserRepository userRepository) {
        this.subscriptionService = subscriptionService;
        this.storeService = storeService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @PostMapping("/api/subscription/select")
    public ResponseEntity<Map<String, Object>> selectSubscription(
            @RequestBody Map<String, Number> body,
            HttpSession session) {

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");
        if (username == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));
        }

        if (storeService.getStoreByUsername(username) == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Please set up your store before purchasing a subscription."));
        }

        Number rawId = body.get("subscriptionId");
        Long subscriptionId = rawId.longValue();
        Subscription subscription = subscriptionService.getSubscriptionById(subscriptionId);

        session.setAttribute("SUBSCRIPTION_ID", subscriptionId);

        Map<String, Object> result = new HashMap<>();
        result.put("amount", subscription.getAmount());
        result.put("name", subscription.getName());
        result.put("subscriptionId", subscription.getId());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/payments/complete")
    public ResponseEntity<Map<String, Object>> completePayment(
            @RequestBody Map<String, String> body,
            HttpSession session) {

        String reference = body.get("reference");
        log.info("=== PAYMENT COMPLETE called === reference={}", reference);

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");
        if (username == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("reference", reference);

        try {
            Object rawSubId = session.getAttribute("SUBSCRIPTION_ID");
            Long subscriptionId = rawSubId != null ? ((Number) rawSubId).longValue() : null;

            if (subscriptionId != null) {
                subscriptionService.upgradeUserSubscription(username, subscriptionId, reference);
                log.info("Subscription upgraded for user={}, subscriptionId={}", username, subscriptionId);
                result.put("success", true);
                result.put("status", "success");

                // Send payment receipt email
                sendPaymentReceipt(username, subscriptionId, reference);
            } else {
                log.warn("No SUBSCRIPTION_ID in session");
                result.put("success", false);
                result.put("status", "no subscription selected");
            }
        } catch (Exception e) {
            log.error("Payment completion error", e);
            result.put("success", false);
            result.put("status", "error: " + e.getMessage());
        }

        return ResponseEntity.ok(result);
    }

    private void sendPaymentReceipt(String username, Long subscriptionId, String reference) {
        try {
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                log.warn("Cannot send receipt: user not found for username={}", username);
                return;
            }
            User user = userOpt.get();

            Subscription subscription = subscriptionService.getSubscriptionById(subscriptionId);
            LocalDateTime now = LocalDateTime.now();
            String date = now.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
            int durationDays = Integer.parseInt(subscription.getDurationtime());
            String expiryDate = now.plusDays(durationDays).format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

            String html = buildReceiptEmail(user, subscription, reference, date, expiryDate, durationDays);
            emailService.sendHtmlEmail(user.getEmail(), "BlackSpace - Payment Receipt", html);
            log.info("Payment receipt sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send payment receipt email", e);
        }
    }

    private String buildReceiptEmail(User user, Subscription subscription, String reference,
                                     String date, String expiryDate, int durationDays) {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head>"
            + "<body style='margin:0;padding:0;background:#F6F6F4;font-family:Arial,sans-serif;'>"
            + "<div style='max-width:500px;margin:30px auto;background:#fff;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,.1);'>"
            // Header
            + "<div style='background:linear-gradient(135deg,#0E0E11,#3A3A40);padding:30px;text-align:center;'>"
            + "<h1 style='color:#fff;margin:12px 0 4px;font-size:20px;letter-spacing:1px;'>BlackSpace Online Store</h1>"
            + "<p style='color:rgba(255,255,255,.6);margin:0;font-size:13px;'>Payment Receipt</p>"
            + "</div>"
            // Body
            + "<div style='padding:28px;'>"
            + "<p style='font-size:16px;color:#3A3A40;'>Hello <strong>" + user.getFirstName() + "</strong>,</p>"
            + "<p style='color:#3A3A40;'>Thank you for your payment! Your subscription has been activated successfully.</p>"
            // Success badge
            + "<div style='background:#E6F2EC;border-left:4px solid #0F7A4E;padding:12px 16px;border-radius:6px;margin:16px 0;'>"
            + "<p style='margin:0;color:#0F7A4E;font-weight:600;font-size:14px;'>Payment Successful</p>"
            + "</div>"
            // Receipt details table
            + "<table style='width:100%;border-collapse:collapse;margin:20px 0;font-size:14px;'>"
            + "<tr style='border-bottom:1px solid #E7E7EA;'>"
            + "<td style='padding:10px 0;color:#8A8A93;'>Plan</td>"
            + "<td style='padding:10px 0;text-align:right;color:#3A3A40;font-weight:600;'>" + subscription.getName() + "</td>"
            + "</tr>"
            + "<tr style='border-bottom:1px solid #E7E7EA;'>"
            + "<td style='padding:10px 0;color:#8A8A93;'>Amount Paid</td>"
            + "<td style='padding:10px 0;text-align:right;color:#3A3A40;font-weight:600;'>ZMW " + subscription.getAmount() + "</td>"
            + "</tr>"
            + "<tr style='border-bottom:1px solid #E7E7EA;'>"
            + "<td style='padding:10px 0;color:#8A8A93;'>Payment Method</td>"
            + "<td style='padding:10px 0;text-align:right;color:#3A3A40;font-weight:600;'>Lenco</td>"
            + "</tr>"
            + "<tr style='border-bottom:1px solid #E7E7EA;'>"
            + "<td style='padding:10px 0;color:#8A8A93;'>Transaction Ref</td>"
            + "<td style='padding:10px 0;text-align:right;color:#3A3A40;font-weight:600;font-size:12px;'>" + reference + "</td>"
            + "</tr>"
            + "<tr style='border-bottom:1px solid #E7E7EA;'>"
            + "<td style='padding:10px 0;color:#8A8A93;'>Date</td>"
            + "<td style='padding:10px 0;text-align:right;color:#3A3A40;font-weight:600;'>" + date + "</td>"
            + "</tr>"
            + "<tr style='border-bottom:1px solid #E7E7EA;'>"
            + "<td style='padding:10px 0;color:#8A8A93;'>Duration</td>"
            + "<td style='padding:10px 0;text-align:right;color:#3A3A40;font-weight:600;'>" + durationDays + " days</td>"
            + "</tr>"
            + "<tr>"
            + "<td style='padding:10px 0;color:#8A8A93;'>Expires On</td>"
            + "<td style='padding:10px 0;text-align:right;color:#3A3A40;font-weight:600;'>" + expiryDate + "</td>"
            + "</tr>"
            + "</table>"
            + "<p style='color:#8A8A93;font-size:13px;margin-top:16px;'>If you have any questions about this payment, please contact our support team.</p>"
            + "<p style='color:#8A8A93;font-size:13px;'>Email: blackspaceonlinestore9@gmail.com</p>"
            + "</div>"
            // Footer
            + "<div style='background:#F6F6F4;padding:20px;text-align:center;border-top:1px solid #E7E7EA;'>"
            + "<p style='margin:0 0 4px;color:#3A3A40;font-weight:600;font-size:13px;'>BlackSpace Online Store</p>"
            + "<p style='margin:0;color:#8A8A93;font-size:12px;'>Lusaka, Zambia | +260 960 847 099</p>"
            + "<p style='margin:4px 0 0;color:#8A8A93;font-size:11px;'>Sent from: <a href='mailto:blackspaceonlinestore9@gmail.com' style='color:#E8611D;text-decoration:none;'>blackspaceonlinestore9@gmail.com</a></p>"
            + "<p style='margin:4px 0 0;color:#8A8A93;font-size:11px;'>www.blackspaceonline.com</p>"
            + "</div>"
            + "</div></body></html>";
    }
}
