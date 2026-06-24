package com.hecos.service;

import com.hecos.entity.User;
import com.hecos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public User findOrCreateByGoogle(String googleId, String email, String name, String avatarUrl) {
        return repository.findByGoogleId(googleId)
                .orElseGet(() -> repository.save(User.builder()
                        .googleId(googleId)
                        .email(email)
                        .name(name)
                        .avatarUrl(avatarUrl)
                        .createdAt(Instant.now())
                        .build()));
    }

    public Optional<User> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
