package com.example.blackspace.Controller;

import com.example.blackspace.Model.SiteSettings;
import com.example.blackspace.Model.User;
import com.example.blackspace.Repository.SiteSettingsRepository;
import com.example.blackspace.Service.User.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class SiteSettingsController {

    private final SiteSettingsRepository settingsRepo;
    private final UserService userService;

    public SiteSettingsController(SiteSettingsRepository settingsRepo, UserService userService) {
        this.settingsRepo = settingsRepo;
        this.userService = userService;
    }

    @GetMapping("/admin/settings")
    public String viewSettings(Model model, HttpSession session) {
        SiteSettings settings = settingsRepo.findAll().stream().findFirst()
                .orElse(new SiteSettings());
        model.addAttribute("settings", settings);

        String username = (String) session.getAttribute("LOGGED_IN_USERNAME");
        User user = userService.findByUsername(username);
        if (user == null) { user = new User(); user.setFirstName("Admin"); }
        model.addAttribute("user", user);

        return "admin/settings";
    }

    @PostMapping("/admin/settings/save")
    public String saveSettings(
            @RequestParam(required = false) String primaryColor,
            @RequestParam(required = false) String secondaryColor,
            @RequestParam(required = false) String accentColor,
            @RequestParam(required = false) String fontFamily,
            @RequestParam(required = false) String siteName,
            @RequestParam(required = false) MultipartFile logoFile,
            @RequestParam(required = false) MultipartFile faviconFile,
            RedirectAttributes redirect) {

        SiteSettings settings = settingsRepo.findAll().stream().findFirst()
                .orElse(new SiteSettings());

        if (primaryColor != null && !primaryColor.isEmpty()) settings.setPrimaryColor(primaryColor);
        if (secondaryColor != null && !secondaryColor.isEmpty()) settings.setSecondaryColor(secondaryColor);
        if (accentColor != null && !accentColor.isEmpty()) settings.setAccentColor(accentColor);
        if (fontFamily != null && !fontFamily.isEmpty()) settings.setFontFamily(fontFamily);
        if (siteName != null && !siteName.isEmpty()) settings.setSiteName(siteName);

        if (logoFile != null && !logoFile.isEmpty()) {
            try {
                String uploadDir = System.getProperty("user.dir") + "/uploads/logo";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

                String filename = System.currentTimeMillis() + "_" + logoFile.getOriginalFilename();
                logoFile.transferTo(uploadPath.resolve(filename).toFile());
                settings.setSiteLogo(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (faviconFile != null && !faviconFile.isEmpty()) {
            try {
                String staticDir = System.getProperty("user.dir") + "/src/main/resources/static";
                Path staticPath = Paths.get(staticDir);
                if (!Files.exists(staticPath)) Files.createDirectories(staticPath);

                // Also save to uploads for serving
                String uploadsDir = System.getProperty("user.dir") + "/uploads/favicon";
                Path uploadsPath = Paths.get(uploadsDir);
                if (!Files.exists(uploadsPath)) Files.createDirectories(uploadsPath);

                String filename = System.currentTimeMillis() + "_" + faviconFile.getOriginalFilename();
                faviconFile.transferTo(uploadsPath.resolve(filename).toFile());
                settings.setSiteFavicon(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        settingsRepo.save(settings);
        redirect.addFlashAttribute("success", "Settings saved successfully!");
        return "redirect:/admin/settings";
    }
}
