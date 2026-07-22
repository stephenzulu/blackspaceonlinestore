package com.example.blackspace.Service.Subscription;




import com.example.blackspace.Model.Payment;
import com.example.blackspace.Model.Stores.Store;
import com.example.blackspace.Model.Subscription;
import com.example.blackspace.Repository.PaymentRepository;
import com.example.blackspace.Repository.SubscriptionRepository;
import com.example.blackspace.Repository.user.StoreRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final PaymentRepository paymentRepository;
    private final StoreRepository storeRepository; // inject this

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, PaymentRepository paymentRepository, StoreRepository storeRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.paymentRepository = paymentRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    public List<Subscription> getAllSubscription() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription saveSubscription(Subscription subscription) {
        if (subscriptionRepository.findByName(subscription.getName()) != null) {
            throw new RuntimeException("Subscription with name '" + subscription.getName() + "' already exists.");
        }
        return subscriptionRepository.save(subscription);
    }

    //SubscriptionServiceImpl.java
    @Override
    public Subscription updateSubscription(Subscription subscription) {
        Subscription existing = subscriptionRepository.findByName(subscription.getName());
        if (existing != null && !existing.getId().equals(subscription.getId())) {
            throw new RuntimeException("Subscription with name '" + subscription.getName() + "' already exists.");
        }
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }


    @Override
    public Subscription getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
    }



    @Override
    @Transactional
    public void upgradeUserSubscription(String username, Long subscriptionId) {

        //  Disable existing active subscription(s)
        List<Payment> activePayments =
                paymentRepository.findByUsernameAndPaymentTypeAndEnabled(
                        username,
                        "SUBSCRIPTION",
                        1
                );

        for (Payment p : activePayments) {
            p.setEnabled(0);
            p.setStatus("UPGRADED");
            paymentRepository.save(p);
        }

        //  Create new active subscription payment
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        Payment newPayment = new Payment();
        newPayment.setUsername(username);
        newPayment.setPaymentType("SUBSCRIPTION");
        newPayment.setSubscriptionid(subscription.getId().toString());
        newPayment.setAmount(subscription.getAmount());
        newPayment.setPaymentMethod("UPGRADE");
        newPayment.setTransactionReference(subscription.getDescription());
        newPayment.setStatus("ACTIVE");
        newPayment.setEnabled(1);
        newPayment.setCreatedtime(LocalDateTime.now());

        paymentRepository.save(newPayment);

        //  UPDATE STORE DURATION ⬅️ IMPORTANT PART

        // Update store
        List<Store> stores = storeRepository.findByUsername(username);
        if (stores.isEmpty()) {
            throw new RuntimeException("Store not found");
        }
        Store store = stores.get(0);

        int durationDays = Integer.parseInt(subscription.getDurationtime());
        store.setDurationindays(String.valueOf(durationDays));
        store.setNumberofproducts(subscription.getNumberofproducts());
        store.setDurationtimeupdate(LocalDateTime.now().plusDays(durationDays));

        System.out.println("futureDate: " + store.getDurationindays() +
                ", expiry: " + store.getDurationtimeupdate());

        storeRepository.save(store);

    }




    @Override
    @Transactional
    public void upgradeUserSubscription(String username, Long subscriptionId, String transactionReference) {

        List<Payment> activePayments =
                paymentRepository.findByUsernameAndPaymentTypeAndEnabled(
                        username,
                        "SUBSCRIPTION",
                        1
                );

        for (Payment p : activePayments) {
            p.setEnabled(0);
            p.setStatus("UPGRADED");
            paymentRepository.save(p);
        }

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        Payment newPayment = new Payment();
        newPayment.setUsername(username);
        newPayment.setPaymentType("SUBSCRIPTION");
        newPayment.setSubscriptionid(subscription.getId().toString());
        newPayment.setAmount(subscription.getAmount());
        newPayment.setPaymentMethod("LENCO");
        newPayment.setTransactionReference(transactionReference);
        newPayment.setStatus("ACTIVE");
        newPayment.setEnabled(1);
        newPayment.setCreatedtime(LocalDateTime.now());

        paymentRepository.save(newPayment);

        List<Store> stores = storeRepository.findByUsername(username);
        if (stores.isEmpty()) {
            throw new RuntimeException("Store not found");
        }
        Store store = stores.get(0);

        int durationDays = Integer.parseInt(subscription.getDurationtime());
        store.setDurationindays(String.valueOf(durationDays));
        store.setNumberofproducts(subscription.getNumberofproducts());
        store.setDurationtimeupdate(LocalDateTime.now().plusDays(durationDays));

        storeRepository.save(store);
    }

    @Override
    public Subscription getLatestSubscription() {
        return subscriptionRepository
                .findTopByOrderByIdDesc()
                .orElseThrow(() -> new RuntimeException("No subscriptions found"));
    }











}
