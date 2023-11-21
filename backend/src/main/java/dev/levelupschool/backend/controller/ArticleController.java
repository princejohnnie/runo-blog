package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    Article store(@RequestBody Article article) {
        return articleService.createArticle(article);
    }

    @PutMapping("/articles/{id}")
    ResponseEntity<Object> update(@RequestBody Article newArticle, @PathVariable Long id) {
        try {
            var article = articleService.updateArticle(newArticle, id);
            return new ResponseEntity<>(article, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/articles/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            articleService.deleteArticle(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
