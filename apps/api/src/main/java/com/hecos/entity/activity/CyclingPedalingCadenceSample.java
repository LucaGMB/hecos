package com.hecos.entity.activity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cycling_pedaling_cadence_samples")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CyclingPedalingCadenceSample {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private CyclingPedalingCadenceRecord record;

    @Column(nullable = false)
    private Instant time;

    @Column(nullable = false)
    private Double revolutionsPerMinute;
}
