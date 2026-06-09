package com.example.blackspace.Controller;

import com.example.blackspace.Model.Category;
import com.example.blackspace.Model.Products.Productstock;
import com.example.blackspace.Model.Stores.Store;
import com.example.blackspace.Model.User;
import com.example.blackspace.Service.Category.CategoryService;
import com.example.blackspace.Service.Productstock.ProductstockService;
import com.example.blackspace.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CategoryController {
    private final UserService userService;

    private final ProductstockService productstockService;

    private final CategoryService categoryService;

    public CategoryController(UserService userService, ProductstockService productstockService, CategoryService categoryService) {
        this.userService = userService;
        this.productstockService = productstockService;
        this.categoryService = categoryService;
    }

    @GetMapping("/category/{categoryName}")
    public String productsByCategory(@PathVariable("categoryName") String categoryName, Model model, HttpSession session) {

        List<Productstock> products = productstockService.getAllProductstock().stream()
                .filter(p -> p.getCategory() != null && p.getCategory().equalsIgnoreCase(categoryName))
                .collect(Collectors.toList());

        model.addAttribute("categoryName", categoryName);
        model.addAttribute("products", products);

        // Get category image for banner
        Category category = categoryService.getAllCategories().stream()
                .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                .findFirst().orElse(null);
        String categoryImage = (category != null && category.getCatimages() != null)
                ? "/uploads/categories/" + category.getCatimages()
                : "/store/banner/banner.jpeg";
        model.addAttribute("categoryImage", categoryImage);

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");

        // Get the user with username "boo1"
        User user = userService.findByUsername(username); // fetch User object
        if (user == null) {
            user = new User();           // create empty User
            user.setFirstName("User Null");  // default value to prevent template errors
        }

        model.addAttribute("user", user);


        return "search_category"; // points to search_category.html
    }



    @GetMapping("/categories")
    public String viewAllCategories(Model model) {

        // Assuming you have a CategoryService that returns all categories
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("categories", categories);

        // Map category name -> image path
        Map<String, String> categoryImages = categories.stream()
                .collect(Collectors.toMap(
                        Category::getName,
                        c -> "/uploads/categories/" + c.getCatimages() // catimages stores the filename
                ));

        model.addAttribute("categoryImages", categoryImages);

        return "categories";
    }








}
