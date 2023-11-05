package dev.levelupschool.backend;

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

                    var user = userRepository.save(new User("John Uzodinma"));

                    var article1 = articleRepository.save(new Article("test title 1", "test content 1", user));
                    articleRepository.save(new Article("test title 2", "test content 2", user));

                    commentRepository.save(new Comment("test comment", user, article1));

                }
            }
        };
    }
}
