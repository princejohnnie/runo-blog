package dev.levelupschool.backend.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenService {

    private static final long EXPIRATION_TIME = 86400000;

    private static final String SIGNING_KEY = "SIGNING_KEY";

    public String generateToken(Long userId) {
        return Jwts
            .builder()
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
            .setSubject(userId.toString())
            .compact();
    }

    public TokenData validateToken(String token) {
        var body = Jwts
            .parser()
            .setSigningKey(SIGNING_KEY)
            .parseClaimsJws(token)
            .getBody();

        var userId = Long.parseLong(body.getSubject());
        var expiration = body.getExpiration();
        var issuedAt = body.getIssuedAt();

        if (expiration.before(new Date())) {
            return null; // Token is expired
        }

        var tokenData = new TokenData();
        tokenData.setExpiration(expiration);
        tokenData.setIssuedAt(issuedAt);
        tokenData.setUserId(userId);

        return tokenData;
    }


    @Getter
    @Setter
    public static class TokenData {
        private Long userId;
        private Date issuedAt;
        private Date expiration;
    }
}
