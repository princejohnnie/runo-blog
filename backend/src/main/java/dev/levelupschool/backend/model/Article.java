package dev.levelupschool.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.annotation.Nullable;
import org.springframework.hateoas.server.core.Relation;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "articles", schema = "public")
@Relation(collectionRelation = "items", itemRelation = "item")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;

    @Nullable
    private String coverUrl;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(
        name = "bookmarks",
        joinColumns = @JoinColumn(name = "bookmarked_id"),
        inverseJoinColumns = @JoinColumn(name = "bookmarker_id")
    )
    public List<User> bookmarkers = new ArrayList<>();

    public Article(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    @Override
    public String toString() {
        return String.format("Article[id=%d, user.id=%d, title='%s', content='%s']", id, author.getId(), title, content);
    }
}
