package com.example.blackspace.MemberStoreSubscription;


import com.example.blackspace.MemberStoreSubscription.MemberStoreSubscription;
import com.example.blackspace.MemberStoreSubscription.MemberStoreSubscriptionRepository;
import com.example.blackspace.SMS.EmailService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreSubscriptionEmailService {

    private static final Logger log = LoggerFactory.getLogger(StoreSubscriptionEmailService.class);

    private final MemberStoreSubscriptionRepository subscriptionRepository;
    private final EmailService emailService;

    public StoreSubscriptionEmailService(
            MemberStoreSubscriptionRepository subscriptionRepository,
            EmailService emailService) {
        this.subscriptionRepository = subscriptionRepository;
        this.emailService = emailService;
    }

    public void sendEmailToStoreSubscribers(Long storeId, String subject, String htmlContent) {

        List<MemberStoreSubscription> subscribers =
                subscriptionRepository.findByStore_IdAndActiveTrue(storeId);

        if (subscribers.isEmpty()) {
            log.info("No subscribers found for store_id={}", storeId);
            return;
        }

        for (MemberStoreSubscription sub : subscribers) {
            try {
                emailService.sendHtmlEmail(
                        sub.getEmail(),
                        subject,
                        htmlContent
                );
                log.info("Email sent to: {}", sub.getEmail());
            } catch (MessagingException e) {
                log.error("Failed to send email to: {}", sub.getEmail(), e);
            }
        }
    }
}
