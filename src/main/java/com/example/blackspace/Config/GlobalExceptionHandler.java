package com.example.blackspace.Config;

import com.example.blackspace.SMS.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String ADMIN_EMAIL = "blackspaceonlinestore9@gmail.com";

    private final EmailService emailService;

    public GlobalExceptionHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException ex, RedirectAttributes redirectAttributes) {
        log.warn("Validation error: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSize(MaxUploadSizeExceededException ex, RedirectAttributes redirectAttributes) {
        log.warn("File upload too large: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("error", "File size exceeds the maximum allowed size (10MB).");
        return "redirect:/";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model, HttpServletRequest request) {
        log.error("Unexpected error", ex);
        sendErrorEmail(ex, request);
        model.addAttribute("error", "An unexpected error occurred. Please try again.");
        model.addAttribute("status", 500);
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model, HttpServletRequest request) {
        log.error("Internal server error", ex);
        sendErrorEmail(ex, request);
        model.addAttribute("error", "Something went wrong. Please try again later.");
        model.addAttribute("status", 500);
        return "error";
    }

    private void sendErrorEmail(Exception ex, HttpServletRequest request) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String url = request.getRequestURL().toString();
            String method = request.getMethod();
            String queryString = request.getQueryString();
            String userAgent = request.getHeader("User-Agent");

            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String stackTrace = sw.toString();
            if (stackTrace.length() > 3000) {
                stackTrace = stackTrace.substring(0, 3000) + "\n... (truncated)";
            }

            String html = "<div style='font-family:Arial,sans-serif;max-width:600px;margin:0 auto;'>"
                    + "<div style='background:#E8611D;color:#fff;padding:20px;border-radius:10px 10px 0 0;'>"
                    + "<h2 style='margin:0;'>&#9888; BlackSpace Error Alert</h2></div>"
                    + "<div style='padding:20px;background:#f9f9f9;border:1px solid #ddd;border-radius:0 0 10px 10px;'>"
                    + "<table style='width:100%;border-collapse:collapse;'>"
                    + "<tr><td style='padding:8px;font-weight:bold;color:#555;'>Time:</td><td style='padding:8px;'>" + timestamp + "</td></tr>"
                    + "<tr><td style='padding:8px;font-weight:bold;color:#555;'>URL:</td><td style='padding:8px;'>" + method + " " + url + (queryString != null ? "?" + queryString : "") + "</td></tr>"
                    + "<tr><td style='padding:8px;font-weight:bold;color:#555;'>Error:</td><td style='padding:8px;color:#E8611D;font-weight:bold;'>" + ex.getClass().getSimpleName() + "</td></tr>"
                    + "<tr><td style='padding:8px;font-weight:bold;color:#555;'>Message:</td><td style='padding:8px;'>" + (ex.getMessage() != null ? ex.getMessage() : "No message") + "</td></tr>"
                    + "<tr><td style='padding:8px;font-weight:bold;color:#555;'>Browser:</td><td style='padding:8px;font-size:0.85em;'>" + (userAgent != null ? userAgent : "Unknown") + "</td></tr>"
                    + "</table>"
                    + "<div style='margin-top:15px;'><strong>Stack Trace:</strong>"
                    + "<pre style='background:#2d3436;color:#dfe6e9;padding:15px;border-radius:8px;font-size:0.8em;overflow-x:auto;white-space:pre-wrap;'>"
                    + stackTrace + "</pre></div>"
                    + "</div></div>";

            emailService.sendHtmlEmail(ADMIN_EMAIL, "BlackSpace Error: " + ex.getClass().getSimpleName(), html);
        } catch (Exception e) {
            log.error("Failed to send error notification email", e);
        }
    }
}
