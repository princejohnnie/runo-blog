package dev.levelupschool.backend.service;

import dev.levelupschool.backend.auth.AuthenticationUtils;
import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(User.class, id));
    }

    public User createUser(User newUser) throws Exception {
        var user = userRepository.findByEmail(newUser.getEmail());

        if (user != null) {
            throw new Exception("user with email already exists");
        }

        return userRepository.save(newUser);
    }

    public User updateUser(User newUser, Long id) throws Exception {
        var loggedInUser = AuthenticationUtils.getLoggedInUser(userRepository);

        var checkUser = userRepository.findByEmail(newUser.getEmail());
        if (checkUser != null) {
            throw new Exception("user with email already exists");
        }

        var user = userRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(User.class, id));

        if (!loggedInUser.getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot update another user's profile!");
        }

        user.setEmail(newUser.getEmail());
        user.setName(newUser.getName());
        user.setPassword(newUser.getPassword());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) throws AccessDeniedException {
        var loggedInUser = AuthenticationUtils.getLoggedInUser(userRepository);

        var user = userRepository.findById(id)
            .orElseThrow(() -> new ModelNotFoundException(User.class, id));

        if (!loggedInUser.getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot delete another user!");
        }

        userRepository.deleteById(id);
    }
}
