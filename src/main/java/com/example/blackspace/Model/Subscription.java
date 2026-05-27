package com.example.blackspace.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String amount;

    @Column(name = "durationtime")
    private String durationtime;

    @Column(name = "numberofproducts")
    private String numberofproducts;



    /* ===== Derived duration ===== */
    @Transient
    private String duration;

    public Subscription() {}


    /* ===== Getters & Setters ===== */

    public String getNumberofproducts() {
        return numberofproducts;
    }

    public void setNumberofproducts(String numberofproducts) {
        this.numberofproducts = numberofproducts;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAmount() { return amount; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getDurationtime() {
        return durationtime;
    }

    public void setDurationtime(String durationtime) {
        this.durationtime = durationtime;
    }
}
