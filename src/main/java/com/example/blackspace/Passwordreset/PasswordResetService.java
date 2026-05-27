package com.example.blackspace.Passwordreset;

import com.example.blackspace.Model.User;
import com.example.blackspace.Repository.user.UserRepository;
import com.example.blackspace.SMS.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PasswordResetService(
            PasswordResetTokenRepository tokenRepo,
            UserRepository userRepo,
            PasswordEncoder passwordEncoder, EmailService emailService) {
        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public String createResetToken(User user) {
        tokenRepo.deleteByUser(user); // invalidate old tokens

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
        String subject = "Your password has been changed";
        String message =
                "Hello " + user.getUsername() + ",\n\n" +
                        "This is a confirmation that your password was successfully changed.\n\n" +
                        "If you did NOT perform this action, please contact support immediately.\n\n" +
                        "Regards,\nBlack Space Store Security Team";

        emailService.sendSimpleEmail(
                user.getEmail(),
                subject,
                message
        );
    }


}

