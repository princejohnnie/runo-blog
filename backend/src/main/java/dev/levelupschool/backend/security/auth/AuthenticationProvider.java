package dev.levelupschool.backend.security.auth;

import dev.levelupschool.backend.model.User;

public interface AuthenticationProvider {
    User getAuthenticatedUser();
}
