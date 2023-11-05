package dev.levelupschool.backend;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CommentController {
    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final ArticleRepository articleRepository;

    CommentController(CommentRepository commentRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/comments")
    List<Comment> index() {
        return commentRepository.findAll();
    }

    @GetMapping("/comments/{id}")
    Comment show(@PathVariable Long id) {
        return commentRepository
            .findById(id)
            .orElseThrow(() -> new ModelNotFoundException(id));
    }

    @PostMapping("/comments")
    Comment store(@RequestBody Map<String, Object> request) {
        Long userId = ((Number) request.get("user_id")).longValue();
        Long articleId = ((Number) request.get("articleId")).longValue();
        String content = (String) request.get("content");

        return commentRepository.save(
            new Comment(
                content,
                userRepository.findById(userId).orElseThrow(),
                articleRepository.findById(articleId).orElseThrow()
            )
        );
    }

    @PutMapping("/comments/{id}")
    Comment update(@RequestBody Comment newComment, @PathVariable Long id) {
        return commentRepository
            .findById(id)
            .map(comment -> {
                comment.setContent(newComment.getContent());
                return commentRepository.save(comment);
            })
            .orElseThrow(() -> new ModelNotFoundException(id));
    }

    @DeleteMapping("/comments/{id}")
    void delete(@PathVariable Long id) {
        commentRepository.deleteById(id);
    }
}
