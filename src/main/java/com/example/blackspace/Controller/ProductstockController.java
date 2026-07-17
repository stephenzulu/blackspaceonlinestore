package com.example.blackspace.Controller;


import com.example.blackspace.MemberStoreSubscription.StoreSubscriptionEmailService;
import com.example.blackspace.Model.Category;
import com.example.blackspace.Model.Payment;
import com.example.blackspace.Model.Products.Productstock;
import com.example.blackspace.Model.Stores.Store;
import com.example.blackspace.Model.Subscription;
import com.example.blackspace.Model.User;
import com.example.blackspace.Repository.user.UserRepository;
import com.example.blackspace.Service.Category.CategoryService;
import com.example.blackspace.Service.Payment.PaymentService;
import com.example.blackspace.Service.Productstock.ProductstockService;
import com.example.blackspace.Service.Store.StoreService;
import com.example.blackspace.Service.Subscription.SubscriptionService;
import com.example.blackspace.Service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class ProductstockController {
    private final CategoryService categoryService;
    private final UserService userService;

    private final StoreService storeService;


    private final StoreSubscriptionEmailService emailService;


    @Autowired
    private PaymentService paymentService ;

    @Autowired
    private ProductstockService productstockService ;


    @Autowired
    private SubscriptionService subscriptionService ;



    @Autowired
    private UserRepository userRepository;

    public ProductstockController(CategoryService categoryService, UserService userService, StoreService storeService, StoreSubscriptionEmailService emailService) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.storeService = storeService;
        this.emailService = emailService;
    }

    // Store landing page (no products displayed initially)

    ///  view all productstock
    @GetMapping("/admin/products")
    public String manageusers(Model model, Principal principal,HttpSession session) {
        model.addAttribute("listproductstock",productstockService.getAllProductstock());
        // Get logged-in username from Spring Security
        // String username = principal.getName();
        // Fetch user from DB
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("category", new Category());
        model.addAttribute("listproductstock", productstockService.getAllProductstock());

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");

        // Get the user with username "boo1"
        User user = userService.findByUsername(username); // fetch User object
        if (user == null) {
            user = new User();           // create empty User
            user.setFirstName("User Null");  // default value to prevent template errors
        }

        model.addAttribute("user", user);

        return "/admin/manageproducts";
    }


    // Update publication with file uploads
    @PostMapping("/adminupdateProductstock")
    public String adminupdateProductstock(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("shortdescription") String shortdescription,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("price") String price,
            @RequestParam("currency") String currency,
            @RequestParam(value = "imageurl", required = false) MultipartFile[] newImages,
            @RequestParam(value = "audiourl", required = false) MultipartFile audiourl,
            @RequestParam(value = "existingImages", required = false) String existingImages,
            RedirectAttributes redirectAttributes) {

        try {
            Productstock existing = productstockService.getProductstockById(id);

            if (existing != null) {
                existing.setName(name);
                existing.setShortdescription(shortdescription);
                existing.setCategory(category);
                existing.setDescription(description);
                existing.setPrice(price);
                existing.setCurrency(currency);

                // Merge existing images with new ones
                String mergedImages = existingImages != null ? existingImages : "";
                if (newImages != null && newImages.length > 0) {
                    // The service method addProductstock/updateProductstock already handles saving files
                    // We just pass the MultipartFile[] along with the merged image string
                    existing.setImageurls(mergedImages);
                    productstockService.updateProductstock(existing, newImages, audiourl);
                } else {
                    existing.setImageurls(mergedImages);
                    productstockService.updateProductstock(existing, null, audiourl);
                }

                // ✅ GET STORE ID FROM PRODUCT
                Long storeId = Long.parseLong(existing.getStoreid());

                // ✅ SEND EMAIL TO SUBSCRIBERS
                String subject = "Product Updated!";
                String html = """
                <p>A Store you follow has just been updated a product.</p>
                <p><strong>%s</strong></p>
                """.formatted(existing.getName());

                emailService.sendEmailToStoreSubscribers(storeId, subject, html);


                redirectAttributes.addFlashAttribute("successMessage", "Amended Successfully!");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", "Product not found!");
            }

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("successMessage", "Failed to update!");
        }

        return "redirect:/admin/products";
    }




    @PostMapping("/updateuserProductstock")
    public String updateuserProductstock(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("shortdescription") String shortdescription,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("price") String price,
            @RequestParam("currency") String currency,
            @RequestParam(value = "imageurl", required = false) MultipartFile[] newImages,
            @RequestParam(value = "audiourl", required = false) MultipartFile audiourl,
            @RequestParam(value = "existingImages", required = false) String existingImages,
            RedirectAttributes redirectAttributes) {

        try {
            Productstock existing = productstockService.getProductstockById(id);

            if (existing == null) {
                redirectAttributes.addFlashAttribute("error", "Product not found!");
                return "redirect:/account/products";
            }

            // Update fields
            existing.setName(name);
            existing.setShortdescription(shortdescription);
            existing.setCategory(category);
            existing.setDescription(description);
            existing.setPrice(price);
            existing.setCurrency(currency);

            // Images
            existing.setImageurls(existingImages != null ? existingImages : "");
            productstockService.updateProductstock(existing, newImages, audiourl);

            redirectAttributes.addFlashAttribute("successMessage", "Amended Successfully!");

            // ✅ GET STORE ID FROM PRODUCT
            Long storeId = Long.parseLong(existing.getStoreid());

            // ✅ SEND EMAIL TO SUBSCRIBERS
            String subject = "Product Updated!";
            String html = """
                <p>A Store you follow has just been updated a product.</p>
                <p><strong>%s</strong></p>
                """.formatted(existing.getName());

            emailService.sendEmailToStoreSubscribers(storeId, subject, html);

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to update!");
        }

        return "redirect:/account/products";
    }




    @DeleteMapping("/deleteProduct/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productstockService.deleteProduct(id);
            return ResponseEntity.ok("Deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting product: " + e.getMessage());
        }
    }


    //  Handle Form Submission




    @PostMapping("/adminaddProductstock")
    public String adminaddProductstock(
            @RequestParam("name") String name,
            @RequestParam("shortdescription") String shortdescription,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("price") String price,
            @RequestParam("currency") String currency,
            @RequestParam(value = "imageurl", required = false) MultipartFile[] imagefiles,
            @RequestParam(value = "audiourl", required = false) MultipartFile audiofile,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        try {
            Productstock productstock = new Productstock();
            productstock.setName(name);
            productstock.setShortdescription(shortdescription);
            productstock.setDescription(description);
            productstock.setCategory(category);
            productstock.setPrice(price);
            productstock.setCurrency(currency);


            productstockService.addProductstock(productstock, imagefiles, audiofile);

            redirectAttributes.addFlashAttribute("success", "Product added successfully!");

            String username = (String) session.getAttribute("LOGGED_IN_USERNAME");
            // ✅ Get store
            Store store = storeService.getStoreByUsername(username);
            // ✅ SEND EMAIL TO SUBSCRIBERS
            Long storeId = store.getId();

            String subject = "New Product Added!";
            String html = """
                <p>The store <strong>%s</strong> has added a new product.</p>
                <p><strong>%s</strong></p>
                <p>Visit BlackSpace to view it.</p>
                """.formatted(store.getName(), name);

            emailService.sendEmailToStoreSubscribers(storeId, subject, html);


        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to upload files.");
        }

        return "redirect:/store/products";
    }



    private long countUsedProducts(
            Payment activePayment,
            List<Productstock> userProducts
    ) {
        LocalDateTime subscriptionStart = activePayment.getCreatedtime();

        return userProducts.stream()
                .filter(p -> p.getCreatedAt() != null)
                .filter(p -> !p.getCreatedAt().isBefore(subscriptionStart))
                .count();
    }

    private boolean canAddMoreProducts(
            Payment activePayment,
            Subscription subscription,
            List<Productstock> userProducts
    ) {
        int maxProducts = Integer.parseInt(subscription.getNumberofproducts());
        long usedProducts = countUsedProducts(activePayment, userProducts);

        return usedProducts < maxProducts;
    }

    private int getMaxProducts(Subscription subscription) {
        try {
            return Integer.parseInt(subscription.getNumberofproducts());
        } catch (NumberFormatException e) {
            return 0;
        }
    }



    @PostMapping("/addUserProduct")
    public String addUserProduct(
            @RequestParam("name") String name,
            @RequestParam("shortdescription") String shortdescription,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("price") String price,
            @RequestParam("currency") String currency,
            @RequestParam(value = "imageurl", required = false) MultipartFile[] imagefiles,
            @RequestParam(value = "audiourl", required = false) MultipartFile audiofile,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        try {
            String username = (String) session.getAttribute("LOGGED_IN_USERNAME");
            if (username == null) {
                redirectAttributes.addFlashAttribute("error", "Session expired.");
                return "redirect:/login";
            }

            Store store = storeService.getStoreByUsername(username);
            if (store == null) {
                redirectAttributes.addFlashAttribute("error", "Store not found.");
                return "redirect:/account/products";
            }

            // ✅ ACTIVE PAYMENT
            Payment activePayment = paymentService.getActiveSubscription(username);
            if (activePayment == null) {
                redirectAttributes.addFlashAttribute("error", "No active subscription.");
                return "redirect:/account/products";
            }

            // ✅ SUBSCRIPTION
            Subscription subscription = subscriptionService
                    .getSubscriptionById(Long.valueOf(activePayment.getSubscriptionid()));

            int maxProducts = getMaxProducts(subscription);

            List<Productstock> userProducts =
                    productstockService.getProductsByUsername(username);

            long usedProducts = countUsedProducts(activePayment, userProducts);
            long remainingProducts = maxProducts - usedProducts;

            // ❌ LIMIT REACHED
            if (remainingProducts <= 0) {
                redirectAttributes.addFlashAttribute(
                        "error",
                        "Product limit reached. Your subscription allows "
                                + maxProducts + " products."
                );
                return "redirect:/account/products";
            }

            // ✅ CREATE PRODUCT
            Productstock productstock = new Productstock();
            productstock.setName(name);
            productstock.setShortdescription(shortdescription);
            productstock.setDescription(description);
            productstock.setCategory(category);
            productstock.setPrice(price);
            productstock.setCurrency(currency);
            productstock.setUsername(username);
            productstock.setStoreid(String.valueOf(store.getId()));

            productstockService.addProductstock(productstock, imagefiles, audiofile);

            // ✅ USER FEEDBACK (AFTER SAVE)
            if (remainingProducts == 1) {
                redirectAttributes.addFlashAttribute(
                        "warning",
                        "Product added. You have reached your product limit. Upgrade your subscription to add more products."
                );
            } else {
                redirectAttributes.addFlashAttribute(
                        "success",
                        "Product added successfully! You have "
                                + (remainingProducts - 1) + " product slots remaining."
                );
            }

            // ✅ EMAIL SUBSCRIBERS
            String subject = "New Product Added!";
            String html = """
            <p>The store <strong>%s</strong> has added a new product.</p>
            <p><strong>%s</strong></p>
            """.formatted(store.getName(), name);

            emailService.sendEmailToStoreSubscribers(store.getId(), subject, html);

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "File upload failed.");
        }

        return "redirect:/account/products";
    }



    @GetMapping("/products")
    public String allProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        int pageSize = 12;

        // Filter + sort
        List<Productstock> filtered = productstockService.getProductsFromActiveStores().stream()
                .filter(p -> name == null || name.isEmpty()
                        || p.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(p -> category == null || category.isEmpty()
                        || (p.getCategory() != null && p.getCategory().equalsIgnoreCase(category)))
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .toList();

        int totalProducts = filtered.size();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalProducts);

        List<Productstock> products =
                fromIndex >= totalProducts ? List.of() : filtered.subList(fromIndex, toIndex);

        // Categories
        List<String> categories = productstockService.getProductsFromActiveStores().stream()
                .map(Productstock::getCategory)
                .filter(c -> c != null && !c.isEmpty())
                .distinct()
                .sorted()
                .toList();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("name", name);
        model.addAttribute("category", category);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        // Top stores for the products page
        List<Store> topStores = storeService.getActiveStores().stream()
                .limit(4)
                .toList();
        model.addAttribute("topStores", topStores);

        return "all-products";
    }





    /// view all products
@GetMapping("/account/products")
public String users(Model model, Principal principal,HttpSession session) {

    String username = (String) session.getAttribute("LOGGED_IN_USERNAME");


    // --- Fetch the logged-in user ---
    User user = userService.findByUsername(username);
    if (user == null) {
        user = new User();
        user.setFirstName("User Null"); // default to prevent template errors
    }
    model.addAttribute("user", user);

    // --- Fetch products only for this user ---
   // String username = "1001"; // or user.getId() if stored in User entity
    model.addAttribute("listproducts", productstockService.getProductsByUsername(username));

    // --- Fetch categories ---
    model.addAttribute("categories", categoryService.getAllCategories());
    model.addAttribute("category", new Category());


    // --- PAYMENT-BASED ADD BUTTON VISIBILITY ---
    boolean showAddProductButton = true;

    Payment activePayment = paymentService.getActiveSubscription(username);
    if (activePayment == null) {
        showAddProductButton = false;
    } else {
        Subscription subscription =
                subscriptionService.getSubscriptionById(
                        Long.valueOf(activePayment.getSubscriptionid()));

        int maxProducts = getMaxProducts(subscription);
        List<Productstock> userProducts =
                productstockService.getProductsByUsername(username);

        long usedProducts = countUsedProducts(activePayment, userProducts);

        model.addAttribute("usedProducts", usedProducts);
        model.addAttribute("maxProducts", maxProducts);
        model.addAttribute("remainingProducts", maxProducts - usedProducts);

        showAddProductButton = usedProducts < maxProducts;
    }
    model.addAttribute("showAddProductButton", showAddProductButton);



    return "/users/products"; // Thymeleaf template
}


    @GetMapping("/products/search")
    public String searchProducts(@RequestParam(value = "q", required = false) String query,
                                 Model model) {

        // 1 Fetch search results
        List<Productstock> results = productstockService.searchProducts(query);

        // 2️ Map each product to include only the first image
        List<Productstock> productsWithFirstImage = results.stream().map(p -> {
            if (p.getImageurls() != null && !p.getImageurls().isEmpty()) {
                // Split and keep only the first image
                String firstImage = p.getImageurls().split(",")[0];
                p.setImageurl(firstImage); // set it to a new field 'imageurl'
            } else {
                p.setImageurl("default.png"); // fallback image
            }
            return p;
        }).toList();

        // 3️ Add attributes to the model
        model.addAttribute("products", productsWithFirstImage);
        model.addAttribute("q", query);

        return "search-results"; // Thymeleaf template
    }

    @GetMapping("/api/products/suggest")
    @ResponseBody
    public List<Map<String, Object>> suggestProducts(@RequestParam(value = "q", required = false) String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        List<Productstock> results = productstockService.searchProducts(query);
        return results.stream().limit(8).map(p -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", p.getId());
            item.put("name", p.getName());
            item.put("price", p.getPrice());
            item.put("currency", p.getCurrency());
            String image = "default.png";
            if (p.getImageurls() != null && !p.getImageurls().isEmpty()) {
                image = p.getImageurls().split(",")[0];
            }
            item.put("image", "/uploads/products/images/" + image);
            return item;
        }).toList();
    }


    // VIEW SINGLE PRODUCT
    // =========================
    @GetMapping("/product/{id}")
    public String viewProduct(@PathVariable Long id, Model model, HttpSession session, HttpServletRequest request) {

        Productstock product = productstockService.findById(id);

        // storeid is STRING (e.g. BC_02) → NO parsing
        Store store = storeService.getStoreByid(Long.valueOf((product.getStoreid())));

        System.out.println("storeidnumber"+product.getStoreid());

        // Keep a Set of product IDs viewed in this session
        @SuppressWarnings("unchecked")
        Set<Long> viewedProducts = (Set<Long>) session.getAttribute("VIEWED_PRODUCTS");
        if (viewedProducts == null) {
            viewedProducts = new HashSet<>();
        }

        // Increment views only if not viewed in this session
        if (!viewedProducts.contains(id)) {
            int currentViews = product.getViews() != null ? product.getViews() : 0;
            product.setViews(currentViews + 1);
            productstockService.saveProduct(product);

            viewedProducts.add(id);
            session.setAttribute("VIEWED_PRODUCTS", viewedProducts);
        }

        model.addAttribute("product", product);
        model.addAttribute("store", store);
        model.addAttribute("currentUrl", request.getRequestURL().toString()); // Pass URL

        //  Similar products (same category)
        List<Productstock> similarProducts =
                productstockService.findSimilarProducts(
                        product.getCategory(),
                        product.getId(),
                        8
                );

        model.addAttribute("similarProducts", similarProducts);



        return "viewproduct"; // viewproduct.html
    }






    @PostMapping("/product/{id}/rate")
    @ResponseBody
    public ResponseEntity<?> rateProduct(
            @PathVariable Long id,
            @RequestParam int stars) {

        if (stars < 1 || stars > 5) {
            return ResponseEntity.badRequest().body("Invalid rating");
        }

        Productstock product = productstockService.findById(id);

        // ✅ NULL SAFE INITIALIZATION
        int total = product.getRatingTotal() != null ? product.getRatingTotal() : 0;
        int count = product.getRatingCount() != null ? product.getRatingCount() : 0;

        product.setRatingTotal(total + stars);
        product.setRatingCount(count + 1);

        productstockService.saveProduct(product);

        Map<String, Object> response = new HashMap<>();
        response.put("average", product.getAverageRating());
        response.put("count", product.getRatingCount());

        return ResponseEntity.ok(response);
    }
















































    @PostMapping("/product/{id}/like")
    @ResponseBody
    public Map<String, Integer> likeProduct(@PathVariable Long id, HttpSession session) {

        Productstock product = productstockService.findById(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Set<Long> liked = (Set<Long>) session.getAttribute("LIKED_PRODUCTS");
        if (liked == null) liked = new HashSet<>();

        Set<Long> disliked = (Set<Long>) session.getAttribute("DISLIKED_PRODUCTS");
        if (disliked == null) disliked = new HashSet<>();

        if (!liked.contains(id)) {
            if (disliked.contains(id)) {
                product.setDislikes(product.getDislikes() - 1);
                disliked.remove(id);
            }

            product.setLikes(product.getLikes() + 1);
            liked.add(id);
            productstockService.saveProduct(product);
        }

        session.setAttribute("LIKED_PRODUCTS", liked);
        session.setAttribute("DISLIKED_PRODUCTS", disliked);

        return Map.of(
                "likes", product.getLikes(),
                "dislikes", product.getDislikes()
        );
    }


    @PostMapping("/product/{id}/dislike")
    @ResponseBody
    public Map<String, Integer> dislikeProduct(@PathVariable Long id, HttpSession session) {

        Productstock product = productstockService.findById(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Set<Long> liked = (Set<Long>) session.getAttribute("LIKED_PRODUCTS");
        if (liked == null) liked = new HashSet<>();

        Set<Long> disliked = (Set<Long>) session.getAttribute("DISLIKED_PRODUCTS");
        if (disliked == null) disliked = new HashSet<>();

        if (!disliked.contains(id)) {
            if (liked.contains(id)) {
                product.setLikes(product.getLikes() - 1);
                liked.remove(id);
            }

            product.setDislikes(product.getDislikes() + 1);
            disliked.add(id);
            productstockService.saveProduct(product);
        }

        session.setAttribute("LIKED_PRODUCTS", liked);
        session.setAttribute("DISLIKED_PRODUCTS", disliked);

        return Map.of(
                "likes", product.getLikes(),
                "dislikes", product.getDislikes()
        );
    }














}
