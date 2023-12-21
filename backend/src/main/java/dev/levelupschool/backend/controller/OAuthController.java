package dev.levelupschool.backend.controller;

import dev.levelupschool.backend.request.AuthRequest;
import dev.levelupschool.backend.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping()
public class OAuthController {

    @Autowired
    private OAuthService oAuthService;

    @PostMapping("/google-auth")
    ResponseEntity<?> register(@RequestBody AuthRequest authRequest) {
        try {
            String generatedToken = oAuthService.authenticateUser(authRequest);

            if (generatedToken != null) {
                return ResponseEntity.ok(generatedToken);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
