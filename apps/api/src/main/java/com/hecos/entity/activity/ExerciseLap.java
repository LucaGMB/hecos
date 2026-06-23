package com.hecos.entity.activity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "exercise_laps")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ExerciseLap {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private ExerciseSessionRecord record;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    private Double lengthMeters;
}
