package dev.levelupschool.backend.auth;

import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {
    public static User getLoggedInUser(UserRepository userRepository) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) auth.getPrincipal();

        return  userRepository.findById(userId).orElseThrow(() -> new ModelNotFoundException(User.class, userId));
    }
}
