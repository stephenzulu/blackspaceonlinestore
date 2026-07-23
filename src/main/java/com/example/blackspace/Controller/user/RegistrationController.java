package com.example.blackspace.Controller.user;


import com.example.blackspace.Model.User;
import com.example.blackspace.Recaptcha.RecaptchaConfig;
import com.example.blackspace.SMS.EmailService;
import com.example.blackspace.Service.User.UserService;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final RecaptchaConfig recaptchaConfig;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserService userService;

    public RegistrationController(
            RecaptchaConfig recaptchaConfig,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            UserService userService) {

        this.recaptchaConfig = recaptchaConfig;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userService = userService;
    }

    // ✅ FIXED GET METHOD
    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        // ✅ ADD reCAPTCHA SITE KEY
        model.addAttribute(
                "recaptchaSiteKey",
                recaptchaConfig.getSiteKey()
        );

        return "auth/register";
    }

    @PostMapping
    public String registerUser(
            @ModelAttribute User user,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes
    ) {

        if (!user.getPassword().equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/register";
        }

        if (userService.emailExists(user.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email already exists");
            return "redirect:/register";
        }

        if (userService.phoneExists(user.getPhoneNumber())) {
            redirectAttributes.addFlashAttribute("error", "Phone number already exists");
            return "redirect:/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUsername(generateUsername(user.getFirstName(), user.getLastName()));
        user.setRole("SELLER");
        user.setEnabled(true);
        user.setStatus("1");

        userService.saveUser(user);

        redirectAttributes.addFlashAttribute("registered", true);
        redirectAttributes.addFlashAttribute("registeredName", user.getFirstName());

        return "redirect:/login";
    }

    private String generateUsername(String firstName, String lastName) {
        String base = (firstName.substring(0, 1) + lastName)
                .toLowerCase()
                .replaceAll("\\s+", "");

        String username = base;
        int i = 1;
        while (userService.getUserByUsername(username).isPresent()) {
            username = base + i++;
        }
        return username;
    }
}

