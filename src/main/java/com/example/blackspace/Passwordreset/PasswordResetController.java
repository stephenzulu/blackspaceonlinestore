package com.example.blackspace.Passwordreset;

import com.example.blackspace.Repository.user.UserRepository;
import com.example.blackspace.SMS.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordResetController {

    private final UserRepository userRepo;
    private final PasswordResetService resetService;
    private final EmailService emailService;

    public PasswordResetController(
            UserRepository userRepo,
            PasswordResetService resetService,
            EmailService emailService) {
        this.userRepo = userRepo;
        this.resetService = resetService;
        this.emailService = emailService;
    }

    // Forgot password page
    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    // Submit email
    @PostMapping("/forgot-password")
    public String processForgotPassword(
            @RequestParam String email,
            RedirectAttributes redirect) {

        userRepo.findByEmail(email).ifPresent(user -> {
            String token = resetService.createResetToken(user);
            String link = "http://localhost:8090/reset-password?token=" + token;

            emailService.sendSimpleEmail(
                    user.getEmail(),
                    "Password Reset",
                    "Click the link to reset your password:\n" + link
            );
        });

        redirect.addFlashAttribute("message",
                "If the email exists, a reset link has been sent.");
        return "redirect:/forgot-password";
    }

    // Reset page
    @GetMapping("/reset-password")
    public String resetPassword(@RequestParam String token, Model model) {
        if (!resetService.isValid(token)) {
            return "redirect:/login?invalid-token";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    // Save new password
    @PostMapping("/reset-password")
    public String savePassword(
            @RequestParam String token,
            @RequestParam String password,
            RedirectAttributes redirect) {

        try {
            resetService.resetPassword(token, password);
            redirect.addFlashAttribute("success", "Password reset successful");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirect.addFlashAttribute("error", "Invalid or expired reset link");
            return "redirect:/forgot-password";
        }
    }



}

