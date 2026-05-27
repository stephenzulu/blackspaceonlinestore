package com.example.blackspace.Recaptcha;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RecaptchaService {

    @Value("${recaptcha.secret}")
    private String secretKey;

    private final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean verify(String response) {
        RestTemplate restTemplate = new RestTemplate();
        String url = RECAPTCHA_VERIFY_URL + "?secret=" + secretKey + "&response=" + response;
        Map<String, Object> result = restTemplate.postForObject(url, null, Map.class);
        return result != null && Boolean.TRUE.equals(result.get("success"));
    }
}

