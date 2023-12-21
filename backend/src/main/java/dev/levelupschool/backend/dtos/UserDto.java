package dev.levelupschool.backend.dtos;

import dev.levelupschool.backend.model.Subscription;
import dev.levelupschool.backend.model.User;
import lombok.Data;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Relation(collectionRelation = "items")
public class UserDto {
    public Long id;
    public String name;
    public String email;
    public String avatarUrl;

    public String slug;
    public boolean isPremium;
    public Subscription currentSubscription;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.avatarUrl = user.getAvatarUrl();
        this.slug = user.getSlug();
        this.isPremium = user.isPremium();
        if (user.getSubscriptions() != null && !user.getSubscriptions().stream().filter(Subscription::isActive).toList().isEmpty()) {
            currentSubscription = user.getSubscriptions().stream().filter(Subscription::isActive).toList().get(0);
        }
    }
}
