package com.hecos.entity.activity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "exercise_route_locations")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ExerciseRouteLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private ExerciseSessionRecord record;

    @Column(nullable = false)
    private Instant time;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private Double altitudeMeters;

    private Double horizontalAccuracyMeters;

    private Double verticalAccuracyMeters;
}
