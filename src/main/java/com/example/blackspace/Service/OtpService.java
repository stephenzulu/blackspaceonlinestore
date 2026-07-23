package com.example.blackspace.Service;

import com.example.blackspace.Model.LoginOtp;
import com.example.blackspace.Repository.LoginOtpRepository;
import com.example.blackspace.SMS.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpService {

    private final LoginOtpRepository otpRepository;
    private final EmailService emailService;
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;

    public OtpService(LoginOtpRepository otpRepository, EmailService emailService) {
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void generateAndSendOtp(String email) {
        // Delete old OTPs for this user
        otpRepository.deleteByEmail(email);

        // Generate 6-digit OTP
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }

        // Save OTP
        LoginOtp loginOtp = new LoginOtp(email, otp.toString(), LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
        otpRepository.save(loginOtp);

        // Send email
        String html = buildOtpEmail(otp.toString());
        try {
            emailService.sendHtmlEmail(email, "Your Login Verification Code - BlackSpace", html);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    public boolean verifyOtp(String email, String otp) {
        Optional<LoginOtp> otpRecord = otpRepository.findTopByEmailAndUsedFalseOrderByExpiryDateDesc(email);
        if (otpRecord.isEmpty()) return false;

        LoginOtp loginOtp = otpRecord.get();
        if (loginOtp.isExpired()) return false;
        if (!loginOtp.getOtp().equals(otp)) return false;

        loginOtp.setUsed(true);
        otpRepository.save(loginOtp);
        return true;
    }

    private String buildOtpEmail(String otp) {
        StringBuilder digits = new StringBuilder();
        for (char c : otp.toCharArray()) {
            digits.append("<span style='display:inline-block;width:44px;height:52px;line-height:52px;")
                  .append("text-align:center;font-size:24px;font-weight:700;color:#0E0E11;")
                  .append("background:#F6F6F4;border:2px solid #E7E7EA;border-radius:10px;margin:0 3px;'>")
                  .append(c).append("</span>");
        }

        return "<!DOCTYPE html><html><head><meta charset='UTF-8'></head>"
            + "<body style='margin:0;padding:0;background:#F6F6F4;font-family:Arial,sans-serif;'>"
            + "<div style='max-width:480px;margin:30px auto;background:#fff;border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,.1);'>"
            // Header
            + "<div style='background:linear-gradient(135deg,#0E0E11,#3A3A40);padding:28px;text-align:center;'>"
            + "<h1 style='color:#fff;margin:0 0 4px;font-size:18px;letter-spacing:1px;'>BlackSpace Online Store</h1>"
            + "<p style='color:rgba(255,255,255,.6);margin:0;font-size:12px;'>Login Verification</p>"
            + "</div>"
            // Body
            + "<div style='padding:28px;text-align:center;'>"
            + "<div style='width:56px;height:56px;border-radius:14px;background:rgba(232,97,29,.1);display:inline-flex;align-items:center;justify-content:center;margin-bottom:16px;'>"
            + "<span style='font-size:24px;'>&#128274;</span></div>"
            + "<h2 style='color:#0E0E11;margin:0 0 8px;font-size:20px;'>Verification Code</h2>"
            + "<p style='color:#8A8A93;font-size:14px;margin:0 0 24px;'>Enter this code to complete your login</p>"
            // OTP digits
            + "<div style='margin:0 0 24px;'>" + digits + "</div>"
            + "<p style='color:#8A8A93;font-size:13px;margin:0 0 8px;'>This code expires in <strong style=\"color:#E8611D;\">" + OTP_EXPIRY_MINUTES + " minutes</strong></p>"
            + "<p style='color:#C33636;font-size:12px;margin:0;'>If you did not request this, please ignore this email.</p>"
            + "</div>"
            // Footer
            + "<div style='background:#F6F6F4;padding:16px;text-align:center;border-top:1px solid #E7E7EA;'>"
            + "<p style='margin:0;color:#8A8A93;font-size:11px;'>BlackSpace Online Store | Lusaka, Zambia</p>"
            + "</div>"
            + "</div></body></html>";
    }
}
