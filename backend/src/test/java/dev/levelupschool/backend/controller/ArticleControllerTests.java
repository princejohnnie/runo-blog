package dev.levelupschool.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.Comment;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.CommentRepository;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.UpdateArticleRequest;
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
public class ArticleControllerTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

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
        var firstUser = new User("john@gmail.com", "John Uzodinma", "slug", "password");

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
    public void givenArticle_whenGetArticles_thenReturnJsonArray() throws Exception {
        var user = userRepository.findAll().get(0);

        articleRepository.save(new Article( "LevelUp Article", "Luka is a great tutor", user));

        mockMvc.perform(
                get("/articles")
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.items", hasSize(1)))
            .andExpect(jsonPath("$._embedded.items[0].title", is("LevelUp Article")))
            .andExpect(jsonPath("$._embedded.items[0].content", is("Luka is a great tutor")));
    }

    @Test
    public void givenArticle_whenGetArticle_thenReturnJson() throws Exception {
        User user = userRepository.findAll().get(0);

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        mockMvc.perform(
                get("/articles/{id}", article.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is("LevelUp Article")))
            .andExpect(jsonPath("$.content", is("Luka is a great tutor")));
    }

    @Test
    public void givenArticle_whenGetArticle_thenReturnAuthorJson() throws Exception {
        User user = userRepository.findAll().get(0);

        var article = articleRepository.save(new Article( "LevelUp Article", "Luka is a great tutor", user));

        mockMvc.perform(
                get("/articles/{id}", article.getId())
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.author.name", is("John Uzodinma")));
    }

    @Test
    public void givenArticle_whenGetArticle_thenReturnCommentsArray() throws Exception {
        User user = userRepository.findAll().get(0);

        var article = articleRepository.save(new Article( "LevelUp Article", "Luka is a great tutor", user));

        commentRepository.save(new Comment("This is my first comment", user, article));

        mockMvc.perform(
                get("/articles/{id}", article.getId())
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.comments", hasSize(1)))
            .andExpect(jsonPath("$.comments[0].content", is("This is my first comment")));
    }

    @Test
    public void givenUser_whenPostArticle_thenStoreArticle() throws Exception {
        var user = userRepository.findAll().get(0); // Get saved User by registerFirstUser()

        var createArticleRequest = new Article("LevelUp Article", "Luka is a great tutor", user);

        var payload = objectMapper.writeValueAsString(createArticleRequest);

        mockMvc.perform(
                post("/articles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", firstUserToken)
                    .content(payload)
            ).andExpect(status().isOk());

        Assertions.assertEquals(1, articleRepository.count()); // Confirm that article was actually stored in the DB
    }

    @Test
    public void givenOwnerUser_whenPutArticle_thenUpdateArticle() throws Exception {
        var user = userRepository.findAll().get(0); // Get saved User

        var createArticleRequest = new Article("LevelUp Article", "Luka is a great tutor", user);
        var articlePayload = objectMapper.writeValueAsString(createArticleRequest);

        // Create article by first user
        mockMvc.perform(
            post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", firstUserToken)
                .content(articlePayload)
        ).andExpect(status().isOk());

        // Get created article
        Article article = articleRepository.findAll().get(0);

        var updateArticleRequest = new UpdateArticleRequest("LevelUp Article updated", "Luka is a great tutor. A fact which cannot be debated");
        var updateArticlePayload = objectMapper.writeValueAsString(updateArticleRequest);

        // Update created article by first user
        mockMvc.perform(
                put("/articles/{id}", article.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", firstUserToken)
                    .content(updateArticlePayload)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is("LevelUp Article updated")))
            .andExpect(jsonPath("$.content", is("Luka is a great tutor. A fact which cannot be debated")));

        Assertions.assertEquals("LevelUp Article updated", articleRepository.findById(article.getId()).get().getTitle()); // Confirm that article was actually updated in the DB
        Assertions.assertEquals("Luka is a great tutor. A fact which cannot be debated", articleRepository.findById(article.getId()).get().getContent());
    }

    @Test
    public void giveDifferentUser_whenPutArticle_thenReturnUnauthorized() throws Exception {
        registerDifferentUser();

        var user = userRepository.findAll().get(0); // Get registered User

        var createArticleRequest = new Article("LevelUp Article", "Luka is a great tutor", user);
        var createArticlePayload = objectMapper.writeValueAsString(createArticleRequest);

        // Create article by first user
        mockMvc.perform(
            post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", firstUserToken)
                .content(createArticlePayload)
        ).andExpect(status().isOk());

        // Get created article
        Article article = articleRepository.findAll().get(0);

        var updateArticleRequest = new UpdateArticleRequest("LevelUp Article updated", "Luka is a great tutor. A fact which cannot be debated");
        var updateArticlePayload = objectMapper.writeValueAsString(updateArticleRequest);

        // Try to update the article by a different User
        mockMvc.perform(
                put("/articles/{id}", article.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", differentUserToken) // NOTE: A different (second) User is trying to Update Article
                    .content(updateArticlePayload)
            ).andExpect(status().isForbidden());

        Assertions.assertEquals("LevelUp Article", articleRepository.findById(article.getId()).get().getTitle()); // Confirm that article was not updated in the DB
        Assertions.assertEquals("Luka is a great tutor", articleRepository.findById(article.getId()).get().getContent());
    }

    @Test
    public void givenOwnerUser_whenDeleteArticle_thenDeleteArticle() throws Exception {
        var user = userRepository.findAll().get(0); // Get saved User by registerFirstUser()

        var createArticleRequest = new Article("LevelUp Article", "Luka is a great tutor", user);
        var articlePayload = objectMapper.writeValueAsString(createArticleRequest);

        // Create Article
        mockMvc.perform(
            post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", firstUserToken)
                .content(articlePayload)
        ).andExpect(status().isOk());

        // Get created article
        Article article = articleRepository.findAll().get(0);

        // Delete article by user
        mockMvc.perform(
            delete("/articles/{id}", article.getId())
                .header("Authorization", firstUserToken)
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, articleRepository.count()); // Confirm that article was actually deleted
    }

    @Test
    public void givenAnotherUser_whenDeleteArticle_thenReturnUnauthorized() throws Exception {
        var user = userRepository.findAll().get(0); // Get created User by registerUser()

        registerDifferentUser(); // Create a different User

        var createArticleRequest = new Article("LevelUp Article", "Luka is a great tutor", user);
        var articlePayload = objectMapper.writeValueAsString(createArticleRequest);

        // Create Article by first User
        mockMvc.perform(
            post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", firstUserToken)
                .content(articlePayload)
        ).andExpect(status().isOk());

        // Get created article
        Article article = articleRepository.findAll().get(0);

        // Try to delete the article by a different User
        mockMvc.perform(
            delete("/articles/{id}", article.getId())
                .header("Authorization", differentUserToken) // NOTE: A different User is trying to delete Article
        ).andExpect(status().isForbidden());

        Assertions.assertEquals(1, articleRepository.count()); // Confirm that the Article was not deleted from the DB
    }

    @Test
    public void givenUser_whenDeleteUser_thenDeleteUserArticles() throws Exception {
        var user = userRepository.findAll().get(0);

        var createArticleRequest = new Article("LevelUp Article", "Luka is a great tutor", user);
        var articlePayload = objectMapper.writeValueAsString(createArticleRequest);

        mockMvc.perform(
            post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", firstUserToken)
                .content(articlePayload)
        ).andExpect(status().isOk());

        Assertions.assertEquals(1, articleRepository.count()); // Confirm article was created

        mockMvc.perform(
            delete("/users/{id}", user.getId())
                .header("Authorization", firstUserToken)
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, articleRepository.count()); // Confirm that user's articles were actually deleted from the DB
    }

    @Test
    public void givenArticle_whenGetComments_thenReturnArticleCommentsArray() throws Exception {
        var user = userRepository.save(new User("john@gmail.com", "John Uzodinma", "Software Developer", "password"));

        var article = articleRepository.save(new Article("Test Article", "Test Article content", user));

        commentRepository.save(new Comment("Test content", user, article));

        mockMvc.perform(
                get("/articles/{id}/comments", article.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.items", hasSize(1)));
    }

}
