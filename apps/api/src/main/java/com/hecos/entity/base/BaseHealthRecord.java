package com.hecos.entity.base;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public abstract class BaseHealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(unique = true)
    private String healthConnectId;

    private String sourceApp;

    private String deviceModel;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
