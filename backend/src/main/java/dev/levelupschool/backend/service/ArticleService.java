package dev.levelupschool.backend.service;

import dev.levelupschool.backend.auth.AuthenticationUtils;
import dev.levelupschool.backend.dtos.ArticleDto;
import dev.levelupschool.backend.dtos.CommentDto;
import dev.levelupschool.backend.exception.CustomValidationException;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleService {

    @Qualifier("fileSystemStorageService")
    @Autowired
    private StorageService storageService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PagedResourcesAssembler<ArticleDto> pagedResourcesAssembler;

    public PagedModel<EntityModel<ArticleDto>> getAllArticles(Pageable paging) {
        var result = articleRepository.findAll(paging).map(ArticleDto::new);
        return pagedResourcesAssembler.toModel(result);
    }

    public ArticleDto getArticle(Long id) {
        return new ArticleDto(articleRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(Article.class, id)));
    }

    public ArticleDto createArticle(Article article, MultipartFile cover) throws CustomValidationException {
        User loggedInUser = AuthenticationUtils.getLoggedInUser(userRepository);
        article.setAuthor(loggedInUser);

        if (cover != null) {
            var coverUrl = storageService.store(cover);
            article.setCoverUrl(coverUrl.toString());
        }

        if (article.getContent().contains("Hate")) {
            throw new CustomValidationException("content", "Sorry, we do not support hate speech on our platform");
        }

        return new ArticleDto(articleRepository.save(article));
    }

    public ArticleDto updateArticle(Article newArticle, Long id) throws AccessDeniedException {
        var loggedInUser = AuthenticationUtils.getLoggedInUser(userRepository);

        var article = articleRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(Article.class, id));

        if (!article.getAuthor().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You cannot edit an article not created by you!");
        }

        article.setTitle(newArticle.getTitle());
        article.setContent(newArticle.getContent());

        return new ArticleDto(articleRepository.save(article));
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

    public Map<String, List<CommentDto>> getArticleComments(Long articleId) {
        var article = articleRepository
            .findById(articleId)
            .orElseThrow(() -> new ModelNotFoundException(Article.class, articleId));

        if (article.getComments() == null) {
            return null;
        }

        Map<String, List<CommentDto>> response = new HashMap<>();
        response.put("items", article.getComments().stream().map(CommentDto::new).toList());

        return response;
    }
}
