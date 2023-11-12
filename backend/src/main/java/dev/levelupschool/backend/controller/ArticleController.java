package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.request.CreateArticleRequest;
import dev.levelupschool.backend.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// You can also enable CORS per controller
//@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;


    @GetMapping("/articles")
    List<Article> index() {
        return articleService.getAllArticles();
    }

    @GetMapping("/articles/{id}")
    Article show(@PathVariable Long id) {
        return articleService.getArticle(id);
    }

    @PostMapping("/articles")
    Article store(@RequestBody CreateArticleRequest request) {
        return articleService.createArticle(request);
    }

    @PutMapping("/articles/{id}")
    Article update(@RequestBody Article newArticle, @PathVariable Long id) {
        return articleService.updateArticle(newArticle, id);
    }

    @DeleteMapping("/articles/{id}")
    void delete(@PathVariable Long id) {
        articleService.deleteArticle(id);
    }
}
