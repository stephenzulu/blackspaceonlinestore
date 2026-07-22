package com.example.blackspace.Controller;


import com.example.blackspace.Lencopayment.LencoProperties;
import com.example.blackspace.Model.Subscription;
import com.example.blackspace.Model.User;
import com.example.blackspace.Service.Payment.PaymentService;
import com.example.blackspace.Service.Store.StoreService;
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

    private final StoreService storeService;

    private final SubscriptionService subscriptionService;

    private final LencoProperties lencoProperties;

    public SubscriptionController(UserService userService, PaymentService paymentService, StoreService storeService, SubscriptionService subscriptionService, LencoProperties lencoProperties) {
        this.userService = userService;
        this.paymentService = paymentService;
        this.storeService = storeService;
        this.subscriptionService = subscriptionService;
        this.lencoProperties = lencoProperties;
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
    public String useraddSubscription(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");

        // Check if user has set up a store first
        if (storeService.getStoreByUsername(username) == null) {
            redirectAttributes.addFlashAttribute("error", "Please set up your store before purchasing a subscription.");
            return "redirect:/account/dashboard";
        }

        model.addAttribute("subscriptions", subscriptionService.getAllSubscription());
        model.addAttribute("subscription", new Subscription());

        User user = userService.findByUsername(username);
        if (user == null) {
            user = new User();           // create empty User
            user.setFirstName("User Null");  // default value to prevent template errors
        }

        model.addAttribute("user", user);




        boolean subscriptionActive = paymentService.isSubscriptionEnabled(username);
        model.addAttribute("subscriptionActive", subscriptionActive);
        model.addAttribute("subscriptions", subscriptionService.getAllSubscription());
        model.addAttribute("username", username);

        // Lenco payment config for the inline widget
        model.addAttribute("lencoPublicKey", lencoProperties.getPublicKey());
        model.addAttribute("lencoInlineJsUrl", lencoProperties.getInlineJsUrl());
        model.addAttribute("lencoCurrency", lencoProperties.getCurrency());

        return "users/subscription";
    }









}

