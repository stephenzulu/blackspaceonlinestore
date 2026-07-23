package com.example.blackspace.Config;

import com.example.blackspace.Model.User;
import com.example.blackspace.Repository.user.UserRepository;
import com.example.blackspace.SMS.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLogoutHandler implements LogoutSuccessHandler {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public CustomLogoutHandler(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        if (authentication != null) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null) {
                String baseUrl = request.getScheme() + "://" + request.getServerName();
                int port = request.getServerPort();
                if (port != 80 && port != 443) {
                    baseUrl += ":" + port;
                }
                String loginUrl = baseUrl + "/login";

                String html = buildBrandedEmail(
                    "You've been logged out",
                    "<p style='font-size:16px;color:#3A3A40;'>Your <strong>BlackSpace Online Store</strong> session has ended.</p>"
                    + "<p style='color:#8A8A93;font-size:14px;'>You have been successfully logged out of your account. "
                    + "Your session data has been cleared for security.</p>"
                    + "<div style='text-align:center;margin:20px 0;'>"
                    + "<a href='" + loginUrl + "' style='display:inline-block;padding:12px 30px;"
                    + "background:linear-gradient(135deg,#E8611D,#B8480F);color:#fff;text-decoration:none;"
                    + "border-radius:8px;font-weight:600;font-size:14px;'>Log Back In</a>"
                    + "</div>"
                    + "<p style='color:#8A8A93;font-size:13px;'>If this wasn't you, please secure your account immediately.</p>"
                );
                try {
                    emailService.sendHtmlEmail(email, "BlackSpace - Session Ended", html);
                } catch (Exception e) {
                    System.err.println("Failed to send logout email: " + e.getMessage());
                }
            }
        }
        response.sendRedirect("/");
    }

    private String buildBrandedEmail(String heading, String bodyContent) {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body style='margin:0;padding:0;background:#F6F6F4;font-family:Arial,sans-serif;'>"
            + "<div style='max-width:500px;margin:30px auto;background:#fff;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,.1);'>"
            + "<div style='background:linear-gradient(135deg,#0E0E11,#3A3A40);padding:30px;text-align:center;'>"
            + "<h1 style='color:#fff;margin:12px 0 4px;font-size:20px;letter-spacing:1px;'>BlackSpace Online Store</h1>"
            + "<p style='color:rgba(255,255,255,.6);margin:0;font-size:13px;'>Your AI-Powered Marketplace</p>"
            + "</div>"
            + "<div style='padding:28px;'>"
            + "<h2 style='color:#3A3A40;margin:0 0 16px;font-size:18px;'>" + heading + "</h2>"
            + bodyContent
            + "</div>"
            + "<div style='background:#F6F6F4;padding:20px;text-align:center;border-top:1px solid #E7E7EA;'>"
            + "<p style='margin:0 0 4px;color:#3A3A40;font-weight:600;font-size:13px;'>BlackSpace Online Store</p>"
            + "<p style='margin:0;color:#8A8A93;font-size:12px;'>Lusaka, Zambia | +260 960 847 099</p>"
            + "<p style='margin:4px 0 0;color:#8A8A93;font-size:11px;'>Sent from: <a href='mailto:blackspaceonlinestore9@gmail.com' style='color:#E8611D;text-decoration:none;'>blackspaceonlinestore9@gmail.com</a></p>"
            + "<p style='margin:4px 0 0;color:#8A8A93;font-size:11px;'>www.blackspaceonline.com</p>"
            + "</div>"
            + "</div></body></html>";
    }
}
