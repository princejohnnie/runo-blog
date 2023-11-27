package dev.levelupschool.backend;

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
                for (int i = 0; i < 11; i++) {
                    var user = userRepository.save(new User("John Uzodinma"));

                    var article1 = articleRepository.save(new Article("Richird Norton photorealistic rendering as real photos", "Seamlessly syndicate cutting-edge architectures rather than collaborative collaboration and idea-sharing. Proactively incubate visionary interfaces whereas premium benefits." +
                        " Seamlessly negotiate ubiquitous leadership skills rather than parallel ideas. Dramatically visualize superior interfaces for best-of-breed alignments. Synergistically formulate performance based users through customized relationships. Interactively deliver cross-platform ROI via granular systems. " +
                        "Intrinsicly enhance effective initiatives vis-a-vis orthogonal outsourcing. Rapidiously monetize market-driven opportunities with multifunctional users. Collaboratively enhance visionary opportunities through revolutionary schemas. Progressively network just in time customer service without real-time scenarios." +
                        "Synergistically drive e-business leadership with unique synergy. Compellingly seize market positioning ROI and bricks-and-clicks e-markets. Proactively myocardinate timely platforms through distributed ideas. Professionally optimize enabled core competencies for leading-edge sources. Professionally enhance stand-alone " +
                        "leadership with innovative synergy. Rapidiously generate backend experiences vis-a-vis long-term high-impact relationships. Authoritatively supply market-driven mindshare and bricks-and-clicks opportunities. Holisticly create diverse innovation through adaptive communities. Efficiently empower seamless meta-services with impactful opportunities." +
                        " Distinctively transition virtual outsourcing with focused e-tailers.“ Monotonectally seize superior mindshare rather than efficient technology. Compellingly enhance seamless resources through competitive content. Continually actualize 24/365 alignments for resource-leveling platforms. Energistically enhance high standards in models and professional expertise. " +
                        "Intrinsicly iterate extensible metrics for prospective opportunities. Continually develop leading-edge experiences through quality e-services.", user));
                    articleRepository.save(new Article("Dream destinations to visit this year in Paris", "Lorem ipsum dolor sit amet consectetur adipisicing elit. Debitis, eveniet consequatur eos quod harum totam nulla repudiandae nemo saepe soluta vero quo, repellendus dicta sed minima amet ut recusandae maxime.", user));

                    commentRepository.save(new Comment("Synergistically drive e-business leadership with unique synergy. Compellingly seize market positioning ROI and bricks-and-clicks e-markets. Proactively myocardinate timely platforms through distributed ideas. Professionally optimize enabled core competencies for leading-edge sources. " +
                        "Professionally enhance stand-alone leadership with innovative synergy. Rapidiously ", user, article1));
                }
            }
        };
    }
}
