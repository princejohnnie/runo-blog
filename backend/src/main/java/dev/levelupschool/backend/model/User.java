package dev.levelupschool.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.levelupschool.backend.enums.Provider;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
@Entity
@Table(name = "users", schema = "public")
@Relation(collectionRelation = "items", itemRelation = "item")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String description;

    private String slug;

    private String password;

    private String avatarUrl;

    private boolean isPremium;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Article> articles;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Subscription> subscriptions;

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

    public void setPremium(List<Subscription> subscriptions) {
        if (subscriptions != null && !subscriptions.isEmpty()) {
            this.isPremium = subscriptions.stream().anyMatch(subscription -> subscription.getUser().getId().equals(id) && subscription.isActive());
        }
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", name='" + name + '\'' +
            ", slug='" + slug + '\'' +
            ", password='" + password + '\'' +
            ", avatarUrl='" + avatarUrl + '\'' +
            ", isPremium=" + isPremium +
            ", articles=" + articles +
            ", comments=" + comments +
            ", subscriptions=" + subscriptions +
            ", followers=" + followers +
            ", following=" + following +
            ", bookmarks=" + bookmarks +
            '}';
    }
}
