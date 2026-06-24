package com.hecos.entity.activity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "power_samples")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PowerSample {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private PowerRecord record;

    @Column(nullable = false)
    private Instant time;

    @Column(nullable = false)
    private Double powerWatts;
}
