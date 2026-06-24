package com.hecos.entity.base;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public abstract class IntervalHealthRecord extends BaseHealthRecord {

    @Column(nullable = false)
    private Instant startTime;

    private String startZoneOffset;

    @Column(nullable = false)
    private Instant endTime;

    private String endZoneOffset;
}
