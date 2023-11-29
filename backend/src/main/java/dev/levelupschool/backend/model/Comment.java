package dev.levelupschool.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "comments", schema = "public")
@Relation(collectionRelation = "items", itemRelation = "item")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    @JsonBackReference
    private Article article;

    public Comment(String content, User author, Article article) {
        this.content = content;
        this.author = author;
        this.article = article;
    }

    @Override
    public String toString() {
        return String.format("Comment[id=%d, user.id=%d, article.id=%d, content='%s']", id, author.getId(), article.getId(), content);
    }
}
