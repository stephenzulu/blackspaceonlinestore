package com.example.blackspace.MemberStoreSubscription;


import com.example.blackspace.Model.Stores.Store;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "memberstoresubscription")
public class MemberStoreSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "subscribed_at")
    private LocalDateTime subscribedAt = LocalDateTime.now();

    @Column(name = "active")
    private Boolean active = true;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }

    public LocalDateTime getSubscribedAt() { return subscribedAt; }
    public void setSubscribedAt(LocalDateTime subscribedAt) { this.subscribedAt = subscribedAt; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
