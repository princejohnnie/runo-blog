package dev.levelupschool.backend.subscription;

import dev.levelupschool.backend.model.Subscription;
import dev.levelupschool.backend.model.User;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Relation(collectionRelation = "items")
public class SubscriptionDto {
    public String transactionId;
    public String subscriptionType;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public boolean isActive;
    public String status;
    public SubscriptionDto(Subscription subscription) {
        this.transactionId = subscription.getTransactionId();
        this.subscriptionType = subscription.getSubscriptionType();
        this.startDate = subscription.getStartDate();
        this.endDate = subscription.getEndDate();
        this.isActive = subscription.isActive();
        this.status = subscription.getStatus();
    }
}
