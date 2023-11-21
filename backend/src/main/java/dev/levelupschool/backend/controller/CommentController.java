package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.model.Comment;
import dev.levelupschool.backend.request.CreateCommentRequest;
import dev.levelupschool.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ResponseEntity<Object> update(@RequestBody Comment newComment, @PathVariable Long id) {
        try {
            var comment = commentService.updateComment(newComment, id);
            return new ResponseEntity<>(comment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/comments/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
