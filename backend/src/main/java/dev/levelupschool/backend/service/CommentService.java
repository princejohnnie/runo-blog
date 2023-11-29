package dev.levelupschool.backend.service;

import dev.levelupschool.backend.auth.AuthenticationUtils;
import dev.levelupschool.backend.dtos.CommentDto;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.Article;
import dev.levelupschool.backend.model.Comment;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.CommentRepository;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.CreateCommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private PagedResourcesAssembler<CommentDto> pagedResourcesAssembler;

    public PagedModel<EntityModel<CommentDto>> getAllComments(Pageable paging) {
        var result = commentRepository.findAll(paging).map(CommentDto::new);
        return pagedResourcesAssembler.toModel(result);
    }

    public CommentDto getComment(Long id) {
        return new CommentDto(commentRepository
            .findById(id)
            .orElseThrow(() -> new ModelNotFoundException(Comment.class, id)));
    }

    public CommentDto createComment(CreateCommentRequest request) {
        User loggedInUser = AuthenticationUtils.getLoggedInUser(userRepository);

        Article article = articleRepository.findById(request.getArticleId())
            .orElseThrow(() -> new ModelNotFoundException(Article.class, request.getArticleId()));

        Comment comment = new Comment(request.getContent(), loggedInUser, article);

        return new CommentDto(commentRepository.save(comment));
    }

    public CommentDto updateComment(Comment newComment, Long id) throws Exception {
        var loggedInUser = AuthenticationUtils.getLoggedInUser(userRepository);

        var comment = commentRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(Comment.class, id));

        if (!comment.getAuthor().getId().equals(loggedInUser.getId())) {
            throw new AccessDeniedException("You cannot edit a comment not created by you!");
        }

        comment.setContent(newComment.getContent());

        return new CommentDto(commentRepository.save(comment));
    }

    public void deleteComment(Long id) throws Exception {
        var loggedInUser = AuthenticationUtils.getLoggedInUser(userRepository);

        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new ModelNotFoundException(Comment.class, id));

        if (!loggedInUser.getId().equals(comment.getAuthor().getId())) {
            throw new AccessDeniedException("You cannot delete an article not created by you!");
        }

        commentRepository.deleteById(id);
    }
}
