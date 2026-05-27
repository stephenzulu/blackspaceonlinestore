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
            String html = "<h2>Welcome back, " + user.getFirstName() +" "+ user.getLastName()+ "!</h2>"
                    + "<p>You have successfully logged in to BlackSpace.</p>";
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

}


