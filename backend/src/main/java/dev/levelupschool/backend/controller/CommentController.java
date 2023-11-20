package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.model.Comment;
import dev.levelupschool.backend.request.CreateCommentRequest;
import dev.levelupschool.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/comments")
    List<Comment> index() {
        return commentService.getAllComments();
    }

    @GetMapping("/comments/{id}")
    Comment show(@PathVariable Long id) {
        return commentService.getComment(id);
    }

    @PostMapping("/comments")
    Comment store(@RequestBody CreateCommentRequest request) {
        return commentService.createComment(request);
    }

    @PutMapping("/comments/{id}")
    Comment update(@RequestBody Comment newComment, @PathVariable Long id) {
        return commentService.updateComment(newComment, id);
    }

    @DeleteMapping("/comments/{id}")
    void delete(@PathVariable Long id) {
        commentService.deleteComment(id);
    }
}
