package dev.levelupschool.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.levelupschool.backend.auth.AuthenticationProvider;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.Comment;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.CommentRepository;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.CreateCommentRequest;
import dev.levelupschool.backend.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CommentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private UserService userService;

    @Test
    void  contextLoads() {

    }


    @Test
    public void givenComment_whenGetComments_thenReturnJsonArray() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma", "slug", "password2"));

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        commentRepository.save(new Comment("This is my first comment", user, article));

        mockMvc.perform(
                get("/comments")
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.items", hasSize(1)))
            .andExpect(jsonPath("$._embedded.items[0].content", is("This is my first comment")));
    }

    @Test
    public void givenComment_whenGetComment_thenReturnJson() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma", "slug", "password2"));

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var comment = commentRepository.save(new Comment("This is my first comment", user, article));

        mockMvc.perform(
                get("/comments/{id}", comment.getId())
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.content", is("This is my first comment")));
    }


    @Test
    public void givenComment_whenGetComment_thenReturnAuthorJson() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma", "slug", "password2"));

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var comment = commentRepository.save(new Comment("This is my first comment", user, article));

        mockMvc.perform(
                get("/comments/{id}", comment.getId())
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.author.name", is("John Uzodinma")));

    }

    @Test
    public void givenUser_whenPostComment_thenStoreComment() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma", "slug", "password2"));
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(user);

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var createCommentRequest = new CreateCommentRequest("This is my first comment", article.getId());
        var payload = objectMapper.writeValueAsString(createCommentRequest);

        mockMvc.perform(
            post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        ).andExpect(status().isOk());

        Assertions.assertEquals(1, commentRepository.count()); // Confirm that comment was actually stored in the DB
    }


    @Test
    public void givenOwnerUser_whenPutComment_thenUpdateComment() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma", "slug", "password2"));
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(user);

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var comment = commentRepository.save(new Comment("This is my first comment", user, article));

        var updateCommentRequest = new CreateCommentRequest("This is my first updated comment", article.getId());
        var updateCommentPayload = objectMapper.writeValueAsString(updateCommentRequest);

        mockMvc.perform(
                put("/comments/{id}", comment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateCommentPayload)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.content", is("This is my first updated comment")));

        Assertions.assertEquals("This is my first updated comment", commentRepository.findById(comment.getId()).get().getContent()); // Confirm that comment was actually updated in the DB
    }

    @Test
    public void givenDifferentUser_whenPutComment_thenReturnUnauthorized() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma", "slug", "password2"));

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var comment = commentRepository.save(new Comment("This is my first comment", user, article));

        var updateCommentRequest = new CreateCommentRequest("This is my first updated comment", article.getId());
        var updateCommentPayload = objectMapper.writeValueAsString(updateCommentRequest);

        mockMvc.perform(
                put("/comments/{id}", comment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateCommentPayload)
            ).andExpect(status().isForbidden());

        Assertions.assertEquals("This is my first comment", commentRepository.findById(comment.getId()).get().getContent()); // Confirm that comment was not updated in the DB
    }

    @Test
    public void givenOwnerUser_whenDeleteComment_thenDeleteComment() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma", "slug", "password2"));
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(user);

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var comment = commentRepository.save(new Comment("This is my first comment", user, article));

        // Delete Comment
        mockMvc.perform(
            delete("/comments/{id}", comment.getId())
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, commentRepository.count()); // Confirm that comment no longer exists in the DB
    }

    @Test
    public void givenAnotherUser_whenDeleteComment_thenReturnUnauthorized() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma", "slug", "password2"));

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var comment = commentRepository.save(new Comment("This is my first comment", user, article));

        // Try to update the article using a different User
        mockMvc.perform(
            delete("/comments/{id}", comment.getId())
        ).andExpect(status().isForbidden());

        Assertions.assertEquals(1, commentRepository.count()); // Confirm that comment was not deleted from the DB
    }

    @Test
    public void givenUser_whenDeleteUser_thenDeleteUserComments() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma", "slug", "password2"));
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(user);

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        commentRepository.save(new Comment("This is my first comment", user, article));

        mockMvc.perform(
            delete("/users/{id}", user.getId())
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, commentRepository.count(), "Count is 0 to confirm that user's comments were actually deleted from the DB");
    }

    @Test
    public void givenArticle_whenDeleteArticle_thenDeleteArticleComments() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma", "slug", "password2"));
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(user);

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        commentRepository.save(new Comment("This is my first comment", user, article));

        Assertions.assertEquals(1, commentRepository.count(), "Count is 1 to confirm Comment was created");

        // Delete article
        mockMvc.perform(
            delete("/articles/{id}", article.getId())
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, commentRepository.count(), "Count is 0 to confirm article's comments were actually deleted from the DB");
    }

}
