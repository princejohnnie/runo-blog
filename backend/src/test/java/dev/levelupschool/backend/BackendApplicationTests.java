package dev.levelupschool.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.Comment;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.CommentRepository;
import dev.levelupschool.backend.repository.UserRepository;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BackendApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void givenArticle_whenGetArticles_thenReturnJsonArray() throws Exception {
        var user = new User("john@gmail.com", "John Uzodinma",  "password");
        userRepository.save(user);

        var article = new Article("test title 1", "test content 1", user);

        articleRepository.save(article);

        mvc.perform(
                get("/articles")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.items", hasSize(1)))
            .andExpect(jsonPath("$._embedded.items[0].title", is("test title 1")));
    }

    @Test
    public void givenComment_whenGetArticle_thenReturnCommentsArray() throws Exception {
        var user = new User("john@gmail.com", "John Uzodinma", "password");
        userRepository.save(user);

        var article = articleRepository.save(new Article("test title", "test content 1", user));

        commentRepository.save(new Comment("test comment", user, article));

        mvc.perform(
                get("/articles/{id}", article.getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.comments", hasSize(1)));
    }

    @Test
    public void givenArticle_whenPostComment_thenStoreComment() throws Exception {
        User user = new User("john@gmail.com", "John Uzodinma",  "password");

        var userPayload = objectMapper.writeValueAsString(user);

        MvcResult result = mvc.perform(
            post(("/register"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(userPayload)).andDo(print()).andReturn();

        var registeredUser = userRepository.findAll().get(0);

        String token = "Bearer " + result.getResponse().getContentAsString();

        var article = articleRepository.save(new Article("test title", "test content 1", registeredUser));

        var payloadDto = new CommentCreationDto();
        payloadDto.setUserId(user.getId());
        payloadDto.setArticleId(article.getId());
        payloadDto.setContent("some content");


        var payload = objectMapper.writeValueAsString(payloadDto);

        mvc.perform(
                post("/comments")
                    .header("Authorization", token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
            )
            .andExpect(status().isOk());

        // VERY IMPORTANT TO CHECK THAT THINGS WERE ACTUALLY STORED
        Assertions.assertEquals(1, commentRepository.count());
    }

    /**
     * TODO: your homework here will be to add tests covering the expected scenarios for all methods
     * <p>
     * Please note that the expected outcome is different for every method.
     * - For GET   methods, the expected outcome is successful status + correct thing returned from the API
     * - For POST  methods, the expected outcome is successful status + something stored in the database
     * - For PATCH methods, the expected outcome is successful status + data updated in the database
     */


    private static class CommentCreationDto {
        public Long userId;
        public Long articleId;
        public String content;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getArticleId() {
            return articleId;
        }

        public void setArticleId(Long articleId) {
            this.articleId = articleId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
