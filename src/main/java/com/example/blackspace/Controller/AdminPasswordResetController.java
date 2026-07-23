package com.example.blackspace.Controller;

import com.example.blackspace.Model.User;
import com.example.blackspace.Passwordreset.PasswordResetService;
import com.example.blackspace.Passwordreset.PasswordResetTokenRepository;
import com.example.blackspace.Repository.user.UserRepository;
import com.example.blackspace.SMS.EmailService;
import com.example.blackspace.Service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminPasswordResetController {

    private final PasswordResetTokenRepository tokenRepo;
    private final PasswordResetService resetService;
    private final UserRepository userRepo;
    private final EmailService emailService;
    private final UserService userService;

    public AdminPasswordResetController(PasswordResetTokenRepository tokenRepo,
                                         PasswordResetService resetService,
                                         UserRepository userRepo,
                                         EmailService emailService,
                                         UserService userService) {
        this.tokenRepo = tokenRepo;
        this.resetService = resetService;
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.userService = userService;
    }

    @GetMapping("/admin/password-resets")
    public String viewPasswordResets(Model model, HttpSession session) {
        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");
        User adminUser = userService.findByUsername(username);
        if (adminUser == null) {
            adminUser = new User();
            adminUser.setFirstName("Admin");
        }
        model.addAttribute("user", adminUser);
        model.addAttribute("resetTokens", tokenRepo.findAllByOrderByExpiryDateDesc());
        model.addAttribute("allUsers", userRepo.findAll());
        return "admin/managepasswordresets";
    }

    @PostMapping("/admin/send-reset-link")
    public String sendResetLink(@RequestParam String email,
                                 RedirectAttributes redirect,
                                 HttpServletRequest request) {

        var userOpt = userRepo.findByEmail(email);
        if (userOpt.isEmpty()) {
            redirect.addFlashAttribute("error", "No account found with email: " + email);
            return "redirect:/admin/password-resets";
        }

        try {
            var user = userOpt.get();
            String token = resetService.createResetToken(user);

            String baseUrl = request.getScheme() + "://" + request.getServerName();
            int port = request.getServerPort();
            if (port != 80 && port != 443) {
                baseUrl += ":" + port;
            }
            String link = baseUrl + "/reset-password?token=" + token;

            String html = buildBrandedEmail(
                "Password Reset Request",
                "<p style='font-size:16px;color:#3A3A40;'>An administrator has initiated a password reset for your <strong>BlackSpace Online Store</strong> account.</p>"
                + "<div style='text-align:center;margin:24px 0;'>"
                + "<a href='" + link + "' style='display:inline-block;padding:14px 36px;"
                + "background:linear-gradient(135deg,#E8611D,#B8480F);color:#fff;text-decoration:none;"
                + "border-radius:10px;font-weight:600;font-size:15px;'>Reset My Password</a>"
                + "</div>"
                + "<p style='color:#8A8A93;font-size:13px;'>This link will expire in <strong>30 minutes</strong>.</p>"
                + "<p style='color:#8A8A93;font-size:12px;'>If you didn't request this, you can safely ignore this email.</p>"
            );

            emailService.sendHtmlEmail(user.getEmail(), "BlackSpace - Password Reset", html);
            redirect.addFlashAttribute("success", "Password reset link sent to " + email);
        } catch (Exception e) {
            e.printStackTrace();
            redirect.addFlashAttribute("error", "Failed to send reset link. Please try again.");
        }

        return "redirect:/admin/password-resets";
    }

    private String buildBrandedEmail(String heading, String bodyContent) {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body style='margin:0;padding:0;background:#F6F6F4;font-family:Arial,sans-serif;'>"
            + "<div style='max-width:500px;margin:30px auto;background:#fff;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,.1);'>"
            + "<div style='background:linear-gradient(135deg,#0E0E11,#3A3A40);padding:30px;text-align:center;'>"
            + "<h1 style='color:#fff;margin:12px 0 4px;font-size:20px;letter-spacing:1px;'>BlackSpace Online Store</h1>"
            + "<p style='color:rgba(255,255,255,.6);margin:0;font-size:13px;'>Password Reset</p>"
            + "</div>"
            + "<div style='padding:28px;'>"
            + "<h2 style='color:#3A3A40;margin:0 0 16px;font-size:18px;'>" + heading + "</h2>"
            + bodyContent
            + "</div>"
            + "<div style='background:#F6F6F4;padding:20px;text-align:center;border-top:1px solid #E7E7EA;'>"
            + "<p style='margin:0 0 4px;color:#3A3A40;font-weight:600;font-size:13px;'>BlackSpace Online Store</p>"
            + "<p style='margin:0;color:#8A8A93;font-size:12px;'>Lusaka, Zambia | +260 960 847 099</p>"
            + "</div>"
            + "</div></body></html>";
    }
}
