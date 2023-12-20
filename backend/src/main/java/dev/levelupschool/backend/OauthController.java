package dev.levelupschool.backend;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory; // Or GsonFactory

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

import dev.levelupschool.backend.model.User;
import dev.levelupschool.backend.repository.UserRepository;
import dev.levelupschool.backend.request.AuthRequest;
import dev.levelupschool.backend.service.SlugService;
import dev.levelupschool.backend.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@RestController
@RequestMapping()
public class OauthController {

    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance(); // Or GsonFactory.getDefaultInstance()

    HttpTransport transport = new NetHttpTransport();

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SlugService slugService;

    @PostMapping("/google-auth")
    ResponseEntity<?> register(@RequestBody AuthRequest authRequest) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(Collections.singletonList("608909873783-2sqj1phfijkms47vo8hk2t90lekn9r4p.apps.googleusercontent.com"))
            .build();

        GoogleIdToken idToken = verifier.verify(authRequest.getCredential());

        if (idToken != null) {
            Payload payload = idToken.getPayload();
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

            String generatedToken = tokenService.generateToken(userId);

            return ResponseEntity.ok(generatedToken);

        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
