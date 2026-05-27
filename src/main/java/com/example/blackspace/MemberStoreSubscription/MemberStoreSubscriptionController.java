package com.example.blackspace.MemberStoreSubscription;

import com.example.blackspace.Model.Stores.Store;
import com.example.blackspace.SMS.EmailService;
import com.example.blackspace.Service.Store.StoreService;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subscription")
public class MemberStoreSubscriptionController {

    private final MemberStoreSubscriptionService subscriptionService;
    private final StoreService storeService;

    private final EmailService emailService;

    public MemberStoreSubscriptionController(MemberStoreSubscriptionService subscriptionService,
                                             StoreService storeService, EmailService emailService) {
        this.subscriptionService = subscriptionService;
        this.storeService = storeService;
        this.emailService = emailService;
    }

    @PostMapping("/subscribe/{id}")
    public String subscribeToStore(@PathVariable Long id,
                                   @RequestParam("email") String email,
                                   Model model) {

        Store store = storeService.getStoreById(id);

        if (store != null) {
            subscriptionService.subscribe(email, store);
            String html = "<h2> Successfully subscribed to  " + store.getName() + "!</h2>"
                    + "<p>You will be the first to know when new products are uploaded, special offers are added, or prices change.</p>";

            try {
                emailService.sendHtmlEmail(email, "Successfully subscribed to " + store.getName() +"", html);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }


            model.addAttribute("message", "Successfully subscribed to " + store.getName());
        } else {
            model.addAttribute("error", "Store not found");
        }

        return "redirect:/store/" + store.getId(); // redirect back to store page
    }
}
