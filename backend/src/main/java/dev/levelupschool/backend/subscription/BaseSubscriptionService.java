package dev.levelupschool.backend.subscription;

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
                subscriptionRepository.save(new Subscription(transactionId, subscriptionType, "active", user));

                List<Subscription> subscriptions = subscriptionRepository.findByUserId(user.getId());

                user.setPremium(subscriptions);

                userRepository.save(user);

                return true;
            }

        }
        return false;
    }

    @Override
    public List<SubscriptionDto> getSubscriptions(User user) {
        return subscriptionRepository.findByUserId(user.getId()).stream().map(SubscriptionDto::new).toList();
    }
}
