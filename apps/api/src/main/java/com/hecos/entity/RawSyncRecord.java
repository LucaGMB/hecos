package com.hecos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "raw_sync_records",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "type", "health_connect_id"})
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RawSyncRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String type;

    @Column(name = "health_connect_id", nullable = false)
    private String healthConnectId;

    private String sourceApp;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String data;

    @Column(nullable = false, updatable = false)
    private Instant receivedAt;

    @PrePersist
    protected void onCreate() {
        receivedAt = Instant.now();
    }
}
