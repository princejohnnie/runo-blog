package dev.levelupschool.backend.dtos;

import dev.levelupschool.backend.model.User;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "items")
public class AuthorDto {
    public Long id;
    public String name;

    public AuthorDto(User author) {
        this.id = author.getId();
        this.name = author.getName();
    }
}
