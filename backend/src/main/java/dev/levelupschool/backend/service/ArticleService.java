package dev.levelupschool.backend.service;

import dev.levelupschool.backend.auth.AuthenticationProvider;
import dev.levelupschool.backend.dtos.ArticleDto;
import dev.levelupschool.backend.dtos.CommentDto;
import dev.levelupschool.backend.dtos.UserDto;
import dev.levelupschool.backend.exception.CustomValidationException;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.UpdateArticleRequest;
import dev.levelupschool.backend.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.OperationNotSupportedException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleService {

    @Qualifier("AWSStorageService")
    @Autowired
    private StorageService storageService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SlugService slugService;

    @Autowired
    private AuthenticationProvider authProvider;

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
        User loggedInUser = authProvider.getAuthenticatedUser();
        article.setAuthor(loggedInUser);

        if (cover != null) {
            var coverUrl = storageService.store(cover, "covers/");
            article.setCoverUrl(coverUrl.toString());
        }

        article.setSlug(slugService.makeSlug(article.getTitle()));


        if (article.getContent().contains("Hate")) {
            throw new CustomValidationException("content", "Sorry, we do not support hate speech on our platform");
        }

        return new ArticleDto(articleRepository.save(article));
    }

    public ArticleDto updateArticle(UpdateArticleRequest newArticle, MultipartFile cover, Long id) throws AccessDeniedException {
        var loggedInUser = authProvider.getAuthenticatedUser();

        var article = articleRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(Article.class, id));

        if (!article.getAuthor().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You cannot edit an article not created by you!");
        }
        if (newArticle.getTitle() != null) {
        article.setTitle(newArticle.getTitle());
            article.setSlug(slugService.makeSlug(newArticle.getTitle()));

        }
        if (newArticle.getContent() != null) {
            article.setContent(newArticle.getContent());
        }
        article.setUpdatedAt(LocalDateTime.now());
        if (cover != null) {
            var coverUrl = storageService.store(cover, "covers/");
            article.setCoverUrl(coverUrl.toString());
        }

        return new ArticleDto(articleRepository.save(article));
    }

    public void deleteArticle(Long id) throws AccessDeniedException {
        var loggedInUser = authProvider.getAuthenticatedUser();

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
        response.put("items", article.getComments().stream().sorted((o1, o2) -> o2.getUpdatedAt().compareTo(o1.getUpdatedAt())).map(CommentDto::new).toList());

        return response;
    }

    public void bookmark(Long articleId) throws Exception {
        var loggedInUser = authProvider.getAuthenticatedUser();

        var article = articleRepository.findById(articleId).orElseThrow(() -> new ModelNotFoundException(Article.class, articleId));

        if (loggedInUser.bookmarks.contains(article)) {
            throw new OperationNotSupportedException("Article already added to bookmarks");
        }

        loggedInUser.bookmarks.add(article);

        userRepository.save(loggedInUser);

    }

    public List<UserDto> bookmarkers(Long id) throws Exception {
        var loggedInUser = authProvider.getAuthenticatedUser();

        var article = articleRepository.findById(id).orElseThrow(() -> new ModelNotFoundException(Article.class, id));

        if (!loggedInUser.getId().equals(article.getAuthor().getId())) {
            throw new OperationNotSupportedException("You cannot see another user article's bookmarks");
        }

        return article.bookmarkers.stream().map(UserDto::new).toList();
    }
}
