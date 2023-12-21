package dev.levelupschool.backend;

import com.github.javafaker.Faker;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.Comment;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.CommentRepository;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.service.SlugService;
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

            final String[] descriptions = {"Software Developer", "Designer", "Journalist", "Cloud Engineer"};

            private void populateDatabase() {
                for (int i = 1; i <= 12; i++) {
                    Faker faker = new Faker();
                    SlugService slug = new SlugService();
                    String name = faker.name().name();
                    String email = faker.internet().emailAddress();
                    String articleTitle = "Richird Norton photorealistic rendering as real photos";
                    String articleSlug = slug.makeSlug(articleTitle);

                    String articleContent = faker.lorem().paragraph(50);

                    String description = descriptions[i%4];
                    var user = new User(email, name, description, "password");
                    user.setSlug(slug.makeSlug(name));
                    userRepository.save(user);

                    var article = new Article(articleTitle, articleContent, user);
                    article.setSlug(articleSlug);
                    articleRepository.save(article);

                    for (int j = 0; j < 3; j++) {
                        String commentString = faker.lorem().paragraph(20);
                        commentRepository.save(new Comment(commentString, user, article));
                    }
                }
            }
        };
    }
}
