package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.auth.AuthenticationUtils;
import dev.levelupschool.backend.dtos.ArticleDto;
import dev.levelupschool.backend.dtos.CommentDto;
import dev.levelupschool.backend.dtos.UserDto;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.UpdateUserRequest;
import dev.levelupschool.backend.service.TokenService;
import dev.levelupschool.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/users")
    PagedModel<EntityModel<UserDto>> index(
        @PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = {"name"}) Pageable paging) {
        return userService.getAllUsers(paging);
    }

    @GetMapping("/users/{id}")
    UserDto show(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}")
    ResponseEntity<Object> update(@RequestBody @Valid UpdateUserRequest newUser, @PathVariable Long id) {
        try {
            var user = userService.updateUser(newUser, id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/users/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/register")
    ResponseEntity<?> register(@RequestBody @Valid User newUser) {
        try {
            var createdUser = userService.createUser(newUser);
            return ResponseEntity.ok(tokenService.generateToken(createdUser.getId()));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }


    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        var user = userRepository.findByEmail(loginDto.getEmail());

        if (user == null) {
            throw new ModelNotFoundException(User.class, loginDto.email);
        }

        if (BCrypt.checkpw(loginDto.getPassword(), user.getPassword())) {
            return ResponseEntity.ok(tokenService.generateToken(user.getId()));
        } else {
            return new ResponseEntity<>("Incorrect Password", HttpStatus.UNAUTHORIZED);
        }

//        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/me")
    User me() {
        return AuthenticationUtils.getLoggedInUser(userRepository);
    }

    @GetMapping("/users/{id}/articles")
    Map<String, List<ArticleDto>> articles(@PathVariable Long id) {
        return userService.getUserArticles(id);
    }

    @GetMapping("/users/{id}/comments")
    Map<String, List<CommentDto>> comments(@PathVariable Long id) {
        return userService.getUserComments(id);
    }


    @Getter
    @Setter
    private static class LoginDto {
        private String email;
        private String password;
    }
}
