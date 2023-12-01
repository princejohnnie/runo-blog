package dev.levelupschool.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;
import org.springframework.security.crypto.bcrypt.BCrypt;

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
    public List<User> followers;

    @ManyToMany
    @JoinTable(
        name = "followers",
        joinColumns = @JoinColumn(name = "follower_id"),
        inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    public List<User> following;

    public User(String email, String name, String slug, String password) {
        this.email = email;
        this.name = name;
        this.slug = slug;
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

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password.getBytes(), BCrypt.gensalt());
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, name='%s']", id, name);
    }
}
