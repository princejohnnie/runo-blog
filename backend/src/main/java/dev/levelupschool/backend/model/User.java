package dev.levelupschool.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "public")
@Relation(collectionRelation = "items", itemRelation = "item")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String slug;

    private String password;

    private String avatarUrl;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Article> articles;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;

    @ManyToMany
    @JoinTable(
        name = "followers",
        joinColumns = @JoinColumn(name = "followed_id"),
        inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    public List<User> followers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "followers",
        joinColumns = @JoinColumn(name = "follower_id"),
        inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    public List<User> following = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "bookmarks",
        joinColumns = @JoinColumn(name = "bookmarker_id"),
        inverseJoinColumns = @JoinColumn(name = "bookmarked_id")
    )
    public List<Article> bookmarks = new ArrayList<>();

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.setPassword(password);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setAvatarUrl(String url) {
        this.avatarUrl = url;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password.getBytes(), BCrypt.gensalt());
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, name='%s', avatar='%s']", id, name, avatarUrl);
    }
}
