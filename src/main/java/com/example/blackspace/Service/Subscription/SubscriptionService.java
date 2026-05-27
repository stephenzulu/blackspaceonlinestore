package com.example.blackspace.Service.Subscription;



import com.example.blackspace.Model.Subscription;

import java.util.List;

public interface SubscriptionService {

    List<Subscription> getAllSubscription();

    Subscription saveSubscription(Subscription subscription);

    void deleteSubscription(Long id);
    // Update an existing Subscription
    Subscription updateSubscription(Subscription subscription);
    Subscription getSubscriptionById(Long id);


    void upgradeUserSubscription(String username, Long subscriptionId);


    Subscription getLatestSubscription();

}

