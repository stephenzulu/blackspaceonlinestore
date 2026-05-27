package com.example.blackspace.Controller;


import com.example.blackspace.Model.Payment;
import com.example.blackspace.Model.Subscription;
import com.example.blackspace.Model.User;
import com.example.blackspace.Service.Payment.PaymentService;
import com.example.blackspace.Service.Subscription.SubscriptionService;
import com.example.blackspace.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PayController {
    private final UserService userService;
    private final SubscriptionService subscriptionService;


    private final PaymentService paymentService;

    public PayController(UserService userService, SubscriptionService subscriptionService,  PaymentService paymentService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.paymentService = paymentService;
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
        // Get the subscription
        Subscription subscription = subscriptionService.getSubscriptionById(subscriptionId);
        if (subscription == null) {
            redirectAttributes.addFlashAttribute("error", "Subscription not found");
            return "redirect:/account/subscription";
        }

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");

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

        redirectAttributes.addFlashAttribute("success", "Subscription purchased successfully!");

        return "redirect:/account/subscription"; // or redirect to a success page
    }












}

