package dev.levelupschool.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.UserRepository;
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
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void  contextLoads() {

    }


    @Test
    public void givenUser_whenGetUsers_thenReturnJsonArray() throws Exception {
        userService.createUser(new User("John Uzodinma"));

        mockMvc.perform(
            get("/users")
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("John Uzodinma")));

    }

    @Test
    public void givenUser_whenGetUser_thenReturnJson() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        mockMvc.perform(
                get("/users/{id}", user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("John Uzodinma")));

    }

    @Test
    public void givenUser_whenPostUser_thenStoreUser() throws Exception {
        var user = new User("John Uzodinma");
        var payload = objectMapper.writeValueAsString(user);

        mockMvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Uzodinma")));

        Assertions.assertEquals(1, userRepository.count()); // Confirm that user was actually stored in the DB
    }

    @Test
    public void givenUser_whenPutUser_thenUpdateUser() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        var updateUserRequest = new User("John Prince");
        var payload = objectMapper.writeValueAsString(updateUserRequest);

        mockMvc.perform(
                put("/users/{id}", user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            ).andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("John Prince")));

        Assertions.assertEquals("John Prince", userRepository.findById(user.getId()).get().getName()); // Confirm that user was actually updated in the DB
    }

    @Test
    public void givenUser_whenDeleteUser_thenDeleteUser() throws Exception {
        var user = userService.createUser(new User("John Uzodinma"));

        mockMvc.perform(
            delete("/users/{id}", user.getId())
        ).andExpect(status().isOk());

        Assertions.assertEquals(0, userRepository.count()); // Confirm that user was actually deleted from the DB
    }
}
