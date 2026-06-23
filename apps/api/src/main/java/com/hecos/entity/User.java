package com.hecos.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String googleId;

    @Column(nullable = false)
    private String email;

    private String name;

    private String avatarUrl;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant lastSyncAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
