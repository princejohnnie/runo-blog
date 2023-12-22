package dev.levelupschool.backend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class OAuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SlugService slugService;
    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance(); // Or GsonFactory.getDefaultInstance()

    HttpTransport transport = new NetHttpTransport();

    public String authenticateUser(AuthRequest authRequest) throws GeneralSecurityException, IOException {

        GoogleIdToken idToken = verifyGoogleToken(authRequest.getCredential());

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            User user = userRepository.findByEmail(email);
            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setName((String) payload.get("name"));
                user.setPassword(Math.random() + "");
                user.setAvatarUrl((String) payload.get("picture"));
                user.setSlug(slugService.makeSlug(user.getName()));
                userRepository.save(user);
            }

            Long userId = user.getId();

            return tokenService.generateToken(userId);
        } else {
            return null;
        }
    }

    private GoogleIdToken verifyGoogleToken(String credential) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(Collections.singletonList("608909873783-2sqj1phfijkms47vo8hk2t90lekn9r4p.apps.googleusercontent.com"))
            .build();
        return verifier.verify(credential);
    }

    }
