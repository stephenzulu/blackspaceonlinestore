package com.example.blackspace.Controller.user;


import com.example.blackspace.Model.User;
import com.example.blackspace.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // View all users
    @GetMapping
    public String viewUsers(Model model, HttpSession session) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", new User());

        // Get the username of logged-in user
       // String username = principal.getName();

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");

        // Get the user with username "boo1"
        User user = userService.findByUsername(username); // fetch User object
        if (user == null) {
            user = new User();           // create empty User
            user.setFirstName("User Null");  // default value to prevent template errors
        }

        model.addAttribute("user", user);


        return "admin/manageusers"; // manageusers.html
    }

    // Update user
    @PostMapping("/save")
    public String saveUser(@RequestParam Long id,
                           @RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String role,
                           @RequestParam(required = false) String status,
                           @RequestParam Boolean enabled,
                           RedirectAttributes redirectAttributes) {
        User existing = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        existing.setUsername(username);
        existing.setEmail(email);
        existing.setRole(role);
        existing.setStatus(status);
        existing.setEnabled(enabled);
        userService.saveUser(existing);
        redirectAttributes.addFlashAttribute("success", "User saved successfully!");
        return "redirect:/admin/users";
    }

    // Delete user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        return "redirect:/admin/users";
    }

    // Edit user
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));

        model.addAttribute("user", user);
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    // Block user
    @GetMapping("/block/{id}")
    public String blockUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        user.setEnabled(false);
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("success", "User blocked successfully!");
        return "redirect:/admin/users";
    }

    // Unblock user
    @GetMapping("/unblock/{id}")
    public String unblockUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
        user.setEnabled(true);
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("success", "User unblocked successfully!");
        return "redirect:/admin/users";
    }

}

