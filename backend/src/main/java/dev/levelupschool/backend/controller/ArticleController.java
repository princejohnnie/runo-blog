package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.dtos.ArticleDto;
import dev.levelupschool.backend.dtos.CommentDto;
import dev.levelupschool.backend.dtos.UserDto;
import dev.levelupschool.backend.exception.CustomValidationException;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.request.UpdateArticleRequest;
import dev.levelupschool.backend.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

// You can also enable CORS per controller
//@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/articles")
    PagedModel<EntityModel<ArticleDto>> index(
        @PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = {"title", "author"}) Pageable paging) {
        return articleService.getAllArticles(paging);
    }

    @GetMapping("/articles/{id}")
    ArticleDto show(@PathVariable Long id) {
        return articleService.getArticle(id);
    }

    @PostMapping("/articles")
    ResponseEntity<Object> store(
        @ModelAttribute @Valid Article article,
        @RequestParam(value = "cover", required = false) MultipartFile cover) {
        try {
            var articleDto = articleService.createArticle(article, cover);
            return new ResponseEntity<>(articleDto, HttpStatus.OK);
        } catch (CustomValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/articles/{id}")
    ResponseEntity<Object> update(
        @ModelAttribute @Valid UpdateArticleRequest newArticle,
        @RequestParam(value = "cover", required = false) MultipartFile cover,
        @PathVariable Long id) {
        try {
            var article = articleService.updateArticle(newArticle, cover, id);
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

    @GetMapping("/articles/{id}/comments")
    Map<String, List<CommentDto>> comments(@PathVariable Long id) {
        return articleService.getArticleComments(id);
    }

    @PostMapping("/articles/{id}/bookmark")
    ResponseEntity<?> bookmark(@PathVariable Long id) {
        try {
            articleService.bookmark(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/articles/{id}/bookmarkers")
    ResponseEntity<?> bookmarkers(@PathVariable Long id) {
        try {
            List<UserDto> users = articleService.bookmarkers(id);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

}
