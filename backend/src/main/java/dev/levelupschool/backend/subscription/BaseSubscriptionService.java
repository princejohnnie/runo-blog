package dev.levelupschool.backend.subscription;

import dev.levelupschool.backend.auth.AuthenticationProvider;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.Subscription;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.SubscriptionRepository;
import dev.levelupschool.backend.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseSubscriptionService implements SubscriptionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public boolean subscribe(User user, JSONObject jsonObject, String subscriptionType) {

        if (jsonObject.has("status")) {
            String status = jsonObject.getString("status");
            String transactionId = jsonObject.getString("txRef");

            if (status.equals("success")) {
                subscriptionRepository.save(new Subscription(transactionId, subscriptionType, true, user));

                List<Subscription> subscriptions = subscriptionRepository.findByUserId(user.getId());

                user.setPremium(subscriptions);

                userRepository.save(user);

                return true;
            }

        }
        return false;
    }

    @Override
    public List<Subscription> getSubscriptions(User user) {
        return subscriptionRepository.findByUserId(user.getId());
    }

    @Override
    public boolean cancelSubscription(Long subscriptionId, User user) {
        var subscriptions = subscriptionRepository.findByUserId(user.getId());

        for (Subscription subscription : subscriptions) {
            if (subscription.getId().equals(subscriptionId)) {
                subscription.setActive(false);
                var canceledSubscription = subscriptionRepository.save(subscription);
                return !canceledSubscription.isActive();
            }
        }
        return false;
    }
}
