package dev.levelupschool.backend.configuration;


import dev.levelupschool.backend.auth.CustomAuthenticationFilter;
import dev.levelupschool.backend.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorization) -> authorization
            .requestMatchers(HttpMethod.POST, "/login", "/register", "/google-auth")
            .permitAll()
            .requestMatchers(HttpMethod.DELETE, "/**")
            .authenticated()
            .requestMatchers(HttpMethod.POST, "/**")
            .authenticated()
            .requestMatchers(HttpMethod.PUT, "/**")
            .authenticated()
            .requestMatchers(HttpMethod.PATCH, "/**")
            .authenticated()
            .anyRequest()
            .permitAll()
        ).csrf(AbstractHttpConfigurer::disable)
            .addFilterBefore(new CustomAuthenticationFilter(new TokenService()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
