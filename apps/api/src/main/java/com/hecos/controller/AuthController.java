package com.hecos.controller;

import com.hecos.repository.UserRepository;
import com.hecos.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/google")
    public ResponseEntity<?> googleAuth(@RequestBody Map<String, String> body) {
        var idToken = body.get("idToken");
        if (idToken == null || idToken.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "idToken is required"));
        }

        try {
            var url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
            var response = restTemplate.getForEntity(url, Map.class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
            }

            var payload = response.getBody();
            var googleId = (String) payload.get("sub");
            var email = (String) payload.get("email");
            var name = (String) payload.get("name");
            var avatarUrl = (String) payload.get("picture");

            var user = userRepository.findByGoogleId(googleId)
                .orElseGet(() -> userRepository.save(
                    com.hecos.entity.User.builder()
                        .googleId(googleId)
                        .email(email)
                        .name(name)
                        .avatarUrl(avatarUrl)
                        .createdAt(Instant.now())
                        .build()
                ));

            var jwt = jwtService.generateToken(user.getId(), user.getEmail());
            return ResponseEntity.ok(Map.of(
                "token", jwt,
                "userId", user.getId().toString(),
                "email", user.getEmail(),
                "name", user.getName()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Token verification failed"));
        }
    }
}
