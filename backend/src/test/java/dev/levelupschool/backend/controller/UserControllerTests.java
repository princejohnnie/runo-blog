package dev.levelupschool.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.levelupschool.backend.auth.AuthenticationProvider;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.Comment;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.CommentRepository;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.service.TokenService;
import dev.levelupschool.backend.service.UserService;
import dev.levelupschool.backend.storage.StorageService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URL;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private SecurityFilterChain securityFilterChain;

    @Qualifier("AWSStorageService")
    @MockBean
    private StorageService storageService;


    @Test
    void  contextLoads() {

    }


    @Test
    public void givenUser_whenGetUsers_thenReturnJsonArray() throws Exception {
        userService.createUser(new User("john@gmail.com", "John Uzodinma",  "password"));

        mockMvc.perform(
            get("/users")
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.items", hasSize(1)))
            .andExpect(jsonPath("$._embedded.items[0].name", is("John Uzodinma")));

    }

    @Test
    public void givenUser_whenGetUser_thenReturnJson() throws Exception {
        var user = userService.createUser(new User("john@gmail.com", "John Uzodinma",  "password"));

        mockMvc.perform(
                get("/users/{id}", user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("John Uzodinma")));

    }

    @Test
    public void givenUser_whenRegisterUser_thenStoreUser() throws Exception {
        var user = new User("john@gmail.com", "John Uzodinma",  "password");
        var payload = objectMapper.writeValueAsString(user);

        mockMvc.perform(
            post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        ).andExpect(status().isOk());

        Assertions.assertEquals(1, userRepository.count()); // Confirm that user was actually stored in the DB
    }

    @Test
    public void givenUser_whenLoginUser_thenReturnToken() throws Exception {
        var user = userService.createUser(new User("john@gmail.com", "John Uzodinma",  "password"));

        var loginUserRequest = new LoginDto(user.getEmail(), "password"); // Login with created user credentials
        var payload = objectMapper.writeValueAsString(loginUserRequest);

        ResultActions resultActions = mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        ).andExpect(status().isOk());

        String returnedToken = resultActions.andReturn().getResponse().getContentAsString();
        TokenService.TokenData tokenData = tokenService.validateToken(returnedToken);

        User loggedInUser = userRepository.findById(tokenData.getUserId()).get();

        Assertions.assertEquals(loggedInUser.getEmail(), user.getEmail()); // Confirm that the Token returned actually belongs to the logged in user
        Assertions.assertEquals(loggedInUser.getName(), user.getName());
    }

    @Test
    public void givenAuthenticatedUser_whenPutUser_thenUpdateUser() throws Exception {
        var authenticatedUser = userService.createUser(new User("johndoe@gmail.com", "John Uzodinma",  "password2"));
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(authenticatedUser);

        var updateUserRequest = new User("johnnydoe@gmail.com", "John Prince",  "password2");
        var payload = objectMapper.writeValueAsString(updateUserRequest);

        mockMvc.perform(
                put("/users/{id}", authenticatedUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("John Prince")));

        Assertions.assertEquals("John Prince", userRepository.findById(authenticatedUser.getId()).get().getName()); // Confirm that user was actually updated in the DB
    }

    @Test
    public void givenUnauthenticatedUser_whenPutUser_thenReturnUnauthorized() throws Exception {
        var authenticatedUser = userService.createUser(new User("johndoe@gmail.com", "John Uzodinma",  "password2"));

        var unAuthenticatedUser = userService.createUser(new User("johnnydoe@gmail.com", "John Prince",  "password"));

        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(authenticatedUser);

        var payload = objectMapper.writeValueAsString(authenticatedUser);

        mockMvc.perform(
                put("/users/{id}", unAuthenticatedUser.getId()) // NOTE: Unauthenticated user is trying to update authenticated user
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            ).andExpect(status().isForbidden());

        Assertions.assertEquals("John Uzodinma", userRepository.findById(authenticatedUser.getId()).get().getName()); // Confirm that user was not updated in the DB
    }

    @Test
    public void givenAuthenticatedUser_whenDeleteUser_thenDeleteUser() throws Exception {
        var authenticatedUser = userService.createUser(new User("johndoe@gmail.com", "John Uzodinma",  "password2"));

        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(authenticatedUser);

        mockMvc.perform(
            delete("/users/{id}", authenticatedUser.getId())
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, userRepository.count()); // Confirm that user was actually deleted from the DB
    }

    @Test
    public void givenUnauthenticatedUser_whenDeleteUser_thenReturnUnauthorized() throws Exception {
        var authenticatedUser = userService.createUser(new User("johndoe@gmail.com", "John Uzodinma",  "password2"));

        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(authenticatedUser);

        var unAuthenticatedUser = userService.createUser(new User("johnnydoe@gmail.com", "John Prince",  "password"));

        mockMvc.perform(
            delete("/users/{id}", unAuthenticatedUser.getId()) // NOTE: Authenticated User is trying to delete unauthenticated User
        ).andExpect(status().isForbidden());

        Assertions.assertEquals(2, userRepository.count(), "Count is 2 because we created two users"); // Confirm that user was not deleted from the DB.
    }

    @Test
    public void givenUser_whenGetArticles_thenReturnUserArticlesArray() throws Exception {
        var user = userRepository.save(new User("john@gmail.com", "John Uzodinma",  "password"));

        articleRepository.save(new Article("Levelup Article", "Levelup is a wonderful internship", user));

        // Get User articles
        mockMvc.perform(
            get("/users/{id}/articles", user.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.items", hasSize(1)))
            .andExpect(jsonPath("$.items[0].author.id", is(user.getId().intValue())));
    }

    @Test
    public void givenUser_whenGetComments_thenReturnUserCommentsArray() throws Exception {
        var user = userRepository.save(new User("john@gmail.com", "John Uzodinma",  "password"));

        var article = articleRepository.save(new Article( "LevelUp Article", "Luka is a great tutor", user));

        commentRepository.save(new Comment("This is a test comment", user, article));

        // Get User comments
        mockMvc.perform(
                get("/users/{id}/comments", user.getId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.items", hasSize(1)))
            .andExpect(jsonPath("$.items[0].author.id", is(user.getId().intValue())));
    }

    @Test
    public void givenUser_whenPostAvatar_thenReturnAvatar() throws Exception {
        var authenticatedUser = userService.createUser(new User("johnny@gmail.com", "Johnny Walker",  "password"));

        URL url = URI.create("http://mocked-avatar-url").toURL();

        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(authenticatedUser);
        Mockito.when(storageService.store(Mockito.any(MultipartFile.class), Mockito.any(String.class))).thenReturn(url);

        MockMultipartFile avatar = new MockMultipartFile(
            "avatar", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "photo.jpg".getBytes());

        mockMvc.perform(
                multipart("/user/upload-avatar")
                    .file(avatar)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.avatarUrl", is("http://mocked-avatar-url")));

        Assertions.assertEquals(authenticatedUser.getAvatarUrl(), "http://mocked-avatar-url");
    }

    @Test
    public void givenUser_whenGetFollowers_thenReturnFollowersArray() throws Exception {
        var loggedInUser = userRepository.save(new User("amaka@gmail.com", "Amaka Uzodinma", "password"));

        var followee = userRepository.save(new User("john@gmail.com", "John Uzodinma",  "password"));

        loggedInUser.getFollowers().add(followee);

        userRepository.save(loggedInUser);

        mockMvc.perform(
            get("/users/{id}/followers", loggedInUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    public void givenUser_whenGetFollowing_thenReturnFollowingArray() throws Exception {
        var mainUser = userRepository.save(new User("amaka@gmail.com", "Amaka Uzodinma", "password"));

        var followee = userRepository.save(new User("john@gmail.com", "John Uzodinma", "password"));

        mainUser.getFollowing().add(followee);

        userRepository.save(mainUser);

        mockMvc.perform(
            get("/users/{id}/following", mainUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    @Transactional
    public void givenAuthenticatedUser_whenFollowAnotherUser_thenFollowUser() throws Exception {
        var follower = userRepository.save(new User("john@gmail.com", "Johnny Doe",  "password"));

        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(follower);

        var followee = userRepository.save(new User("john@gmail.com", "Johnny Doe",  "password"));

        mockMvc.perform(
            post("/users/{id}/follow", followee.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        Assertions.assertEquals(follower.getFollowing().size(), 1); // Confirm that User was followed

    }

    @Test
    @Transactional
    public void givenAuthenticatedUser_whenUnfollowAnotherUser_thenUnfollowUser() throws Exception {
        var follower = userRepository.save(new User("john@gmail.com", "Johnny Doe",  "password"));
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(follower);

        var followee = userRepository.save(new User("johnny@gmail.com", "Johnny Doe", "password"));

        userService.followUser(followee.getId());

        Assertions.assertEquals(follower.getFollowing().size(), 1, "Count is 1 because followee was followed by follower");

        mockMvc.perform(
            post("/users/{id}/unfollow", followee.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        Assertions.assertEquals(follower.getFollowing().size(), 0, "Count is 0 because followee waS unfollowed by follower");
    }

    @Test
    @Transactional
    public void givenUser_whenGetBookmarks_thenReturnBookmarksArray() throws Exception {
        var loggedInUser = userRepository.save(new User("john@gmail.com", "Johnny Doe",  "password"));
        Mockito.when(authenticationProvider.getAuthenticatedUser()).thenReturn(loggedInUser);

        var article = articleRepository.save(new Article("Test title", "Test content", loggedInUser));
        loggedInUser.bookmarks.add(article);

        articleRepository.save(article);

        mockMvc.perform(
                get("/user/bookmarks")
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class LoginDto {
        private String email;
        private String password;
    }
}
