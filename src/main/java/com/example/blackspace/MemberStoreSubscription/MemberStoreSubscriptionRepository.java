package com.example.blackspace.MemberStoreSubscription;


import com.example.blackspace.Model.Stores.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberStoreSubscriptionRepository extends JpaRepository<MemberStoreSubscription, Long> {
    List<MemberStoreSubscription> findByStore(Store store);
    Optional<MemberStoreSubscription> findByEmailAndStore(String email, Store store);



    List<MemberStoreSubscription> findByStore_IdAndActiveTrue(Long storeId);

}

