package dev.levelupschool.backend;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;

    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    List<User> index() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    User show(@PathVariable Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(id));
    }

    @PostMapping("/users")
    User store(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/users/{id}")
    User update(@RequestBody User newUser, @PathVariable Long id) {
        return userRepository.findById(id)
            .map(user -> {
                user.setName(newUser.getName());
                return userRepository.save(user);
            }).orElseThrow(() -> new ModelNotFoundException(id));
    }

    @DeleteMapping("/users/{id}")
    void delete(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}
