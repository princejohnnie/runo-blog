package dev.levelupschool.backend.configuration;

import dev.levelupschool.backend.security.auth.CustomAuthenticationFilter;
import dev.levelupschool.backend.security.oauth2.CustumOAuth2UserService;
import dev.levelupschool.backend.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CustumOAuth2UserService custumOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorization) -> authorization
            .requestMatchers(HttpMethod.POST, "/login", "/register", "login/oauth2/**")
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
        )
            .csrf(AbstractHttpConfigurer::disable)
            .oauth2Login(
                (oauth2) -> oauth2
                    .userInfoEndpoint(
                        (userInfo) -> userInfo
                            .userService(custumOAuth2UserService)
                    ).loginPage("/login/oauth2/**")
            )
            .addFilterBefore(new CustomAuthenticationFilter(new TokenService()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}
