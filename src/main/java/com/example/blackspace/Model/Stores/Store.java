package com.example.blackspace.Model.Stores;

import com.example.blackspace.Model.Products.Productstock;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "whatsappNumber")
    private String whatsappNumber;

    @Column(name = "shopviews")
    private String shopviews;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "storeid")
    private String storeid;

    @Column(name = "useridimage")
    private String useridimage;

    @Column(name = "storebanner")
    private String storebanner;

    @Column(name = "storelogo")
    private String storelogo;

    @Column(name = "username")
    private String username;

    @Column(name = "numberofproducts")
    private String numberofproducts;

    @Column(name = "durationindays")
    private String durationindays;

    @Column(name = "durationtimeupdate")
    private LocalDateTime durationtimeupdate;

    public String getDurationindays() {
        return durationindays;
    }

    public void setDurationindays(String durationindays) {
        this.durationindays = durationindays;
    }

    public LocalDateTime getDurationtimeupdate() {
        return durationtimeupdate;
    }

    public void setDurationtimeupdate(LocalDateTime durationtimeupdate) {
        this.durationtimeupdate = durationtimeupdate;
    }

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "createdAt")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getNumberofproducts() {
        return numberofproducts;
    }

    public void setNumberofproducts(String numberofproducts) {
        this.numberofproducts = numberofproducts;
    }

    public String getWhatsappNumber() {
        return whatsappNumber;
    }

    public void setWhatsappNumber(String whatsappNumber) {
        this.whatsappNumber = whatsappNumber;
    }

    public String getShopviews() {
        return shopviews;
    }

    public void setShopviews(String shopviews) {
        this.shopviews = shopviews;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStorelogo() {
        return storelogo;
    }

    public void setStorelogo(String storelogo) {
        this.storelogo = storelogo;
    }

    public String getStorebanner() {
        return storebanner;
    }

    public void setStorebanner(String storebanner) {
        this.storebanner = storebanner;
    }

    public String getUseridimage() {
        return useridimage;
    }

    public void setUseridimage(String useridimage) {
        this.useridimage = useridimage;
    }

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
