package com.example.blackspace.Model.Products;


import com.example.blackspace.Model.Stores.Store;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "productstock")
public class Productstock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "likes")
    private Integer likes = 0;

    @Column(name = "dislikes")
    private Integer dislikes = 0;


    @Column(name = "shortdescription", columnDefinition = "TEXT")
    private String shortdescription;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "price", precision = 10, scale = 2)
    private String price;

    @Column(name = "category",length = 100)
    private String category;

    @Column(name = "location")
    private String location;

    @Column(name = "currency")
    private String currency;

    @Column(name = "imageurl")
    private String imageurl; // main product image

    @Column(name = "username")
    private String username;

    @Column(name = "createdAt")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    // Audio-specific fields
    @Column(name = "audiourl")
    private String audiourl;   // path/URL to the uploaded audio file


    @Column(name = "imageurls", columnDefinition = "TEXT")
    private String imageurls; // multiple images, comma-separated


    // Many products belong to one store
    @Column(name = "storeid", length = 100)
    private String storeid;  // store ID as String


    @Column(name = "views")
    private Integer views = 0;

    @Column(name = "rating_total")
    private Integer ratingTotal = 0;

    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    @Transient
    public int getAverageRating() {
        if (ratingCount == null || ratingCount == 0) return 0;
        return Math.round((float) ratingTotal / ratingCount); // rounds to nearest whole number
    }

    public Integer getRatingTotal() {
        return ratingTotal;
    }

    public void setRatingTotal(Integer ratingTotal) {
        this.ratingTotal = ratingTotal;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }




    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getImageurls() {
        return imageurls;
    }

    public void setImageurls(String imageurls) {
        this.imageurls = imageurls;
    }


    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getAudiourl() {
        return audiourl;
    }

    public void setAudiourl(String audiourl) {
        this.audiourl = audiourl;
    }

    public String getShortdescription() {
        return shortdescription;
    }

    public void setShortdescription(String shortdescription) {
        this.shortdescription = shortdescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
