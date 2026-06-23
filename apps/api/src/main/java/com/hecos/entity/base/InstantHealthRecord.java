package com.hecos.entity.base;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@MappedSuperclass
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public abstract class InstantHealthRecord extends BaseHealthRecord {

    @Column(nullable = false)
    private Instant time;

    private String zoneOffset;
}
