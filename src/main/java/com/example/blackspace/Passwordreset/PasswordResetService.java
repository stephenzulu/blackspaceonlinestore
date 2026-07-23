package com.example.blackspace.Passwordreset;

import com.example.blackspace.Model.User;
import com.example.blackspace.Repository.user.UserRepository;
import com.example.blackspace.SMS.EmailService;
import jakarta.persistence.EntityManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EntityManager entityManager;

    public PasswordResetService(
            PasswordResetTokenRepository tokenRepo,
            UserRepository userRepo,
            PasswordEncoder passwordEncoder, EmailService emailService,
            EntityManager entityManager) {
        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.entityManager = entityManager;
    }

    @Transactional
    public String createResetToken(User user) {
        tokenRepo.deleteByUser(user); // invalidate old tokens
        entityManager.flush(); // ensure delete is executed before insert

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        tokenRepo.save(token);
        return token.getToken();
    }

    public boolean isValid(String token) {
        return tokenRepo.findByToken(token)
                .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepo.findByToken(token)
                .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        tokenRepo.delete(resetToken);
        // Send confirmation email
        sendPasswordResetConfirmation(user);
    }

    private void sendPasswordResetConfirmation(User user) {
        String html = buildBrandedEmail(
            "Password Changed Successfully",
            "<p style='font-size:16px;color:#3A3A40;'>Hello <strong>" + user.getFirstName() + "</strong>,</p>"
            + "<p style='color:#3A3A40;'>Your password for <strong>BlackSpace Online Store</strong> has been successfully changed.</p>"
            + "<div style='background:#E6F2EC;border-left:4px solid #0F7A4E;padding:12px 16px;border-radius:6px;margin:16px 0;'>"
            + "<p style='margin:0;color:#0F7A4E;font-weight:600;font-size:14px;'>Password updated successfully</p>"
            + "</div>"
            + "<p style='color:#8A8A93;font-size:14px;'>If you did <strong>NOT</strong> perform this action, please contact our support team immediately.</p>"
            + "<p style='color:#8A8A93;font-size:13px;'>Email: blackspaceonlinestore9@gmail.com</p>"
        );

        try {
            emailService.sendHtmlEmail(
                    user.getEmail(),
                    "BlackSpace - Password Changed",
                    html
            );
        } catch (Exception e) {
            System.err.println("Failed to send password change email: " + e.getMessage());
        }
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
            + "<p style='margin:8px 0 0;color:#8A8A93;font-size:11px;'>www.blackspaceonline.com</p>"
            + "</div>"
            + "</div></body></html>";
    }


}

