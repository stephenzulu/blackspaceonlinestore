package com.example.blackspace.Lencopayment;

import com.example.blackspace.Model.Subscription;
import com.example.blackspace.Service.Subscription.SubscriptionService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PaymentVerifyController {

    private static final Logger log = LoggerFactory.getLogger(PaymentVerifyController.class);

    private final SubscriptionService subscriptionService;

    public PaymentVerifyController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/api/subscription/select")
    public ResponseEntity<Map<String, Object>> selectSubscription(
            @RequestBody Map<String, Number> body,
            HttpSession session) {

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");
        if (username == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Not logged in"));
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
}
