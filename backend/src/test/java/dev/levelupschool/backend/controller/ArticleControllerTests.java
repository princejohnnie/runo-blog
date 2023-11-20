package dev.levelupschool.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.request.CreateArticleRequest;
import dev.levelupschool.backend.request.CreateCommentRequest;
import dev.levelupschool.backend.service.ArticleService;
import dev.levelupschool.backend.service.CommentService;
import dev.levelupschool.backend.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
public class ArticleControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void  contextLoads() {

    }

    @Test
    public void givenArticle_whenGetArticles_thenReturnJsonArray() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        articleService.createArticle(new CreateArticleRequest(user.getId(), "LevelUp Article", "Luka is a great tutor"));

        mockMvc.perform(
                get("/articles")
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].title", is("LevelUp Article")))
            .andExpect(jsonPath("$[0].content", is("Luka is a great tutor")));
    }

    @Test
    public void givenArticle_whenGetArticle_thenReturnJson() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        var article = articleService.createArticle(new CreateArticleRequest(user.getId(), "LevelUp Article", "Luka is a great tutor"));

        mockMvc.perform(
                get("/articles/{id}", article.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is("LevelUp Article")))
            .andExpect(jsonPath("$.content", is("Luka is a great tutor")));
    }

    @Test
    public void givenArticle_whenGetArticle_thenReturnAuthorJson() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        var article = articleService.createArticle(new CreateArticleRequest(user.getId(), "LevelUp Article", "Luka is a great tutor"));

        mockMvc.perform(
                get("/articles/{id}", article.getId())
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.author.name", is("John Uzodinma")));
    }

    @Test
    public void givenArticle_whenGetArticle_thenReturnCommentsArray() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        var article = articleService.createArticle(new CreateArticleRequest(user.getId(), "LevelUp Article", "Luka is a great tutor"));

        commentService.createComment(new CreateCommentRequest(user.getId(), article.getId(), "This is my first comment"));

        mockMvc.perform(
                get("/articles/{id}", article.getId())
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.comments", hasSize(1)))
            .andExpect(jsonPath("$.comments[0].content", is("This is my first comment")));

    }

    @Test
    public void givenUser_whenPostArticle_thenStoreArticle() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        var createArticleRequest = new CreateArticleRequest(user.getId(),"LevelUp Article", "Luka is a great tutor");

        var payload = objectMapper.writeValueAsString(createArticleRequest);

        mockMvc.perform(
                post("/articles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            ).andExpect(status().isOk());

        Assertions.assertEquals(1, articleRepository.count()); // Confirm that article was actually stored in the DB
    }

    @Test
    public void givenArticle_whenPutArticle_thenUpdateArticle() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        var article = articleService.createArticle(new CreateArticleRequest(user.getId(),"LevelUp Article", "Luka is a great tutor"));

        var updateArticleRequest = new Article("LevelUp Article updated", "Luka is a great tutor. A fact which cannot be debated", user);

        var payload = objectMapper.writeValueAsString(updateArticleRequest);

        mockMvc.perform(
                put("/articles/{id}", article.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.title", is("LevelUp Article updated")))
            .andExpect(jsonPath("$.content", is("Luka is a great tutor. A fact which cannot be debated")));

        Assertions.assertEquals("LevelUp Article updated", articleRepository.findById(article.getId()).get().getTitle()); // Confirm that article was actually updated in the DB
        Assertions.assertEquals("Luka is a great tutor. A fact which cannot be debated", articleRepository.findById(article.getId()).get().getContent());
    }

    @Test
    public void givenArticle_whenDeleteArticle_thenDeleteArticle() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        var article = articleService.createArticle(new CreateArticleRequest(user.getId(),"LevelUp Article", "Luka is a great tutor"));

        mockMvc.perform(
            delete("/articles/{id}", article.getId())
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, articleRepository.count());
    }

    @Test
    public void givenUser_whenDeleteUser_thenDeleteUserArticles() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        articleService.createArticle(new CreateArticleRequest(user.getId(),"LevelUp Article", "Luka is a great tutor"));

        mockMvc.perform(
            delete("/users/{id}", user.getId())
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, articleRepository.count()); // Confirm that user's articles were actually deleted from the DB
    }

}
