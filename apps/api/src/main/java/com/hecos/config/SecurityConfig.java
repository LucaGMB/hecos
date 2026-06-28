package com.hecos.config;

import com.hecos.entity.User;
import com.hecos.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Instant;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(UserRepository userRepository, JwtAuthFilter jwtAuthFilter) {
        this.userRepository = userRepository;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfig()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/health", "/login/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .successHandler((request, response, authentication) -> {
                    var oauthUser = (org.springframework.security.oauth2.core.user.OAuth2User) authentication.getPrincipal();
                    var attributes = oauthUser.getAttributes();
                    var googleId = attributes.get("sub").toString();
                    var email = attributes.get("email").toString();
                    var name = (String) attributes.get("name");
                    var avatarUrl = (String) attributes.get("picture");

                    userRepository.findByGoogleId(googleId).orElseGet(() ->
                        userRepository.save(User.builder()
                            .googleId(googleId)
                            .email(email)
                            .name(name)
                            .avatarUrl(avatarUrl)
                            .createdAt(Instant.now())
                            .build())
                    );

                    response.sendRedirect("/login/success");
                })
            );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfig() {
        var config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
