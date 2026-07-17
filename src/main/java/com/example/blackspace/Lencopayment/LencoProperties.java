package com.example.blackspace.Lencopayment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Binds the `lenco.*` keys from application.yml.
 * Kept as a @Component so it is picked up by component scanning; alternatively
 * drop @Component and add @EnableConfigurationProperties(LencoProperties.class)
 * to your main application class.
 */
@Component
@ConfigurationProperties(prefix = "lenco")
public class LencoProperties {

    /** Your Lenco PUBLIC key (safe to expose to the browser). */
    private String publicKey;

    /** Your Lenco SECRET key. Never send this to the frontend. */
    private String secretKey;

    /** Lenco API base URL. */
    private String baseUrl = "https://api.lenco.co/access/v2";

    /**
     * URL of the Lenco inline widget script served to the browser.
     * Production: https://pay.lenco.co/js/v1/inline.js
     * Sandbox:    https://pay.sandbox.lenco.co/js/v1/inline.js
     */
    private String inlineJsUrl = "https://pay.lenco.co/js/v1/inline.js";

    /** Default currency for the checkout form. */
    private String currency = "ZMW";

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getInlineJsUrl() {
        return inlineJsUrl;
    }

    public void setInlineJsUrl(String inlineJsUrl) {
        this.inlineJsUrl = inlineJsUrl;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
