package com.example.blackspace.MemberStoreSubscription;

import com.example.blackspace.Model.Stores.Store;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberStoreSubscriptionServiceImpl implements MemberStoreSubscriptionService {

    private final MemberStoreSubscriptionRepository subscriptionRepository;

    public MemberStoreSubscriptionServiceImpl(MemberStoreSubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public MemberStoreSubscription subscribe(String email, Store store) {
        return subscriptionRepository
                .findByEmailAndStore(email, store)
                .orElseGet(() -> {
                    MemberStoreSubscription sub = new MemberStoreSubscription();
                    sub.setEmail(email);
                    sub.setStore(store);
                    return subscriptionRepository.save(sub);
                });
    }

    @Override
    public void unsubscribe(String email, Store store) {
        subscriptionRepository.findByEmailAndStore(email, store)
                .ifPresent(subscriptionRepository::delete);
    }

    @Override
    public List<MemberStoreSubscription> getStoreSubscribers(Store store) {
        return subscriptionRepository.findByStore(store);
    }

    @Override
    public boolean isSubscribed(String email, Store store) {
        return subscriptionRepository.findByEmailAndStore(email, store).isPresent();
    }
}


