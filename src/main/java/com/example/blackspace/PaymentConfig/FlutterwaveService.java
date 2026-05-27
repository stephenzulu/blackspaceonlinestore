package com.example.blackspace.PaymentConfig;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class FlutterwaveService {

    @Value("${flutterwave.secret-key}")
    private String secretKey;

    @Value("${flutterwave.redirect-url}")
    private String redirectUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Create payment link (v3)
    public String createPaymentLink(String name, String email, String phone, double amount, String currency) throws Exception {
        String url = "https://api.flutterwave.com/v3/payments";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(secretKey);

        Map<String, Object> customer = new HashMap<>();
        customer.put("name", name);
        customer.put("email", email);
        customer.put("phonenumber", phone);

        Map<String, Object> body = new HashMap<>();
        body.put("amount", amount);
        body.put("currency", currency);
        body.put("tx_ref", "TX-" + System.currentTimeMillis());
        body.put("redirect_url", redirectUrl);
        body.put("customer", customer);
        body.put("payment_options", "card,mobilemoneyzambia");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("data").get("link").asText();  // payment link
        } else {
            throw new RuntimeException("Failed to create payment link: " + response.getBody());
        }
    }

    // Verify transaction by ID
    public boolean verifyTransaction(String txId) throws Exception {
        String url = "https://api.flutterwave.com/v3/transactions/" + txId + "/verify";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(secretKey);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            String status = jsonNode.get("data").get("status").asText();
            return "successful".equalsIgnoreCase(status);
        } else {
            throw new RuntimeException("Failed to verify transaction: " + response.getBody());
        }
    }
}
