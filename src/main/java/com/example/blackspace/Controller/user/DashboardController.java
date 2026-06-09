package com.example.blackspace.Controller.user;


import com.example.blackspace.Model.Payment;
import com.example.blackspace.Model.Stores.Store;
import com.example.blackspace.Model.User;
import com.example.blackspace.Service.PageVisit.PageVisitService;
import com.example.blackspace.Service.Payment.PaymentService;
import com.example.blackspace.Service.Productstock.ProductstockService;
import com.example.blackspace.Service.Store.StoreService;
import com.example.blackspace.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class DashboardController {
    @Autowired
    private StoreService storeService;

    private final UserService userService;
    private final ProductstockService productstockService;
    private final PaymentService paymentService;
    private final PageVisitService pageVisitService;

    public DashboardController(UserService userService, ProductstockService productstockService,
                               PaymentService paymentService, PageVisitService pageVisitService) {
        this.userService = userService;
        this.productstockService = productstockService;
        this.paymentService = paymentService;
        this.pageVisitService = pageVisitService;
    }




    // Display profile by username
    @GetMapping("/help")
    public String help( Model model) {
        return "help"; // managedashboard.html

    }
        // Display profile by username
    @GetMapping("/admin/dashboard")
    public String profileByUsername(Model model, HttpSession session) {


        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");

        // Get the user with username "boo1"
        User user = userService.findByUsername(username); // fetch User object
        if (user == null) {
            user = new User();           // create empty User
            user.setFirstName("User Null");  // default value to prevent template errors
        }

        model.addAttribute("user", user);


        // USER ID FIXED AS REQUESTED
       // String username = "1001";


        // Total products (ALL)
        long productCount = productstockService.countAllProducts();
        model.addAttribute("productCount", productCount);

        // Products per category (ALL)
        Map<String, Long> categoryData =
                productstockService.countProductsByCategory();

        model.addAttribute("categoryLabels", categoryData.keySet());
        model.addAttribute("categoryValues", categoryData.values());

        // Page visit data for chart
        model.addAttribute("visitDates", pageVisitService.getVisitDatesLast7Days());
        model.addAttribute("visitCounts", pageVisitService.getVisitCountsLast7Days());

        // Top pages by visits
        Map<String, Long> topPages = pageVisitService.getTopPagesByVisits();
        model.addAttribute("topPages", topPages);

        // Stats for dashboard cards
        long userCount = userService.getAllUsers().size();
        long storeCount = storeService.getAllStores().size();
        model.addAttribute("userCount", userCount);
        model.addAttribute("storeCount", storeCount);

        // Browser & device stats
        Map<String, Long> browserStats = pageVisitService.getBrowserStats();
        Map<String, Long> deviceStats = pageVisitService.getDeviceStats();
        model.addAttribute("browserLabels", browserStats.keySet());
        model.addAttribute("browserValues", browserStats.values());
        model.addAttribute("deviceLabels", deviceStats.keySet());
        model.addAttribute("deviceValues", deviceStats.values());

        // Recent visits log
        model.addAttribute("recentVisits", pageVisitService.getRecentVisits());

        return "admin/managedashboard"; // managedashboard.html
    }


    // Display profile by username
    @GetMapping("/account/dashboard")
    public String Dashboard_user( Model model,HttpSession session) {


        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");

        Store store = storeService.getStoreByUsername(username);
        model.addAttribute("store", store);


        // Get the user with username "boo1"
        User user = userService.findByUsername(username); // fetch User object
        if (user == null) {
            user = new User();           // create empty User
            user.setFirstName("User Null");  // default value to prevent template errors
        }

        model.addAttribute("user", user);


        // USER ID FIXED AS REQUESTED

        // Total products
        long productCount = productstockService.countByUsername(username);
        model.addAttribute("productuserChart", productCount);

        // Products per category
        Map<String, Long> categoryData =
                productstockService.countProductsByUsername(username);

        model.addAttribute("categoryLabels", categoryData.keySet());
        model.addAttribute("categoryValues", categoryData.values());





        Optional<Payment> paymentOpt = paymentService.getLatestPayment(username);
        boolean subscriptionActive = paymentService.isSubscriptionEnabled(username);

        // DEBUG (watch console)
        System.out.println("Payment found: " + paymentOpt.isPresent());
        paymentOpt.ifPresent(p ->
                System.out.println("Enabled=" + p.getEnabled() + ", Type=" + p.getPaymentType())
        );






        model.addAttribute("subscriptionActive", subscriptionActive); // primitive boolean
        model.addAttribute("subscription", paymentOpt.orElse(null));
        model.addAttribute("username", username);

        return "users/dashboard"; // managedashboard.html
    }



}

