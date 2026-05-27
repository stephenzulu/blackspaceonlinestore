package com.example.blackspace.Controller;

import com.example.blackspace.Model.Products.Productstock;

import com.example.blackspace.Model.Stores.Store;
import com.example.blackspace.Model.User;
import com.example.blackspace.Service.Productstock.ProductstockService;
import com.example.blackspace.Service.Store.StoreService;
import com.example.blackspace.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {
    private final UserService userService;

    private final ProductstockService productstockService;
    private final StoreService storeService;

    public HomeController(UserService userService, ProductstockService productstockService,
                          StoreService storeService) {
        this.userService = userService;
        this.productstockService = productstockService;
        this.storeService = storeService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {

        List<Productstock> products = productstockService.getAllProductstock().stream()
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt())) // newest first
                .limit(8) // top 8 latest products
                .toList();

        // 🔹 TOP 4 DISTINCT CATEGORIES
        List<String> categories = products.stream()
                .map(Productstock::getCategory)
                .filter(c -> c != null && !c.isEmpty())
                .distinct()
                .limit(4)
                .toList();

        // 🔹 LATEST STORES (top 6)
        List<Store> latestStores = storeService.getAllStores().stream()
                .sorted(Comparator.comparing(Store::getCreatedAt).reversed())
                .limit(6)
                .toList();

        // 🔹 TOP 8 PRODUCTS BY LIKES
        List<Productstock> topProductsByLikes = products.stream()
                .sorted((p1, p2) -> {
                    int likes1 = 0;
                    int likes2 = 0;
                    try {
                        likes1 = p1.getLikes();
                    } catch (NumberFormatException ignored) {}
                    try {
                        likes2 = p2.getLikes();
                    } catch (NumberFormatException ignored) {}
                    return Integer.compare(likes2, likes1); // descending
                })
                .limit(8)
                .toList();

        model.addAttribute("topcategories", categories);

        model.addAttribute("products", products);
        model.addAttribute("latestStores", latestStores);
        model.addAttribute("topProducts", topProductsByLikes);

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");

        // Get the user with username "boo1"
        User user = userService.findByUsername(username); // fetch User object
        if (user == null) {
            user = new User();           // create empty User
            user.setFirstName("User Null");  // default value to prevent template errors
        }

        model.addAttribute("user", user);


        return "index";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "about"; // loads about.html from templates
    }


}
