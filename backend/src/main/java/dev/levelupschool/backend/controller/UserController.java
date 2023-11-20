package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.auth.AuthenticationProvider;
import dev.levelupschool.backend.dtos.ArticleDto;
import dev.levelupschool.backend.dtos.BookmarkDto;
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
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @GetMapping("/users")
    PagedModel<EntityModel<UserDto>> index(
        @PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = {"name"}) Pageable paging) {
        return userService.getAllUsers(paging);
    }

    @GetMapping("/users/{id}")
    UserDto show(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping("/user/upload-avatar")
    ResponseEntity<?> upload(@RequestParam(value = "avatar")MultipartFile avatar) {
        try {
            Map<String, String> response = userService.uploadAvatar(avatar);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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

    }

    @GetMapping("/me")
    UserDto me() {
        var loggedInUser = authenticationProvider.getAuthenticatedUser();
        return new UserDto(loggedInUser);
    }

    @GetMapping("/users/{id}/articles")
    Map<String, List<ArticleDto>> articles(@PathVariable Long id) {
        return userService.getUserArticles(id);
    }

    @GetMapping("/users/{id}/comments")
    Map<String, List<CommentDto>> comments(@PathVariable Long id) {
        return userService.getUserComments(id);
    }

    @GetMapping("/users/{id}/followers")
    List<UserDto> followers(@PathVariable Long id) {
        return userService.getFollowers(id);
    }

    @GetMapping("/users/{id}/following")
    List<UserDto> following(@PathVariable Long id) {
        return userService.getFollowing(id);
    }

    @PostMapping("/users/{id}/follow")
    ResponseEntity<?> follow(@PathVariable Long id) {
        try {
            userService.followUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/users/{id}/unfollow")
    ResponseEntity<?> unfollow(@PathVariable Long id) {
        try {
            userService.unfollowUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/bookmarks")
    ResponseEntity<?> bookmarks() {
        try {
            List<BookmarkDto> bookmarks = userService.getBookmarks();
            return new ResponseEntity<>(bookmarks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @Getter
    @Setter
    private static class RegisterDto {
        private String email;
        private String name;
        private String slug;
        private String password;
    }

    @Getter
    @Setter
    private static class LoginDto {
        private String email;
        private String password;
    }
}
