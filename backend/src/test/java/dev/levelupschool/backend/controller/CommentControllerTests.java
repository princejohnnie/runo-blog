package dev.levelupschool.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.Comment;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.CommentRepository;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.CreateCommentRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    private String firstUserToken;
    private String differentUserToken;

    @Test
    void  contextLoads() {

    }

    @BeforeEach
    public void setup() throws Exception {
        registerUser();
    }

    private void registerUser() throws Exception {
        User firstUser = new User("john@gmail.com", "John Uzodinma", "slug", "password");

        var payload = objectMapper.writeValueAsString(firstUser);

        MvcResult result = mockMvc.perform(
            post(("/register"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)).andDo(print()).andReturn();

        firstUserToken = "Bearer " + result.getResponse().getContentAsString();
    }

    private void registerDifferentUser() throws Exception {
        User secondUser = new User("luka@gmail.com", "Luka Papez", "slug", "password");

        var payload = objectMapper.writeValueAsString(secondUser);

        MvcResult result = mockMvc.perform(
            post(("/register"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)).andDo(print()).andReturn();

        differentUserToken = "Bearer " + result.getResponse().getContentAsString();
    }

    @Test
    public void givenComment_whenGetComments_thenReturnJsonArray() throws Exception {
        var user = userRepository.findAll().get(0); // Get saved User by registerUser()

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        commentRepository.save(new Comment("This is my first comment", user, article));

        mockMvc.perform(
                get("/comments")
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].content", is("This is my first comment")));
    }

    @Test
    public void givenComment_whenGetComment_thenReturnJson() throws Exception {
        var user = userRepository.findAll().get(0);

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var comment = commentRepository.save(new Comment("This is my first comment", user, article));

        mockMvc.perform(
                get("/comments/{id}", comment.getId())
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.content", is("This is my first comment")));
    }


    @Test
    public void givenComment_whenGetComment_thenReturnAuthorJson() throws Exception {
        var user = userRepository.findAll().get(0);

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var comment = commentRepository.save(new Comment("This is my first comment", user, article));

        mockMvc.perform(
                get("/comments/{id}", comment.getId())
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.author.name", is("John Uzodinma")));

    }

    @Test
    public void givenUser_whenPostComment_thenStoreComment() throws Exception {
        var user = userRepository.findAll().get(0);

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var createCommentRequest = new CreateCommentRequest("This is my first comment", article.getId());
        var payload = objectMapper.writeValueAsString(createCommentRequest);

        mockMvc.perform(
            post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", firstUserToken)
                .content(payload)
        ).andExpect(status().isOk());

        Assertions.assertEquals(1, commentRepository.count()); // Confirm that comment was actually stored in the DB
    }


    @Test
    public void givenOwnerUser_whenPutComment_thenUpdateComment() throws Exception {
        var user = userRepository.findAll().get(0);

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var createCommentRequest = new CreateCommentRequest("This is my first comment", article.getId());
        var createCommentPayload = objectMapper.writeValueAsString(createCommentRequest);

        // Create Comment by first User
        mockMvc.perform(
            post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", firstUserToken)
                .content(createCommentPayload)
        ).andExpect(status().isOk());

        // Get created Comment
        var comment = commentRepository.findAll().get(0);

        var updateCommentRequest = new Comment("This is my first updated comment", user, article);
        var updateCommentPayload = objectMapper.writeValueAsString(updateCommentRequest);

        // Update created comment by User
        mockMvc.perform(
                put("/comments/{id}", comment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", firstUserToken)
                    .content(updateCommentPayload)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.content", is("This is my first updated comment")));

        Assertions.assertEquals("This is my first updated comment", commentRepository.findById(comment.getId()).get().getContent()); // Confirm that comment was actually updated in the DB
    }

    @Test
    public void givenDifferentUser_whenPutComment_thenReturnUnauthorized() throws Exception {
        var user = userRepository.findAll().get(0); // Get first user

        registerDifferentUser(); // Register a different (second) user

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var createCommentRequest = new CreateCommentRequest("This is my first comment", article.getId());
        var createCommentPayload = objectMapper.writeValueAsString(createCommentRequest);

        // Create Comment by first User
        mockMvc.perform(
            post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", firstUserToken)
                .content(createCommentPayload)
        ).andExpect(status().isOk());

        var comment = commentRepository.findAll().get(0); // Get created Comment

        var updateCommentRequest = new Comment("This is my first updated comment", user, article);
        var updateCommentPayload = objectMapper.writeValueAsString(updateCommentRequest);

        // Try to update the article using a different User
        mockMvc.perform(
                put("/comments/{id}", comment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", differentUserToken) // NOTE: A different (second) User is trying to update first user's Comment
                    .content(updateCommentPayload)
            ).andExpect(status().isForbidden());

        Assertions.assertEquals("This is my first comment", commentRepository.findById(comment.getId()).get().getContent()); // Confirm that comment was not updated in the DB
    }

    @Test
    public void givenOwnerUser_whenDeleteComment_thenDeleteComment() throws Exception {
        var user = userRepository.findAll().get(0); // Get first user

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var createCommentRequest = new CreateCommentRequest("This is my first comment", article.getId());
        var commentPayload = objectMapper.writeValueAsString(createCommentRequest);

        // Create Comment
        mockMvc.perform(
            post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", firstUserToken)
                .content(commentPayload)
        ).andExpect(status().isOk());

        // Get created Comment
        var comment = commentRepository.findAll().get(0);

        // Delete Comment
        mockMvc.perform(
            delete("/comments/{id}", comment.getId())
                .header("Authorization", firstUserToken)
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, commentRepository.count()); // Confirm that comment no longer exists in the DB
    }

    @Test
    public void givenAnotherUser_whenDeleteComment_thenReturnUnauthorized() throws Exception {
        var user = userRepository.findAll().get(0); // Get first user

        registerDifferentUser();  // Register a different (second) user

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var createCommentRequest = new CreateCommentRequest("This is my first comment", article.getId());
        var commentPayload = objectMapper.writeValueAsString(createCommentRequest);

        // Create Comment by first User
        mockMvc.perform(
            post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", firstUserToken)
                .content(commentPayload)
        ).andExpect(status().isOk());

        var comment = commentRepository.findAll().get(0); // Get created Comment

        // Try to update the article using a different User
        mockMvc.perform(
            delete("/comments/{id}", comment.getId())
                .header("Authorization", differentUserToken) // NOTE: A different User is trying to delete Comment
        ).andExpect(status().isForbidden());

        Assertions.assertEquals(1, commentRepository.count()); // Confirm that comment was not deleted from the DB
    }

    @Test
    public void givenUser_whenDeleteUser_thenDeleteUserComments() throws Exception {
        var user = userRepository.findAll().get(0);

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var createCommentRequest = new CreateCommentRequest("This is my first comment", article.getId());
        var commentPayload = objectMapper.writeValueAsString(createCommentRequest);

        // Create Comment
        mockMvc.perform(
            post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", firstUserToken)
                .content(commentPayload)
        ).andExpect(status().isOk());

        Assertions.assertEquals(1, commentRepository.count()); // Confirm comment was created

        mockMvc.perform(
            delete("/users/{id}", user.getId())
                .header("Authorization", firstUserToken)
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, commentRepository.count()); // Confirm that user's comments were actually deleted from the DB
    }

    @Test
    public void givenArticle_whenDeleteArticle_thenDeleteArticleComments() throws Exception {
        var user = userRepository.findAll().get(0);

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var createCommentRequest = new CreateCommentRequest("This is my first comment", article.getId());
        var commentPayload = objectMapper.writeValueAsString(createCommentRequest);

        // Create Comment
        mockMvc.perform(
            post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", firstUserToken)
                .content(commentPayload)
        ).andExpect(status().isOk());

        Assertions.assertEquals(1, commentRepository.count()); // Confirm Comment was created

        mockMvc.perform(
            delete("/articles/{id}", article.getId())
                .header("Authorization", firstUserToken)
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, commentRepository.count()); // Confirm that article's comments were actually deleted from the DB
    }

}
