package com.hecos.entity.vitals;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "skin_temperature_deltas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SkinTemperatureDelta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private SkinTemperatureRecord record;

    @Column(nullable = false)
    private Instant time;

    @Column(nullable = false)
    private Double deltaCelsius;
}
