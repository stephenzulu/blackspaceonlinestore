package com.example.blackspace.Controller;

import com.example.blackspace.Model.Category;
import com.example.blackspace.Model.User;
import com.example.blackspace.Repository.CategoryRepository;
import com.example.blackspace.Service.Category.CategoryService;
import com.example.blackspace.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Controller
public class AllCategoriesController {
    private final UserService userService;

    private final CategoryService categoryService;



    public AllCategoriesController(UserService userService, CategoryService categoryService ) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    // View categories page
    @GetMapping("/admin/categories")
    public String viewCategories(Model model, HttpSession session) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("category", new Category());

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");


        // Get the user with username "boo1"
        User user = userService.findByUsername(username); // fetch User object
        if (user == null) {
            user = new User();           // create empty User
            user.setFirstName("User Null");  // default value to prevent template errors
        }

        model.addAttribute("user", user);


        return "/admin/categories";
    }





    // Add category
    @PostMapping("/admin/categories/add")
    public String addCategory(
            @RequestParam("name") String name,
            @RequestParam("catimages") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        try {
            Category category = new Category();
            category.setName(name);

            if (file != null && !file.isEmpty()) {
                String uploadDir = System.getProperty("user.dir") + "/uploads/categories";
                Path uploadPath = Paths.get(uploadDir);


                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                file.transferTo(uploadPath.resolve(filename).toFile());

                category.setCatimages(filename);
            }

            categoryService.saveCategory(category);
            redirectAttributes.addFlashAttribute("success", "Category added successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to upload image!");
        }

        return "redirect:/admin/categories";
    }



    @PostMapping("/admin/categories/update")
    public String updateCategory(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "catimages", required = false) MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Category category = categoryService.getAllCategories()
                    .stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            category.setName(name);

            // Handle file upload
            if (file != null && !file.isEmpty()) {

                // ✅ Use absolute path to your project folder
                String uploadDir = System.getProperty("user.dir") + "/uploads/categories";

                Path uploadPath = Paths.get(uploadDir);

                // Create folders if they don't exist
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Save file with timestamp prefix
                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);

                file.transferTo(filePath.toFile()); // Save file

                // Save filename to database
                category.setCatimages(filename);
            }

            categoryService.updateCategory(category);
            redirectAttributes.addFlashAttribute("success", "Category updated successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to update category!");
        }

        return "redirect:/admin/categories";
    }



    // Delete category
    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/all")
    public String gettAllCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("category", new Category());
        return "productstock"; // this matches your Thymeleaf template
    }





}

