package dev.levelupschool.backend.service;

import dev.levelupschool.backend.controller.UserController;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final TokenService tokenService;

    UserService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(User.class, id));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User newUser, Long id) {
        return userRepository.findById(id)
            .map(user -> {
                user.setName(newUser.getName());
                return userRepository.save(user);
            }).orElseThrow(() -> new ModelNotFoundException(User.class, id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
