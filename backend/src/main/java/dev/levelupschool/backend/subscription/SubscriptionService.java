package dev.levelupschool.backend.subscription;

import dev.levelupschool.backend.model.Subscription;
import dev.levelupschool.backend.model.User;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubscriptionService {
    boolean subscribe(User user, JSONObject jsonObject, String subscriptionType);

    List<SubscriptionDto> getSubscriptions(User user);
    boolean cancelSubscription(Long subscriptionId, User user);


}
