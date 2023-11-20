package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.auth.AuthenticationUtils;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.service.TokenService;
import dev.levelupschool.backend.service.UserService;
import dev.levelupschool.backend.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/users")
    List<User> index() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    User show(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}")
    User update(@RequestBody User newUser, @PathVariable Long id) {
        return userService.updateUser(newUser, id);
    }

    @DeleteMapping("/users/{id}")
    void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/register")
    ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        var user = new User(registerDto.getEmail(), registerDto.getName(), registerDto.getSlug(), registerDto.getPassword());

        userService.createUser(user);

        return ResponseEntity.ok(tokenService.generateToken(user.getId()));
    }


    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        var user = userRepository.findByEmail(loginDto.getEmail());

        if (user == null) {
            throw new ModelNotFoundException(User.class, 0L);
        }

        if(BCrypt.checkpw(loginDto.getPassword(), user.getPassword())) {
            return ResponseEntity.ok(tokenService.generateToken(user.getId()));
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/me")
    User me() {
        return AuthenticationUtils.getLoggedInUser(userRepository);
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
