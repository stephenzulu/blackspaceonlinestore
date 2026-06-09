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

            String html = buildBrandedEmail(
                "Password Reset Request",
                "<p style='font-size:16px;color:#333;'>We received a request to reset your password for your <strong>BlackSpace Online Store</strong> account.</p>"
                + "<div style='text-align:center;margin:24px 0;'>"
                + "<a href='" + link + "' style='display:inline-block;padding:14px 36px;"
                + "background:linear-gradient(135deg,#302b63,#24243e);color:#fff;text-decoration:none;"
                + "border-radius:10px;font-weight:600;font-size:15px;'>Reset My Password</a>"
                + "</div>"
                + "<p style='color:#666;font-size:13px;'>This link will expire in <strong>30 minutes</strong>.</p>"
                + "<p style='color:#999;font-size:12px;'>If you didn't request this, you can safely ignore this email.</p>"
            );
            try {
                emailService.sendHtmlEmail(
                        user.getEmail(),
                        "BlackSpace - Password Reset",
                        html
                );
            } catch (Exception e) {
                System.err.println("Failed to send reset email: " + e.getMessage());
            }
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

    private String buildBrandedEmail(String heading, String bodyContent) {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body style='margin:0;padding:0;background:#f0f2f5;font-family:Arial,sans-serif;'>"
            + "<div style='max-width:500px;margin:30px auto;background:#fff;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,.1);'>"
            + "<div style='background:linear-gradient(135deg,#0f0c29,#302b63,#24243e);padding:30px;text-align:center;'>"
            + "<img src='https://via.placeholder.com/60x60/302b63/fff?text=BS' style='width:60px;height:60px;border-radius:14px;border:2px solid rgba(255,255,255,.3);' alt='Logo'>"
            + "<h1 style='color:#fff;margin:12px 0 4px;font-size:20px;letter-spacing:1px;'>BlackSpace Online Store</h1>"
            + "<p style='color:rgba(255,255,255,.6);margin:0;font-size:13px;'>Your AI-Powered Marketplace</p>"
            + "</div>"
            + "<div style='padding:28px;'>"
            + "<h2 style='color:#302b63;margin:0 0 16px;font-size:18px;'>" + heading + "</h2>"
            + bodyContent
            + "</div>"
            + "<div style='background:#f8f9fa;padding:20px;text-align:center;border-top:1px solid #e9ecef;'>"
            + "<p style='margin:0 0 4px;color:#302b63;font-weight:600;font-size:13px;'>BlackSpace Online Store</p>"
            + "<p style='margin:0;color:#999;font-size:12px;'>Lusaka, Zambia | +260 960 847 099</p>"
            + "<p style='margin:8px 0 0;color:#999;font-size:11px;'>www.blackspaceonline.com</p>"
            + "</div>"
            + "</div></body></html>";
    }

}

