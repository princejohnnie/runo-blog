package dev.levelupschool.backend.service;

import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.Comment;
import dev.levelupschool.backend.repository.ArticleRepository;
import dev.levelupschool.backend.repository.CommentRepository;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.CreateCommentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment getComment(Long id) {
        return commentRepository
            .findById(id)
            .orElseThrow(() -> new ModelNotFoundException(id));
    }

    public Comment createComment(CreateCommentRequest request) {
        Long userId = request.getUserId();
        Long articleId = request.getArticleId();
        String content = request.getContent();

        return commentRepository.save(
            new Comment(
                content,
                userRepository.findById(userId).orElseThrow(() -> new ModelNotFoundException(userId)),
                articleRepository.findById(articleId).orElseThrow(() -> new ModelNotFoundException(articleId))
            )
        );
    }

    public Comment updateComment(Comment newComment, Long id) {
        return commentRepository
            .findById(id)
            .map(comment -> {
                comment.setContent(newComment.getContent());
                return commentRepository.save(comment);
            })
            .orElseThrow(() -> new ModelNotFoundException(id));
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
