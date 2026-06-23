package com.hecos.entity.activity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "exercise_segments")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ExerciseSegment {

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

    @Column(nullable = false)
    private Integer segmentType;

    private Integer repetitions;

    private Double weightGrams;

    private Integer setIndex;

    private Float rateOfPerceivedExertion;
}
