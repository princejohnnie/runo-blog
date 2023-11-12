package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.service.UserService;
import dev.levelupschool.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    List<User> index() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    User show(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping("/users")
    User store(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/users/{id}")
    User update(@RequestBody User newUser, @PathVariable Long id) {
        return userService.updateUser(newUser, id);
    }

    @DeleteMapping("/users/{id}")
    void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
