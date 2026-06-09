package com.example.blackspace.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "site_settings")
public class SiteSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "primary_color")
    private String primaryColor = "#302b63";

    @Column(name = "secondary_color")
    private String secondaryColor = "#24243e";

    @Column(name = "accent_color")
    private String accentColor = "#0d6efd";

    @Column(name = "font_family")
    private String fontFamily = "Arial, sans-serif";

    @Column(name = "site_logo")
    private String siteLogo = "logo.jpeg";

    @Column(name = "site_name")
    private String siteName = "BlackSpace Online Store";

    @Column(name = "site_favicon")
    private String siteFavicon = "favicon.png";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(String primaryColor) { this.primaryColor = primaryColor; }

    public String getSecondaryColor() { return secondaryColor; }
    public void setSecondaryColor(String secondaryColor) { this.secondaryColor = secondaryColor; }

    public String getAccentColor() { return accentColor; }
    public void setAccentColor(String accentColor) { this.accentColor = accentColor; }

    public String getFontFamily() { return fontFamily; }
    public void setFontFamily(String fontFamily) { this.fontFamily = fontFamily; }

    public String getSiteLogo() { return siteLogo; }
    public void setSiteLogo(String siteLogo) { this.siteLogo = siteLogo; }

    public String getSiteName() { return siteName; }
    public void setSiteName(String siteName) { this.siteName = siteName; }

    public String getSiteFavicon() { return siteFavicon; }
    public void setSiteFavicon(String siteFavicon) { this.siteFavicon = siteFavicon; }
}
