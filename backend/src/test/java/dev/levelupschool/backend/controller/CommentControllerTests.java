package dev.levelupschool.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.levelupschool.backend.model.Comment;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.CommentRepository;
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
public class CommentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void  contextLoads() {

    }

    @Test
    public void givenComment_whenGetComments_thenReturnJsonArray() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        var article = articleService.createArticle(new CreateArticleRequest(user.getId(), "LevelUp Article", "Luka is a great tutor"));

        commentService.createComment(new CreateCommentRequest(user.getId(), article.getId(), "This is my first comment"));

        mockMvc.perform(
                get("/comments")
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].content", is("This is my first comment")));
    }

    @Test
    public void givenComment_whenGetComment_thenReturnJson() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        var article = articleService.createArticle(new CreateArticleRequest(user.getId(), "LevelUp Article", "Luka is a great tutor"));

        var comment = commentService.createComment(new CreateCommentRequest(user.getId(), article.getId(), "This is my first comment"));

        mockMvc.perform(
                get("/comments/{id}", comment.getId())
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.content", is("This is my first comment")));
    }


    @Test
    public void givenComment_whenGetComment_thenReturnAuthorJson() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        var article = articleService.createArticle(new CreateArticleRequest(user.getId(), "LevelUp Article", "Luka is a great tutor"));

        var comment = commentService.createComment(new CreateCommentRequest(user.getId(), article.getId(), "This is my first comment"));

        mockMvc.perform(
                get("/comments/{id}", comment.getId())
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.author.name", is("John Uzodinma")));

    }

    @Test
    public void givenArticle_whenPostComment_thenStoreComment() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        var article = articleService.createArticle(new CreateArticleRequest(user.getId(), "LevelUp Article", "Luka is a great tutor"));

        var createCommentRequest = new CreateCommentRequest(user.getId(), article.getId(), "This is my first comment");

        var payload = objectMapper.writeValueAsString(createCommentRequest);

        mockMvc.perform(
            post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        ).andExpect(status().isOk());

        Assertions.assertEquals(1, commentRepository.count()); // Confirm that comment was actually stored in the DB
    }


    @Test
    public void givenComment_whenPutComment_thenUpdateComment() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        var article = articleService.createArticle(new CreateArticleRequest(user.getId(), "LevelUp Article", "Luka is a great tutor"));

        var comment = commentService.createComment(new CreateCommentRequest(user.getId(), article.getId(), "This is my first comment"));

        var updateCommentRequest = new Comment("This is my first updated comment", user, article);

        var payload = objectMapper.writeValueAsString(updateCommentRequest);

        mockMvc.perform(
                put("/comments/{id}", comment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.content", is("This is my first updated comment")));

        Assertions.assertEquals("This is my first updated comment", commentRepository.findById(comment.getId()).get().getContent()); // Confirm that comment was actually updated in the DB
    }

    @Test
    public void givenComment_whenDeleteComment_thenDeleteComment() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        var article = articleService.createArticle(new CreateArticleRequest(user.getId(), "LevelUp Article", "Luka is a great tutor"));

        var comment = commentService.createComment(new CreateCommentRequest(user.getId(), article.getId(), "This is my first comment"));

        mockMvc.perform(
            delete("/comments/{id}", comment.getId())
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, commentRepository.count()); // Confirm that comment no longer exists in the DB
    }

    @Test
    public void givenUser_whenDeleteUser_thenDeleteUserComments() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        var article = articleService.createArticle(new CreateArticleRequest(user.getId(), "LevelUp Article", "Luka is a great tutor"));

        commentService.createComment(new CreateCommentRequest(user.getId(), article.getId(), "This is my first comment"));

        mockMvc.perform(
            delete("/users/{id}", user.getId())
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, commentRepository.count()); // Confirm that user's comments were actually deleted from the DB
    }

    @Test
    public void givenArticle_whenDeleteArticle_thenDeleteArticleComments() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        var article = articleService.createArticle(new CreateArticleRequest(user.getId(), "LevelUp Article", "Luka is a great tutor"));

        commentService.createComment(new CreateCommentRequest(user.getId(), article.getId(), "This is my first comment"));

        mockMvc.perform(
            delete("/articles/{id}", article.getId())
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, commentRepository.count()); // Confirm that article's comments were actually deleted from the DB
    }

}
