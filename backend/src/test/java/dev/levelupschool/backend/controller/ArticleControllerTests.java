package dev.levelupschool.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.levelupschool.backend.auth.AuthenticationProvider;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.Comment;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.CommentRepository;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.UpdateArticleRequest;
import dev.levelupschool.backend.service.ArticleService;
import dev.levelupschool.backend.service.UserService;
import dev.levelupschool.backend.storage.StorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

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
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private UserService userService;

    @Qualifier("AWSStorageService")
    @MockBean
    private StorageService storageService;

    @Autowired
    private ArticleService articleService;


    @Test
    void  contextLoads() {

    }


    @Test
    public void givenArticle_whenGetArticles_thenReturnJsonArray() throws Exception {
        var user = userRepository.save(new User("johndoe@gmail.com", "John Uzodinma", "password2"));

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
        var user = userRepository.save(new User("johndoe@gmail.com", "John Uzodinma", "password2"));

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
        var user = userRepository.save(new User("johndoe@gmail.com", "John Uzodinma", "password2"));

        var article = articleRepository.save(new Article( "LevelUp Article", "Luka is a great tutor", user));

        mockMvc.perform(
                get("/articles/{id}", article.getId())
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.author.name", is("John Uzodinma")));
    }

    @Test
    public void givenArticle_whenGetArticle_thenReturnCommentsArray() throws Exception {
        var user = userRepository.save(new User("johndoe@gmail.com", "John Uzodinma", "password2"));

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
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma", "password2"));
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(user);

        var createArticleRequest = new Article("LevelUp Article", "Luka is a great tutor", user);

        mockMvc.perform(
                post("/articles")
                    .param("title", createArticleRequest.getTitle())
                    .param("content", createArticleRequest.getContent())
            ).andExpect(status().isOk());

        Assertions.assertEquals(1, articleRepository.count()); // Confirm that article was actually stored in the DB
    }

    @Test
    public void givenUser_whenPostArticleWithCoverPhoto_thenStoreCoverPhoto() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma", "password2"));

        URL url = URI.create("http://mocked-avatar-url").toURL();
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(user);
        Mockito.when(storageService.store(Mockito.any(MultipartFile.class), Mockito.any(String.class))).thenReturn(url);

        var createArticleRequest = new Article("LevelUp Article", "Luka is a great tutor", user);

        MockMultipartFile coverPhoto = new MockMultipartFile(
            "cover", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "photo.jpg".getBytes());

        mockMvc.perform(
            multipart("/articles")
                .file(coverPhoto)
                .param("title", createArticleRequest.getTitle())
                .param("content", createArticleRequest.getContent())
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$.coverUrl", is("http://mocked-avatar-url")));

        var savedArticle = articleRepository.findAll().get(0);

        Assertions.assertNotNull(savedArticle.getCoverUrl());// Confirm that article cover was saved to model
    }

    @Test
    public void givenOwnerUser_whenPutArticle_thenUpdateArticle() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma", "password2"));
        URL url = URI.create("http://mocked-avatar-url").toURL();
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(user);
        Mockito.when(storageService.store(Mockito.any(MultipartFile.class), Mockito.any(String.class))).thenReturn(url);


        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var updateArticleRequest = new UpdateArticleRequest("LevelUp Article updated", "Luka is a great tutor. A fact which cannot be debated", "slug");
        var updateArticlePayload = objectMapper.writeValueAsString(updateArticleRequest);
        MockMultipartFile coverPhoto = new MockMultipartFile(
            "cover", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "photo.jpg".getBytes());

        mockMvc.perform(
                multipart("/articles")
                    .file(coverPhoto)
                    .param("title", updateArticleRequest.getTitle())
                    .param("content", updateArticleRequest.getContent())
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.coverUrl", is("http://mocked-avatar-url")));

        var savedArticle = articleRepository.findAll().get(0);

        Assertions.assertNotNull(savedArticle.getCoverUrl());// Confirm that article cover was saved to model

    }

    @Test
    public void giveDifferentUser_whenPutArticle_thenReturnUnauthorized() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma",  "password2"));

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        var updateArticleRequest = new UpdateArticleRequest("LevelUp Article updated", "Luka is a great tutor. A fact which cannot be debated", "slug");
        var updateArticlePayload = objectMapper.writeValueAsString(updateArticleRequest);

        mockMvc.perform(
                put("/articles/{id}", article.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateArticlePayload)
            ).andExpect(status().isForbidden());

        Assertions.assertEquals("LevelUp Article", articleRepository.findById(article.getId()).get().getTitle()); // Confirm that article was not updated in the DB
        Assertions.assertEquals("Luka is a great tutor", articleRepository.findById(article.getId()).get().getContent());
    }

    @Test
    public void givenOwnerUser_whenDeleteArticle_thenDeleteArticle() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma", "password2"));
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(user);

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        mockMvc.perform(
            delete("/articles/{id}", article.getId())
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, articleRepository.count(), "Count is 0 because article was actually deleted from DB");
    }

    @Test
    public void givenAnotherUser_whenDeleteArticle_thenReturnUnauthorized() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma",  "password2"));

        var article = articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        mockMvc.perform(
            delete("/articles/{id}", article.getId())
        ).andExpect(status().isForbidden());

        Assertions.assertEquals(1, articleRepository.count(), "Count is 1 because the Article should not be deleted by unauthenticated user");
    }

    @Test
    public void givenUser_whenDeleteUser_thenDeleteUserArticles() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma",  "password2"));
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(user);

        articleRepository.save(new Article("LevelUp Article", "Luka is a great tutor", user));

        mockMvc.perform(
            delete("/users/{id}", user.getId())
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, articleRepository.count(), "Count is 0 because user's articles were actually deleted from the DB");
    }

    @Test
    public void givenArticle_whenGetComments_thenReturnArticleCommentsArray() throws Exception {
        var user = userRepository.save(new User("john@gmail.com", "John Uzodinma",  "password"));

        var article = articleRepository.save(new Article("Test Article", "Test Article content", user));

        commentRepository.save(new Comment("Test content", user, article));

        mockMvc.perform(
                get("/articles/{id}/comments", article.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.items", hasSize(1)));
    }

    @Test
    public void givenArticle_whenBookmarkArticle_thenSaveBookmark() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma",  "password2"));
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(user);

        var article = articleRepository.save(new Article("Test title", "Test content", user));

        mockMvc.perform(
            post("/articles/{id}/bookmark", article.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        Assertions.assertEquals(1, user.getBookmarks().size());
    }

    @Test
    public void givenArticle_whenGetBookmarkers_thenReturnBookmarkersArray() throws Exception {
        var user = userService.createUser(new User("johnndoe@gmail.com", "John Uzodinma", "password2"));
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(user);

        var article = articleRepository.save(new Article("Test title", "Test content", user));
        articleService.bookmark(article.getId());

        mockMvc.perform(
            get("/articles/{id}/bookmarkers", article.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

}
