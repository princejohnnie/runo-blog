package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.dtos.CommentDto;
import dev.levelupschool.backend.model.Comment;
import dev.levelupschool.backend.request.CreateCommentRequest;
import dev.levelupschool.backend.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/comments")
    PagedModel<EntityModel<CommentDto>> index(
        @PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = {"author"}) Pageable paging) {
        return commentService.getAllComments(paging);
    }

    @GetMapping("/comments/{id}")
    CommentDto show(@PathVariable Long id) {
        return commentService.getComment(id);
    }

    @PostMapping("/comments")
    CommentDto store(@RequestBody @Valid CreateCommentRequest request) {
        return commentService.createComment(request);
    }

    @PutMapping("/comments/{id}")
    ResponseEntity<Object> update(@RequestBody @Valid Comment newComment, @PathVariable Long id) {
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
