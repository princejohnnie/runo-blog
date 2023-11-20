package dev.levelupschool.backend.auth;

import dev.levelupschool.backend.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public CustomAuthenticationFilter(TokenService tokenService) {
        super();
        this.tokenService = tokenService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String encodedToken = header.substring(7);
            var tokenData = tokenService.validateToken(encodedToken);

            if (tokenData != null) {
                var authentication = new UsernamePasswordAuthenticationToken(
                    tokenData.getUserId(),
                    null,
                    new ArrayList<>()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
