package dev.levelupschool.backend.service;

import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.CreateArticleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            .orElseThrow(() -> new ModelNotFoundException(id));
    }

    public Article createArticle(CreateArticleRequest request) {
        Long userId = request.getUserId();
        String title = request.getTitle();
        String content = request.getContent();

        return articleRepository.save(
            new Article(
                title,
                content,
                userRepository.findById(userId).orElseThrow(() -> new ModelNotFoundException(userId))
            )
        );
    }

    public Article updateArticle(Article newArticle, Long id) {
        return articleRepository.findById(id)
            .map(article -> {
                article.setTitle(newArticle.getTitle());
                article.setContent(newArticle.getContent());
                return articleRepository.save(article);
            }).orElseThrow(() -> new ModelNotFoundException(id));
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }
}
