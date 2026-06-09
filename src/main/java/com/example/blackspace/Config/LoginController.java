package com.example.blackspace.Config;


import com.example.blackspace.Model.User;
import com.example.blackspace.Repository.user.UserRepository;
import com.example.blackspace.SMS.EmailService;
import com.example.blackspace.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private  final UserRepository userRepository;

    private final EmailService emailService;


    public LoginController(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @GetMapping("/post-login")
    public String postLogin(Authentication authentication, HttpSession session) {

        String userEmail = authentication.getName(); // this is the email
        User user = userRepository.findByEmail(userEmail).orElse(null);

        //  SAVE IN SESSION
        session.setAttribute("LOGGED_IN_USERNAME", user.getUsername());



        if (user != null) {
            String html = buildBrandedEmail(
                "Welcome Back, " + user.getFirstName() + "!",
                "<p style='font-size:16px;color:#333;'>You have successfully logged into your <strong>BlackSpace Online Store</strong> account.</p>"
                + "<table style='width:100%;border-collapse:collapse;margin:16px 0;'>"
                + "<tr><td style='padding:8px 12px;background:#f8f9fa;border-radius:6px;color:#666;'>Account</td>"
                + "<td style='padding:8px 12px;background:#f8f9fa;border-radius:6px;font-weight:600;'>" + user.getEmail() + "</td></tr>"
                + "<tr><td style='padding:8px 12px;color:#666;'>Role</td>"
                + "<td style='padding:8px 12px;font-weight:600;'>" + user.getRole() + "</td></tr>"
                + "</table>"
                + "<p style='color:#666;font-size:14px;'>If this wasn't you, please change your password immediately or contact support.</p>"
            );
            try {
                emailService.sendHtmlEmail(userEmail, "Welcome Back to BlackSpace!", html);
            } catch (Exception e) {
                System.err.println("Failed to send login email: " + e.getMessage());
            }
        }

        // Redirect based on role
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isSeller = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SELLER"));

        if (isAdmin) return "redirect:/admin/dashboard";
        if (isSeller) return "redirect:/account/dashboard";

        return "redirect:/";
    }

    private String buildBrandedEmail(String heading, String bodyContent) {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body style='margin:0;padding:0;background:#f0f2f5;font-family:Arial,sans-serif;'>"
            + "<div style='max-width:500px;margin:30px auto;background:#fff;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,.1);'>"
            // Header
            + "<div style='background:linear-gradient(135deg,#0f0c29,#302b63,#24243e);padding:30px;text-align:center;'>"
            + "<img src='https://via.placeholder.com/60x60/302b63/fff?text=BS' style='width:60px;height:60px;border-radius:14px;border:2px solid rgba(255,255,255,.3);' alt='Logo'>"
            + "<h1 style='color:#fff;margin:12px 0 4px;font-size:20px;letter-spacing:1px;'>BlackSpace Online Store</h1>"
            + "<p style='color:rgba(255,255,255,.6);margin:0;font-size:13px;'>Your AI-Powered Marketplace</p>"
            + "</div>"
            // Body
            + "<div style='padding:28px;'>"
            + "<h2 style='color:#302b63;margin:0 0 16px;font-size:18px;'>" + heading + "</h2>"
            + bodyContent
            + "</div>"
            // Footer
            + "<div style='background:#f8f9fa;padding:20px;text-align:center;border-top:1px solid #e9ecef;'>"
            + "<p style='margin:0 0 4px;color:#302b63;font-weight:600;font-size:13px;'>BlackSpace Online Store</p>"
            + "<p style='margin:0;color:#999;font-size:12px;'>Lusaka, Zambia | +260 960 847 099</p>"
            + "<p style='margin:8px 0 0;color:#999;font-size:11px;'>www.blackspaceonline.com</p>"
            + "</div>"
            + "</div></body></html>";
    }

}


