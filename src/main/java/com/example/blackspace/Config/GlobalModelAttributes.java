package com.example.blackspace.Config;

import com.example.blackspace.Model.SiteSettings;
import com.example.blackspace.Repository.SiteSettingsRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    private final SiteSettingsRepository siteSettingsRepository;

    public GlobalModelAttributes(SiteSettingsRepository siteSettingsRepository) {
        this.siteSettingsRepository = siteSettingsRepository;
    }

    @ModelAttribute("siteSettings")
    public SiteSettings siteSettings() {
        return siteSettingsRepository.findAll().stream().findFirst()
                .orElse(new SiteSettings());
    }

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null
                && auth.isAuthenticated()
                && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    @ModelAttribute("isSeller")
    public boolean isSeller() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null
                && auth.isAuthenticated()
                && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SELLER"));
    }

}
