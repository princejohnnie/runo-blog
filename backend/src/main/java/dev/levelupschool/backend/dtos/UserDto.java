package dev.levelupschool.backend.dtos;

import dev.levelupschool.backend.model.User;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "items")
public class UserDto {
    public Long id;
    public String name;
    public String email;
    public String avatarUrl;

    public String slug;
    public boolean isPremium;
    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.avatarUrl = user.getAvatarUrl();
        this.slug = user.getSlug();
        this.isPremium = user.isPremium();
    }
}
