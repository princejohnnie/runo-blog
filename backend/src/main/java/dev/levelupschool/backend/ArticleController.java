package dev.levelupschool.backend;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// You can also enable CORS per controller
//@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RestController
public class ArticleController {
    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    ArticleController(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/articles")
    List<Article> index() {
        return articleRepository.findAll();
    }

    @GetMapping("/articles/{id}")
    Article show(@PathVariable Long id) {
        return articleRepository
            .findById(id)
            .orElseThrow(() -> new ModelNotFoundException(id));
    }

    @PostMapping("/articles")
    Article store(@RequestBody Map<String, Object> request) {
        Long userId = ((Number) request.get("user_id")).longValue();
        String title = (String) request.get("title");
        String content = (String) request.get("content");

        return articleRepository.save(
            new Article(
                title,
                content,
                userRepository.findById(userId).orElseThrow()
            )
        );
    }

    @PutMapping("/articles/{id}")
    Article update(@RequestBody Article newArticle, @PathVariable Long id) {
        return articleRepository
            .findById(id)
            .map(article -> {
                article.setContent(newArticle.getContent());
                article.setTitle(newArticle.getTitle());
                return articleRepository.save(article);
            })
            .orElseThrow(() -> new ModelNotFoundException(id));
    }

    @DeleteMapping("/articles/{id}")
    void delete(@PathVariable Long id) {
        articleRepository.deleteById(id);
    }
}
