package dev.levelupschool.backend.dtos;

import dev.levelupschool.backend.model.User;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "items")
@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String avatarUrl;

    private String slug;
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
