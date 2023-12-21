package dev.levelupschool.backend.auth;

import dev.levelupschool.backend.exception.ModelNotFoundException;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BearerTokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        var userId = (Long) auth.getPrincipal();

        return  userRepository.findById(userId).orElseThrow(() -> new ModelNotFoundException(User.class, userId));
    }
}
