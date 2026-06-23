package com.hecos.entity.vitals;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "heart_rate_samples")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HeartRateSample {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private HeartRateRecord record;

    @Column(nullable = false)
    private Instant time;

    @Column(nullable = false)
    private Long beatsPerMinute;
}
