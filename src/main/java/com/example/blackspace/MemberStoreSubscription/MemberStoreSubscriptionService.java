package com.example.blackspace.MemberStoreSubscription;



import com.example.blackspace.Model.Stores.Store;

import java.util.List;

public interface MemberStoreSubscriptionService {

    MemberStoreSubscription subscribe(String email, Store store);

    void unsubscribe(String email, Store store);

    List<MemberStoreSubscription> getStoreSubscribers(Store store);

    boolean isSubscribed(String email, Store store);
}

