package com.example.blackspace.Recaptcha;


import com.example.blackspace.Recaptcha.RecaptchaConfig;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPageController {

    private final RecaptchaConfig recaptchaConfig;

    public AuthPageController(RecaptchaConfig recaptchaConfig) {
        this.recaptchaConfig = recaptchaConfig;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("recaptchaSiteKey", recaptchaConfig.getSiteKey());
        return "login";
    }
}
