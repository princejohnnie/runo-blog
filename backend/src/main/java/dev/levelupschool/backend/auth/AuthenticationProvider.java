package dev.levelupschool.backend.auth;

import dev.levelupschool.backend.model.User;

public interface AuthenticationProvider {
    User getAuthenticatedUser();
}
