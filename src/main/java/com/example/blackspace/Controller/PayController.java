package com.example.blackspace.Controller;


import com.example.blackspace.Model.Payment;
import com.example.blackspace.Model.Subscription;
import com.example.blackspace.Model.User;
import com.example.blackspace.SMS.EmailService;
import com.example.blackspace.Service.Payment.PaymentService;
import com.example.blackspace.Service.Store.StoreService;
import com.example.blackspace.Service.Subscription.SubscriptionService;
import com.example.blackspace.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class PayController {

    private static final Logger log = LoggerFactory.getLogger(PayController.class);

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final StoreService storeService;
    private final PaymentService paymentService;
    private final EmailService emailService;

    public PayController(UserService userService, SubscriptionService subscriptionService,
                         StoreService storeService, PaymentService paymentService, EmailService emailService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.storeService = storeService;
        this.paymentService = paymentService;
        this.emailService = emailService;
    }

    @GetMapping("/admin/payments")
    public String paymentsPage(Model model, HttpSession session) {
        model.addAttribute("payments", paymentService.getAllPayments());

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");

        // Get the user with username "boo1"
        User user = userService.findByUsername(username); // fetch User object
        if (user == null) {
            user = new User();           // create empty User
            user.setFirstName("User Null");  // default value to prevent template errors
        }

        model.addAttribute("user", user);


        return "/admin/managepayments";
    }

    @PostMapping("/admin/payments/add")
    public String addPayment(@ModelAttribute Payment payment,
                             RedirectAttributes redirectAttributes) {
        paymentService.savePayment(payment);
        redirectAttributes.addFlashAttribute("success", "Payment recorded successfully");
        return "redirect:/admin/payments";
    }

    @PostMapping("/admin/payments/update")
    public String updatePayment(@ModelAttribute Payment payment,
                                RedirectAttributes redirectAttributes) {
        paymentService.updatePayment(payment);
        redirectAttributes.addFlashAttribute("success", "Payment updated successfully");
        return "redirect:/admin/payments";
    }

    @GetMapping("/admin/payments/delete/{id}")
    public String deletePayment(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        paymentService.deletePayment(id);
        redirectAttributes.addFlashAttribute("success", "Payment deleted");
        return "redirect:/admin/payments";
    }


    @GetMapping("/account/payments")
    public String userpayments(Model model,HttpSession session) {

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");

        // Fetch user (replace "1002b" with dynamic Principal if needed)
        User user = userService.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setFirstName("User Null");
        }
        model.addAttribute("user", user);

        // Fetch payments only for this username
        model.addAttribute("payments", paymentService.getPaymentsByUsername(username));

        return "/users/payments"; // Thymeleaf template
    }



    @PostMapping("/account/subscription")
    public String paymentSubscription(
            @RequestParam("subscriptionId") Long subscriptionId,
            RedirectAttributes redirectAttributes,HttpSession session
    ) {
        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");

        // Check if user has set up a store first
        if (storeService.getStoreByUsername(username) == null) {
            redirectAttributes.addFlashAttribute("error", "Please set up your store before purchasing a subscription.");
            return "redirect:/account/dashboard";
        }

        // Get the subscription
        Subscription subscription = subscriptionService.getSubscriptionById(subscriptionId);
        if (subscription == null) {
            redirectAttributes.addFlashAttribute("error", "Subscription not found");
            return "redirect:/account/subscription";
        }

        // Get the user (replace with real logged-in user)
        User user = userService.findByUsername(username); // later replace with Principal
        if (user == null) {
            user = new User();
            user.setFirstName("User Null");
        }

        // Create a payment record
        Payment payment = new Payment();
        payment.setUsername(user.getUsername());
        payment.setAmount(subscription.getAmount());
        payment.setPaymentType("Subscription");
        payment.setSubscriptionid(""+subscription.getId());
        payment.setTransactionReference("Purchase of subscription: " + subscription.getName());
        payment.setPaymentMethod("Manual"); // or whatever payment method you integrate
        payment.setStatus("SUCCESS"); // default success for now
        payment.setCreatedtime(java.time.LocalDateTime.now());

        paymentService.savePayment(payment);

        // Send payment receipt email
        sendPaymentReceipt(user, subscription, payment);

        redirectAttributes.addFlashAttribute("success", "Subscription purchased successfully!");

        return "redirect:/account/subscription";
    }

    private void sendPaymentReceipt(User user, Subscription subscription, Payment payment) {
        try {
            LocalDateTime now = payment.getCreatedtime() != null ? payment.getCreatedtime() : LocalDateTime.now();
            String date = now.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
            int durationDays = Integer.parseInt(subscription.getDurationtime());
            String expiryDate = now.plusDays(durationDays).format(DateTimeFormatter.ofPattern("dd MMM yyyy"));

            String html = buildReceiptEmail(user, subscription, payment, date, expiryDate, durationDays);
            emailService.sendHtmlEmail(user.getEmail(), "BlackSpace - Payment Receipt", html);
            log.info("Payment receipt sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send payment receipt email", e);
        }
    }

    private String buildReceiptEmail(User user, Subscription subscription, Payment payment,
                                     String date, String expiryDate, int durationDays) {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head>"
            + "<body style='margin:0;padding:0;background:#F6F6F4;font-family:Arial,sans-serif;'>"
            + "<div style='max-width:500px;margin:30px auto;background:#fff;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,.1);'>"
            + "<div style='background:linear-gradient(135deg,#0E0E11,#3A3A40);padding:30px;text-align:center;'>"
            + "<h1 style='color:#fff;margin:12px 0 4px;font-size:20px;letter-spacing:1px;'>BlackSpace Online Store</h1>"
            + "<p style='color:rgba(255,255,255,.6);margin:0;font-size:13px;'>Payment Receipt</p>"
            + "</div>"
            + "<div style='padding:28px;'>"
            + "<p style='font-size:16px;color:#3A3A40;'>Hello <strong>" + user.getFirstName() + "</strong>,</p>"
            + "<p style='color:#3A3A40;'>Thank you for your payment! Your subscription has been activated successfully.</p>"
            + "<div style='background:#E6F2EC;border-left:4px solid #0F7A4E;padding:12px 16px;border-radius:6px;margin:16px 0;'>"
            + "<p style='margin:0;color:#0F7A4E;font-weight:600;font-size:14px;'>Payment Successful</p>"
            + "</div>"
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
            + "<td style='padding:10px 0;text-align:right;color:#3A3A40;font-weight:600;'>" + payment.getPaymentMethod() + "</td>"
            + "</tr>"
            + "<tr style='border-bottom:1px solid #E7E7EA;'>"
            + "<td style='padding:10px 0;color:#8A8A93;'>Transaction Ref</td>"
            + "<td style='padding:10px 0;text-align:right;color:#3A3A40;font-weight:600;font-size:12px;'>" + payment.getTransactionReference() + "</td>"
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
            + "<div style='background:#F6F6F4;padding:20px;text-align:center;border-top:1px solid #E7E7EA;'>"
            + "<p style='margin:0 0 4px;color:#3A3A40;font-weight:600;font-size:13px;'>BlackSpace Online Store</p>"
            + "<p style='margin:0;color:#8A8A93;font-size:12px;'>Lusaka, Zambia | +260 960 847 099</p>"
            + "<p style='margin:4px 0 0;color:#8A8A93;font-size:11px;'>Sent from: <a href='mailto:blackspaceonlinestore9@gmail.com' style='color:#E8611D;text-decoration:none;'>blackspaceonlinestore9@gmail.com</a></p>"
            + "<p style='margin:4px 0 0;color:#8A8A93;font-size:11px;'>www.blackspaceonline.com</p>"
            + "</div>"
            + "</div></body></html>";
    }










}

