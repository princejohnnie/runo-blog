package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.security.auth.AuthenticationProvider;
import dev.levelupschool.backend.dtos.ArticleDto;
import dev.levelupschool.backend.dtos.BookmarkDto;
import dev.levelupschool.backend.dtos.CommentDto;
import dev.levelupschool.backend.dtos.UserDto;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.payment.PaymentDetailsDto;
import dev.levelupschool.backend.payment.PaymentService;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.UpdateUserRequest;
import dev.levelupschool.backend.service.TokenService;
import dev.levelupschool.backend.service.UserService;
import dev.levelupschool.backend.subscription.SubscriptionDto;
import dev.levelupschool.backend.subscription.SubscriptionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
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

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SubscriptionService subscriptionService;

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
    ResponseEntity<?> register(@RequestBody @Valid RegisterDto registerDto) {
        try {
            var newUser = new User(registerDto.email, registerDto.name, registerDto.password);
            var createdUser = userService.createUser(newUser);
            return ResponseEntity.ok(tokenService.generateToken(createdUser.getId()));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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

    @PostMapping("/users/subscribe")
    ResponseEntity<?> subscribe(@RequestBody @Valid PaymentDetailsDto paymentDetailsDto) {
        var loggedInUser = authenticationProvider.getAuthenticatedUser();

        if (paymentDetailsDto.subscriptionType.equals("monthly")) {
            paymentDetailsDto.amount = 20;
        } else if (paymentDetailsDto.subscriptionType.equals("yearly")){
            paymentDetailsDto.amount = 180;
        }

        try {
            var response = paymentService.processPayment(paymentDetailsDto);

            JSONObject jsonObject = new JSONObject(response.getBody());

            if (subscriptionService.subscribe(loggedInUser, jsonObject, paymentDetailsDto.subscriptionType)){
                return new ResponseEntity<>("Your subscription was successful", HttpStatus.OK);
            }

            return new ResponseEntity<>("Could not verify transaction. Please try again", HttpStatus.REQUEST_TIMEOUT);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users/subscriptions")
    public List<SubscriptionDto> getSubscriptions() {
        var loggedInUser = authenticationProvider.getAuthenticatedUser();

        return subscriptionService.getSubscriptions(loggedInUser);

    }

    @Getter
    @Setter
    private static class LoginDto {
        @NotBlank
        @Email
        private String email;
        @NotEmpty
        private String password;
    }

    @Getter
    @Setter
    public static class RegisterDto {
        @NotBlank
        @Email
        private String email;
        @Size(min = 1, max = 256)
        private String name;
        @Size(min = 3, max = 25)
        private String password;
    }
}
