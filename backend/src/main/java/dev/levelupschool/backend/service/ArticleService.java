package dev.levelupschool.backend.service;

import dev.levelupschool.backend.auth.AuthenticationProvider;
import dev.levelupschool.backend.dtos.ArticleDto;
import dev.levelupschool.backend.dtos.CommentDto;
import dev.levelupschool.backend.dtos.UserDto;
import dev.levelupschool.backend.exception.CustomValidationException;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.Category;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.CategoryRepository;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.UpdateArticleRequest;
import dev.levelupschool.backend.storage.StorageService;
import dev.levelupschool.backend.utils.StringCutter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.OperationNotSupportedException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private CategoryRepository categoryRepository;

    @Autowired
    private SlugService slugService;

    @Autowired
    private AuthenticationProvider authProvider;

    @Autowired
    private PagedResourcesAssembler<ArticleDto> pagedResourcesAssembler;

    public PagedModel<EntityModel<ArticleDto>> getAllArticles(Pageable paging) {
        var loggedInUser = authProvider.getAuthenticatedUser();
        Page<Article> result = articleRepository.findAll(paging);

        if (loggedInUser == null || !loggedInUser.isPremium()) {
            result.forEach(article -> {
                if (article.isPremium()) {
                    article.setContent(StringCutter.truncate(article.getContent()));
                }
            });
            return pagedResourcesAssembler.toModel(result.map(ArticleDto::new));
        }

        return pagedResourcesAssembler.toModel(result.map(ArticleDto::new));
    }

    public PagedModel<EntityModel<ArticleDto>> getPremiumArticles(Pageable paging) throws CustomValidationException {
        var loggedInUser = authProvider.getAuthenticatedUser();

        if (loggedInUser == null || !loggedInUser.isPremium()) {
            throw new CustomValidationException("message", "Sorry, you are not a Premium User! Upgrade to Premium to view Premium Articles");
        }

        List<ArticleDto> articleDtos = articleRepository.findAll(paging).stream().filter(Article::isPremium).map(ArticleDto::new).toList();

        PageRequest pageRequest = PageRequest.of(0, articleDtos.size());
        PageImpl<ArticleDto> page = new PageImpl<>(articleDtos, pageRequest, articleDtos.size());

        return pagedResourcesAssembler.toModel(page);

    }

    public ArticleDto getArticle(Long id) {
        var article = articleRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(Article.class, id));

        var loggedInUser = authProvider.getAuthenticatedUser();

        if (loggedInUser == null || !loggedInUser.isPremium()) {
            if (article.isPremium()) {
                article.setContent(StringCutter.truncate(article.getContent()));
            }
            return new ArticleDto(article);
        }

        return new ArticleDto(article);

    }

    public ArticleDto createArticle(Article article, MultipartFile cover) throws CustomValidationException {
        User loggedInUser = authProvider.getAuthenticatedUser();
        article.setAuthor(loggedInUser);
        article.setSlug(slugService.makeSlug(article.getTitle()));

        if (cover != null) {
            var coverUrl = storageService.store(cover, "covers/");
            article.setCoverUrl(coverUrl.toString());
        }

        if (!loggedInUser.isPremium() && article.isPremium()) {
            throw new CustomValidationException("content", "Sorry, you cannot create a Premium Article");
        }

        if (article.getContent().contains("Hate")) {
            throw new CustomValidationException("content", "Sorry, we do not support hate speech on our platform");
        }

        return new ArticleDto(articleRepository.save(article));
    }

    public ArticleDto updateArticle(UpdateArticleRequest newArticle, MultipartFile cover, Long id) throws AccessDeniedException {
        var loggedInUser = authProvider.getAuthenticatedUser();

        var article = articleRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(Article.class, id));

        List<Category> existingCategories = new ArrayList<>();
        newArticle.getCategories().forEach(category -> {
            Category newCategory = categoryRepository.findById(category).get();
            if (newCategory != null) {
                existingCategories.add(newCategory);
            }
        });

        article.setCategories(existingCategories);

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
