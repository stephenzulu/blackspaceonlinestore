package com.example.blackspace.PaymentConfig;


import com.example.blackspace.Model.Subscription;
import com.example.blackspace.Service.Subscription.SubscriptionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PaymentController {

    private final FlutterwaveService flutterwaveService;
    private final SubscriptionService subscriptionService;

    public PaymentController(FlutterwaveService flutterwaveService, SubscriptionService subscriptionService) {
        this.flutterwaveService = flutterwaveService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/payment-success")
    public String paymentSuccess(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String transaction_id,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        // USER CANCELLED PAYMENT
        if ("cancelled".equalsIgnoreCase(status)) {
            redirectAttributes.addFlashAttribute("error", "Payment was cancelled.");
            return "redirect:/account/subscription";
        }

        try {
            if ("successful".equalsIgnoreCase(status) && transaction_id != null) {

                boolean verified = flutterwaveService.verifyTransaction(transaction_id);

                if (verified) {
                    String username = (String) session.getAttribute("LOGGED_IN_USERNAME");
                    Long subscriptionId = (Long) session.getAttribute("SUBSCRIPTION_ID");

                    Subscription subscription =
                            subscriptionService.getSubscriptionById(subscriptionId);

                    subscriptionService.upgradeUserSubscription(username, subscription.getId());

                    redirectAttributes.addFlashAttribute(
                            "success", "Subscription upgraded successfully!"
                    );
                } else {
                    redirectAttributes.addFlashAttribute(
                            "error", "Payment verification failed."
                    );
                }

            } else {
                redirectAttributes.addFlashAttribute(
                        "error", "Payment failed."
                );
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error", "Payment error: " + e.getMessage()
            );
        }

        return "redirect:/account/subscription";
    }




    @GetMapping("/payment-cancelled")
    public String paymentCancelled() {
        return "payment-cancelled"; // This should match the template name
    }



}

