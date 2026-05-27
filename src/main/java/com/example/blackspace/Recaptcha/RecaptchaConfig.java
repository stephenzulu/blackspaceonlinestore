package com.example.blackspace.Recaptcha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RecaptchaConfig {

    @Value("${recaptcha.site}")
    private String siteKey;

    public String getSiteKey() {
        return siteKey;
    }
}
