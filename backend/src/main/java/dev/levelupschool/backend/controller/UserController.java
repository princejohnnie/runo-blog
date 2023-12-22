package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.dtos.*;
import dev.levelupschool.backend.auth.AuthenticationProvider;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.mail.EmailService;
import dev.levelupschool.backend.model.PasswordResetToken;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.payment.PaymentDetailsDto;
import dev.levelupschool.backend.payment.PaymentService;
import dev.levelupschool.backend.repository.PasswordResetTokenRepository;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.PasswordResetRequest;
import dev.levelupschool.backend.request.UpdateUserRequest;
import dev.levelupschool.backend.service.TokenService;
import dev.levelupschool.backend.service.UserService;
import dev.levelupschool.backend.subscription.SubscriptionDto;
import dev.levelupschool.backend.subscription.SubscriptionService;
import jakarta.mail.MessagingException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private SubscriptionService subscriptionService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    private final EmailService emailService;


    private final PaymentService paymentService;

    @Autowired
    UserController(PaymentService paymentService, EmailService emailService) {
        this.paymentService = paymentService;
        this.emailService = emailService;
    }

    @GetMapping("/users")
    ResponseEntity<?> index(
        @PageableDefault(page = 0, size = Integer.MAX_VALUE, sort = {"name"}) Pageable paging) {
        try {
            var users =  userService.getAllUsers(paging);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
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
            var newUser = new User(registerDto.getEmail(), registerDto.getName(), registerDto.getDescription(), registerDto.getPassword());
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
            throw new ModelNotFoundException(User.class, loginDto.getEmail());
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

            String newResponse = response.getBody().substring(response.getBody().indexOf("{"));

            JSONObject jsonObject = new JSONObject(newResponse);

            if (subscriptionService.subscribe(loggedInUser, jsonObject, paymentDetailsDto.subscriptionType)){
                return new ResponseEntity<>("Your subscription was successful", HttpStatus.OK);
            }

            return new ResponseEntity<>(jsonObject.get("message"), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/subscriptions")
    public List<SubscriptionDto> getSubscriptions() {
        var loggedInUser = authenticationProvider.getAuthenticatedUser();
        return subscriptionService.getSubscriptions(loggedInUser).stream().map(SubscriptionDto::new).toList();
    }

    @PostMapping("/user/cancel-subscription/{id}")
    public ResponseEntity<String> cancelSubscription(@PathVariable Long id) {
        var loggedInUser = authenticationProvider.getAuthenticatedUser();
        if (subscriptionService.cancelSubscription(id, loggedInUser)) {
            var user = userRepository.findById(loggedInUser.getId()).orElseThrow(() -> new ModelNotFoundException(User.class, loggedInUser.getId()));
            user.setPremium(subscriptionService.getSubscriptions(loggedInUser));
            userRepository.save(user);
            return new ResponseEntity<>("Subscription cancelled successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Could not cancel subscription", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody PasswordResetRequest request) {
        System.out.println("Got Email -> " + request.getEmail());

        var user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            return new ResponseEntity<String>("User with email does not exist", HttpStatus.NOT_FOUND);
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryTime(LocalDateTime.now().plusMinutes(30));

        token = passwordResetTokenRepository.save(token);

        try {
            String body = "Use this link to reset your password. Expires after 30 minutes\nhttp://localhost:80/reset-password?token="+token;
            emailService.sendMail(request.getEmail(), "Password Reset link", body);

            System.out.println("Got token -> " + token.getToken());

            return new ResponseEntity<String>("A Password reset link has been sent to your email", HttpStatus.OK);

        } catch (MessagingException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("password");

        try {
            var resetToken = passwordResetTokenRepository.findByToken(token);

            if (resetToken.getExpiryTime().isBefore(LocalDateTime.now())) {
                passwordResetTokenRepository.deleteById(resetToken.getId());
                return new ResponseEntity<>("Password reset link has expired", HttpStatus.UNAUTHORIZED);
            }

            User user = userRepository.findById(resetToken.getUser().getId()).orElseThrow(() -> new ModelNotFoundException(User.class, resetToken.getUser().getId()));

            user.setPassword(newPassword);

            User updatedUser = userRepository.save(user);

            if (updatedUser.getId().equals(user.getId())) {
                passwordResetTokenRepository.deleteById(resetToken.getId());
                return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
            }

            return new ResponseEntity<>("Could not complete request. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);

        } catch (Exception e) {

            return new ResponseEntity<>("Sorry, you are unauthorized to perform this operation", HttpStatus.UNAUTHORIZED);

        }
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
    private static class RegisterDto {
        @NotBlank
        @Email
        private String email;
        @Size(min = 1, max = 256)
        private String name;
        @Size(min = 5, max = 64)
        private String description;
        @Size(min = 3, max = 25)
        private String password;
    }
}
