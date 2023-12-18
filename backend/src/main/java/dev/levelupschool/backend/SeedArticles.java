package dev.levelupschool.backend;

import com.github.javafaker.Faker;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.Comment;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.CommentRepository;
import dev.levelupschool.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class SeedArticles {
    private static final Logger log = LoggerFactory.getLogger(SeedArticles.class);

    @Bean
    CommandLineRunner init(
        ArticleRepository articleRepository,
        CommentRepository commentRepository,
        UserRepository userRepository
    ) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                if (articleRepository.count() == 0) {
                    log.info("Seeding articles");

                    populateDatabase();

                }
            }

            private void populateDatabase() {
                for (int i = 0; i < 12; i++) {
                    Faker faker = new Faker();
                    String name = faker.name().name();

                    String articleTitle = "Richird Norton photorealistic rendering as real photos";

                    String articleContent = faker.lorem().paragraph(50);

                    var user = userRepository.save(new User("john@gmail.com", name, "slug", "password"));

                    var article = articleRepository.save(new Article(articleTitle, articleContent, user));

                    for (int j = 0; j < 3; j++) {
                        String commentString = faker.lorem().paragraph(20);
                        commentRepository.save(new Comment(commentString, user, article));
                    }
                }
            }
        };
    }
}
