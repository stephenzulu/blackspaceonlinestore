package com.example.blackspace.Config;


import com.example.blackspace.Model.User;
import com.example.blackspace.Repository.user.UserRepository;
import com.example.blackspace.SMS.EmailService;
import com.example.blackspace.Service.OtpService;
import com.example.blackspace.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final OtpService otpService;

    public LoginController(UserRepository userRepository, EmailService emailService, OtpService otpService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.otpService = otpService;
    }

    @GetMapping("/post-login")
    public String postLogin(Authentication authentication, HttpSession session) {

        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail).orElse(null);

        if (user == null) return "redirect:/login?error=true";

        // Store email in session for OTP verification (not fully logged in yet)
        session.setAttribute("OTP_EMAIL", userEmail);
        session.setAttribute("OTP_PENDING", true);

        // Generate and send OTP
        try {
            otpService.generateAndSendOtp(userEmail);
        } catch (Exception e) {
            System.err.println("Failed to send OTP: " + e.getMessage());
        }

        return "redirect:/verify-otp";
    }

    @GetMapping("/verify-otp")
    public String showOtpPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("OTP_EMAIL");
        if (email == null) return "redirect:/login";

        // Mask email for display
        String masked = maskEmail(email);
        model.addAttribute("maskedEmail", masked);
        return "auth/verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp, HttpSession session,
                            RedirectAttributes redirectAttributes, Authentication authentication) {

        String email = (String) session.getAttribute("OTP_EMAIL");
        if (email == null) return "redirect:/login";

        if (!otpService.verifyOtp(email, otp)) {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired OTP. Please try again.");
            return "redirect:/verify-otp";
        }

        // OTP verified — complete login
        session.removeAttribute("OTP_PENDING");
        session.removeAttribute("OTP_EMAIL");

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return "redirect:/login";

        session.setAttribute("LOGGED_IN_USERNAME", user.getUsername());

        // Send welcome email
        String html = buildBrandedEmail(
            "Welcome Back, " + user.getFirstName() + "!",
            "<p style='font-size:16px;color:#3A3A40;'>You have successfully logged into your <strong>BlackSpace Online Store</strong> account.</p>"
            + "<table style='width:100%;border-collapse:collapse;margin:16px 0;'>"
            + "<tr><td style='padding:8px 12px;background:#F6F6F4;border-radius:6px;color:#8A8A93;'>Account</td>"
            + "<td style='padding:8px 12px;background:#F6F6F4;border-radius:6px;font-weight:600;'>" + user.getEmail() + "</td></tr>"
            + "<tr><td style='padding:8px 12px;color:#8A8A93;'>Role</td>"
            + "<td style='padding:8px 12px;font-weight:600;'>" + user.getRole() + "</td></tr>"
            + "</table>"
            + "<p style='color:#8A8A93;font-size:14px;'>If this wasn't you, please change your password immediately or contact support.</p>"
        );
        try {
            emailService.sendHtmlEmail(email, "Welcome Back to BlackSpace!", html);
        } catch (Exception e) {
            System.err.println("Failed to send login email: " + e.getMessage());
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

    @PostMapping("/resend-otp")
    public String resendOtp(HttpSession session, RedirectAttributes redirectAttributes) {
        String email = (String) session.getAttribute("OTP_EMAIL");
        if (email == null) return "redirect:/login";

        try {
            otpService.generateAndSendOtp(email);
            redirectAttributes.addFlashAttribute("success", "A new OTP has been sent to your email.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to resend OTP. Please try again.");
        }
        return "redirect:/verify-otp";
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 2) return email;
        return email.charAt(0) + "***" + email.substring(atIndex - 1);
    }

    private String buildBrandedEmail(String heading, String bodyContent) {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body style='margin:0;padding:0;background:#F6F6F4;font-family:Arial,sans-serif;'>"
            + "<div style='max-width:500px;margin:30px auto;background:#fff;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,.1);'>"
            // Header
            + "<div style='background:linear-gradient(135deg,#0E0E11,#3A3A40);padding:30px;text-align:center;'>"
            + "<h1 style='color:#fff;margin:12px 0 4px;font-size:20px;letter-spacing:1px;'>BlackSpace Online Store</h1>"
            + "<p style='color:rgba(255,255,255,.6);margin:0;font-size:13px;'>Your AI-Powered Marketplace</p>"
            + "</div>"
            // Body
            + "<div style='padding:28px;'>"
            + "<h2 style='color:#3A3A40;margin:0 0 16px;font-size:18px;'>" + heading + "</h2>"
            + bodyContent
            + "</div>"
            // Footer
            + "<div style='background:#F6F6F4;padding:20px;text-align:center;border-top:1px solid #E7E7EA;'>"
            + "<p style='margin:0 0 4px;color:#3A3A40;font-weight:600;font-size:13px;'>BlackSpace Online Store</p>"
            + "<p style='margin:0;color:#8A8A93;font-size:12px;'>Lusaka, Zambia | +260 960 847 099</p>"
            + "<p style='margin:8px 0 0;color:#8A8A93;font-size:11px;'>www.blackspaceonline.com</p>"
            + "</div>"
            + "</div></body></html>";
    }

}


