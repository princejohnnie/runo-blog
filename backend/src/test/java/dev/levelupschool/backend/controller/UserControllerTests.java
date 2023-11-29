package dev.levelupschool.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.Comment;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.CommentRepository;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.CreateCommentRequest;
import dev.levelupschool.backend.service.TokenService;
import dev.levelupschool.backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    private String firstUserToken;
    private String differentUserToken;

    @Test
    void  contextLoads() {

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
    public void givenUser_whenGetUsers_thenReturnJsonArray() throws Exception {
        userService.createUser(new User("john@gmail.com", "John Uzodinma", "slug", "password"));

        mockMvc.perform(
            get("/users")
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.items", hasSize(1)))
            .andExpect(jsonPath("$._embedded.items[0].name", is("John Uzodinma")));

    }

    @Test
    public void givenUser_whenGetUser_thenReturnJson() throws Exception {
        var user = userService.createUser(new User("john@gmail.com", "John Uzodinma", "slug", "password"));

        mockMvc.perform(
                get("/users/{id}", user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("John Uzodinma")));

    }

    @Test
    public void givenUser_whenRegisterUser_thenStoreUser() throws Exception {
        var user = new User("john@gmail.com", "John Uzodinma", "slug", "password");
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
        var user = userService.createUser(new User("john@gmail.com", "John Uzodinma", "slug", "password"));

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
    public void givenOwnerUser_whenPutUser_thenUpdateUser() throws Exception {
        registerUser(); // Register new user to get Token

        var user = userRepository.findAll().get(0); // Get registered user

        var updateUserRequest = new User("johndoe@gmail.com", "John Prince", "slug", "password2");
        var payload = objectMapper.writeValueAsString(updateUserRequest);

        mockMvc.perform(
                put("/users/{id}", user.getId())
                    .header("Authorization", firstUserToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("John Prince")));

        Assertions.assertEquals("John Prince", userRepository.findById(user.getId()).get().getName()); // Confirm that user was actually updated in the DB
    }

    @Test
    public void givenDifferentUser_whenPutUser_thenReturnUnauthorized() throws Exception {
        registerUser(); // Register new user to get token
        registerDifferentUser(); // Register a different user to get second token

        var user = userRepository.findAll().get(0); // Get registered user

        var updateUserRequest = new User("john@gmail.com", "John Prince", "slug", "password2");
        var payload = objectMapper.writeValueAsString(updateUserRequest);

        mockMvc.perform(
                put("/users/{id}", user.getId())
                    .header("Authorization", differentUserToken) // NOTE: A different (second) User is trying to Update first user
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            ).andExpect(status().isForbidden());

        Assertions.assertEquals("John Uzodinma", userRepository.findById(user.getId()).get().getName()); // Confirm that user was not updated in the DB
    }

    @Test
    public void givenOwnerUser_whenDeleteUser_thenDeleteUser() throws Exception {
        registerUser();

        var user = userRepository.findAll().get(0);

        mockMvc.perform(
            delete("/users/{id}", user.getId())
                .header("Authorization", firstUserToken)
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, userRepository.count()); // Confirm that user was actually deleted from the DB
    }

    @Test
    public void givenDifferentUser_whenDeleteUser_thenReturnUnauthorized() throws Exception {
        registerUser();
        registerDifferentUser();

        var user = userRepository.findAll().get(0);

        // Try to update the user using a different User
        mockMvc.perform(
            delete("/users/{id}", user.getId())
                .header("Authorization", differentUserToken) // NOTE: A different (second) User is trying to Update First User
        ).andExpect(status().isForbidden());

        Assertions.assertEquals(2, userRepository.count()); // Confirm that user was not deleted from the DB. Count is 2 because we registered two users
    }

    @Test
    public void givenUser_whenGetArticles_thenReturnUserArticlesArray() throws Exception {
        var user = userRepository.save(new User("john@gmail.com", "John Uzodinma", "Software Developer", "password"));

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
        var user = userRepository.save(new User("john@gmail.com", "John Uzodinma", "slug", "password"));

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

    @Getter
    @Setter
    @AllArgsConstructor
    private static class LoginDto {
        private String email;
        private String password;
    }
}
