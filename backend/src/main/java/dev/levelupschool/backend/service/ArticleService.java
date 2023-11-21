package dev.levelupschool.backend.service;

import dev.levelupschool.backend.auth.AuthenticationUtils;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article getArticle(Long id) {
        return articleRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(Article.class, id));
    }

    public Article createArticle(Article article) {
        User loggedInUser = AuthenticationUtils.getLoggedInUser(userRepository);

        article.setAuthor(loggedInUser);

        return articleRepository.save(article);
    }

    public Article updateArticle(Article newArticle, Long id) throws AccessDeniedException {
        var loggedInUser = AuthenticationUtils.getLoggedInUser(userRepository);

        var article = articleRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(Article.class, id));

        if (!article.getAuthor().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You cannot edit an article not created by you!");
        }

        article.setTitle(newArticle.getTitle());
        article.setContent(newArticle.getContent());

        return articleRepository.save(article);
    }

    public void deleteArticle(Long id) throws AccessDeniedException {
        var loggedInUser = AuthenticationUtils.getLoggedInUser(userRepository);

        var article = articleRepository
            .findById(id)
            .orElseThrow(() -> new ModelNotFoundException(Article.class, id));

        if (!loggedInUser.getId().equals(article.getAuthor().getId())) {
            throw new AccessDeniedException("You cannot delete an article not created by you!");
        }

        articleRepository.deleteById(id);
    }
}
