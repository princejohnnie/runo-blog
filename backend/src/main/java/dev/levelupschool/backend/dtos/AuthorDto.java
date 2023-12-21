package dev.levelupschool.backend.dtos;

import dev.levelupschool.backend.model.User;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "items")
public class AuthorDto {
    public Long id;
    public String name;
    public String description;
    public String avatarUrl;

    public AuthorDto(User author) {
        this.id = author.getId();
        this.name = author.getName();
        this.description = author.getDescription();
        this.avatarUrl = author.getAvatarUrl();
    }
}
