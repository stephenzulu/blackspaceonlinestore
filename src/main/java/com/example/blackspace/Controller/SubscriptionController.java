package com.example.blackspace.Controller;


import com.example.blackspace.Model.Subscription;
import com.example.blackspace.Model.User;
import com.example.blackspace.PaymentConfig.FlutterwaveService;
import com.example.blackspace.Service.Payment.PaymentService;
import com.example.blackspace.Service.Subscription.SubscriptionService;
import com.example.blackspace.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SubscriptionController {
    private final UserService userService;

    private final PaymentService paymentService;

    private final SubscriptionService subscriptionService;

    private final FlutterwaveService flutterwaveService;

    public SubscriptionController(UserService userService, PaymentService paymentService, SubscriptionService subscriptionService, FlutterwaveService flutterwaveService) {
        this.userService = userService;
        this.paymentService = paymentService;
        this.subscriptionService = subscriptionService;
        this.flutterwaveService = flutterwaveService;
    }





    // View membership page
    @GetMapping("/admin/subscription")
    public String viewSubscription(Model model, HttpSession session) {
        model.addAttribute("subscriptions", subscriptionService.getAllSubscription());
        model.addAttribute("subscription", new Subscription());

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");

        // Get the user with username "boo1"
        User user = userService.findByUsername(username); // fetch User object
        if (user == null) {
            user = new User();           // create empty User
            user.setFirstName("User Null");  // default value to prevent template errors
        }

        model.addAttribute("user", user);


        return "admin/managesubscription";
    }

    // Add membership
    @PostMapping("/admin/subscription/add")
    public String addmembership(@ModelAttribute Subscription subscription, RedirectAttributes redirectAttributes) {
        try {
            subscriptionService.saveSubscription(subscription);
            redirectAttributes.addFlashAttribute("success", "subscription added successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/subscription";
    }

    @PostMapping("/admin/subscription/update")
    public String updatesubscription(@ModelAttribute Subscription subscription,  RedirectAttributes redirectAttributes) {
        try {
            subscriptionService.updateSubscription(subscription);
            redirectAttributes.addFlashAttribute("success", "subscription updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/subscription";
    }


    // Delete membership
    @GetMapping("/admin/subscription/delete/{id}")
    public String deletesubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return "redirect:/admin/subscription";
    }

    @GetMapping("/admin/subscription/all")
    public String gettAllsubscription(Model model) {
        model.addAttribute("subscriptions", subscriptionService.getAllSubscription());
       // model.addAttribute("subscription", new Membership());
        return "productstock"; // this matches your Thymeleaf template
    }



    // View membership page
    @GetMapping("/account/subscription")
    public String useraddSubscription(Model model,HttpSession session) {
        model.addAttribute("subscriptions", subscriptionService.getAllSubscription());
        model.addAttribute("subscription", new Subscription());

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");

        // Get the user with username "boo1"
        User user = userService.findByUsername(username); // fetch User object
        if (user == null) {
            user = new User();           // create empty User
            user.setFirstName("User Null");  // default value to prevent template errors
        }

        model.addAttribute("user", user);




        boolean subscriptionActive = paymentService.isSubscriptionEnabled(username);
        model.addAttribute("subscriptionActive", subscriptionActive);
        model.addAttribute("subscriptions", subscriptionService.getAllSubscription());
        model.addAttribute("username", username);



        return "users/subscription";
    }






    @PostMapping("/account/subscription/upgrade")
    public String upgradeSubscription(
            @RequestParam("subscriptionId") Long subscriptionId,
            RedirectAttributes redirectAttributes,HttpSession session) {

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");



        //  SAVE IN SESSION
        session.setAttribute("SUBSCRIPTION_ID", subscriptionId);

        try {
            // Fetch the user
            User user = userService.findByUsername(username);

            // Fetch the subscription to get the price
            Subscription subscription = subscriptionService.getSubscriptionById(subscriptionId);
            double amount = Double.parseDouble(subscription.getAmount()); // assuming Subscription has a price field

            // Create Flutterwave payment link
            String paymentLink = flutterwaveService.createPaymentLink(
                    user.getFirstName() + " " + user.getLastName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    amount,
                    "ZMW"
            );

            // Redirect to Flutterwave payment page
            return "redirect:" + paymentLink;

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to initiate payment: " + e.getMessage());
            return "redirect:/account/subscription";
        }
    }






}

