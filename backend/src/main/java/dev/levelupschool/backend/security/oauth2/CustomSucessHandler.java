package dev.levelupschool.backend.security.oauth2;

import dev.levelupschool.backend.controller.UserController;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class CustomSucessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CustumOAuth2UserService custumOAuth2UserService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirectUrl = null;
        if (authentication.getPrincipal() instanceof  CustomOAuth2User) {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            String email = oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email") : oAuth2User.getAttribute("login") + "@gmail.com";
            String name = oAuth2User.getAttribute("name") != null ? oAuth2User.getAttribute("name") : oAuth2User.getAttribute("login");
            User existingUser = userRepository.findByEmail(email);
            if (existingUser == null) {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                userRepository.save(newUser);
            }
            redirectUrl = "http://localhost:80/?loggedIn=true";

            response.sendRedirect(redirectUrl);
        }
    }
}
