package dev.levelupschool.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "subscriptions", schema = "public")
@Relation(collectionRelation = "items", itemRelation = "item")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    private String subscriptionType;
    @CreationTimestamp
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private boolean isActive;

    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Subscription(String transactionId, String subscriptionType, boolean active, User user) {
        this.transactionId = transactionId;
        this.subscriptionType = subscriptionType;
        this.startDate = LocalDateTime.now();
        this.setEndDate(subscriptionType);
        this.isActive = active;
        this.setStatus(active);
        this.user = user;
    }


    public void setEndDate(String subscriptionType) {
        if (subscriptionType.equals("monthly")) {
            this.endDate = startDate.plusMonths(1);
        } else if (subscriptionType.equals("yearly")) {
            this.endDate = startDate.plusMonths(12);
        }
    }

    public void setActive(boolean active) {
        this.isActive = active;
        setStatus(active);
    }

    public void setStatus(boolean isActive) {
        if (isActive) {
            this.status = "active";
        }
        if (startDate.isBefore(endDate)) {
            this.status = "ended";
        }
        if (startDate.isBefore(endDate) && !isActive) {
            this.status = "cancelled";
        }
    }

}
