package com.example.blackspace.Controller;

import com.example.blackspace.Model.Products.Productstock;
import com.example.blackspace.Model.Stores.Store;
import com.example.blackspace.Model.User;
import com.example.blackspace.Service.Productstock.ProductstockService;
import com.example.blackspace.Service.Store.StoreService;
import com.example.blackspace.Service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Controller
public class StoreController {

    private final UserService userService;

    private final StoreService storeService;

    private final ProductstockService productstockService;

    public StoreController(UserService userService, StoreService storeService, ProductstockService productstockService) {
        this.userService = userService;
        this.storeService = storeService;
        this.productstockService = productstockService;
    }

    // =========================



    // =========================
    // VIEW STORE & PRODUCTS
    // =========================
    @GetMapping("/store")
    public String userviewStores(Model model) {

        List<Store> stores = storeService.getAllStores();

        // Optional fallback if no stores exist
        if (stores == null || stores.isEmpty()) {
            Store defaultStore = new Store();
            defaultStore.setName("Black Space Store");
            defaultStore.setDescription("Welcome to our store");
            defaultStore.setStorebanner("/store/banner/banner.jpeg");
            defaultStore.setStorelogo("/store/logo/logo.jpeg");

            stores = List.of(defaultStore);
        }

        model.addAttribute("store", stores);

        return "store"; // store.html
    }


    @GetMapping({"/store/{id}"})
    public String searchStore(@PathVariable(required = false) String id, Model model) {

        Store store = storeService.getStoreByid(Long.valueOf(id));

        if (store == null) {
            store = new Store();
            store.setName("Black Space Store");
            store.setDescription("Welcome to our store");
            store.setStorebanner("/store/banner/banner.jpeg");
            store.setStorelogo("/store/logo/logo.jpeg");
        }

        List<Productstock> products = storeService.getProductsByStore(id);

        model.addAttribute("store", store);
        model.addAttribute("products", products);

        return "store"; // store.html
    }


    @GetMapping("/stores")
    public String viewAllStores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String q,
            Model model) {

        int pageSize = 12;

        // 1️⃣ Get all stores
        List<Store> allStores = storeService.getAllStores();

        // 2️⃣ Apply search filter (if provided)
        if (q != null && !q.trim().isEmpty()) {
            String search = q.toLowerCase();
            allStores = allStores.stream()
                    .filter(store -> store.getName() != null &&
                            store.getName().toLowerCase().contains(search))
                    .toList();
        }

        // 3️⃣ Pagination logic
        int totalStores = allStores.size();
        int totalPages = (int) Math.ceil((double) totalStores / pageSize);

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalStores);

        List<Store> stores =
                fromIndex >= totalStores ? List.of() : allStores.subList(fromIndex, toIndex);

        // 4️⃣ Model attributes
        model.addAttribute("stores", stores);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("q", q); // keep search value

        // Top products for the stores page
        List<Productstock> topProducts = productstockService.getAllProductstock().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getViews(), p1.getViews()))
                .limit(4)
                .toList();
        model.addAttribute("topProducts", topProducts);

        return "stores";
    }






    @GetMapping("/admin/stores")
    public String manageStores(Model model,HttpSession session) {
        // Get all stores
        model.addAttribute("stores", storeService.getAllStores());

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");

        // Get the user with username "boo1"
        User user = userService.findByUsername(username); // fetch User object
        if (user == null) {
            user = new User();           // create empty User
            user.setFirstName("User Null");  // default value to prevent template errors
        }
        model.addAttribute("user", user); // pass to Thymeleaf

        return "admin/managestores";
    }



    @PostMapping("/admin/stores/add")
    public String addStore(
            @ModelAttribute Store store,
            @RequestParam("logoFile") MultipartFile logoFile,
            @RequestParam("bannerFile") MultipartFile bannerFile
    ) {

        try {
            if (!logoFile.isEmpty()) {
                String logoPath = storeService.saveImage(logoFile);
                store.setStorelogo(logoPath);
            }

            if (!bannerFile.isEmpty()) {
                String bannerPath = storeService.saveImage(bannerFile);
                store.setStorebanner(bannerPath);
            }

            storeService.saveStore(store);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/stores";
    }






    @PostMapping("/admin/stores/update")
    public String updateStore(
            @ModelAttribute Store store,
            @RequestParam(value = "logoFile", required = false) MultipartFile logoFile,
            @RequestParam(value = "bannerFile", required = false) MultipartFile bannerFile
    ) {

        Store existingStore = storeService.getStoreById(store.getId());

        if (existingStore != null) {

            existingStore.setName(store.getName());
            existingStore.setEmail(store.getEmail());
            existingStore.setContactNumber(store.getContactNumber());
            existingStore.setWhatsappNumber(store.getWhatsappNumber());
            existingStore.setDescription(store.getDescription());
            existingStore.setActive(store.getActive());

            // ✅ Update logo ONLY if new file uploaded
            if (logoFile != null && !logoFile.isEmpty()) {
                String logoName = storeService.saveImage(logoFile);
                existingStore.setStorelogo(logoName);
            }

            // ✅ Update banner ONLY if new file uploaded
            if (bannerFile != null && !bannerFile.isEmpty()) {
                String bannerName = storeService.saveImage(bannerFile);
                existingStore.setStorebanner(bannerName);
            }

            storeService.saveStore(existingStore);
        }

        return "redirect:/admin/stores";
    }






    @PostMapping("/user/stores/add")
    public String useraddStore(@ModelAttribute Store store) {
        storeService.saveStore(store);  // make sure your service can save the store
        return "redirect:/user/stores"; // redirect back to manage page

    }




    @PostMapping("/store/create")
    public String usersaveStore(
            @ModelAttribute Store store,
            @RequestParam("logoFile") MultipartFile logoFile,
            @RequestParam("bannerFile") MultipartFile bannerFile, HttpSession session
    ) {

        try {

            // Set username from logged-in user
            //String username = principal.getName();
            String username = (String) session.getAttribute("LOGGED_IN_USERNAME");
           // String username="sz4";
            store.setUsername(username);

            // Generate a unique store ID (you can customize format)
            LocalDate today = LocalDate.now();
            String datePart = today.format(DateTimeFormatter.ofPattern("yyyyMMdd_")); // e.g., 20251225
            String uniquePart = (System.currentTimeMillis() % 100000) + String.format("%02d", new Random().nextInt(100));
            String storeId = "STORE"+datePart + uniquePart; // STORE20251225 1701234567
            store.setStoreid(storeId);

            if (!logoFile.isEmpty()) {
                String logoPath = storeService.saveImage(logoFile);
                store.setStorelogo(logoPath);
            }

            if (!bannerFile.isEmpty()) {
                String bannerPath = storeService.saveImage(bannerFile);
                store.setStorebanner(bannerPath);
            }

            storeService.saveStore(store);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/account/dashboard";
    }




    @PostMapping("/store/update")
    public String userupdateStore(
            @RequestParam("id") Long id,
            @RequestParam("email") String email,
            @RequestParam("contactNumber") String contact,
            @RequestParam("whatsappNumber") String whatsapp,
            @RequestParam("description") String description,
            @RequestParam(value = "storelogo", required = false) MultipartFile logoFile,
            @RequestParam(value = "storebanner", required = false) MultipartFile bannerFile
    ) {
        Store existing = storeService.getStoreById(id);
        if (existing != null) {
            existing.setEmail(email);
            existing.setContactNumber(contact);
            existing.setWhatsappNumber(whatsapp);
            existing.setDescription(description);

            if (logoFile != null && !logoFile.isEmpty()) {
                existing.setStorelogo(storeService.saveImage(logoFile));
            }
            if (bannerFile != null && !bannerFile.isEmpty()) {
                existing.setStorebanner(storeService.saveImage(bannerFile));
            }

            storeService.saveStore(existing);
        }
        return "redirect:/account/dashboard";
    }








}
