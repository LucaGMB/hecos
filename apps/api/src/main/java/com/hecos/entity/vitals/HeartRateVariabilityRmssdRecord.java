package com.hecos.entity.vitals;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "heart_rate_variability_rmssd_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HeartRateVariabilityRmssdRecord extends InstantHealthRecord {

    @Column(nullable = false)
    private Double heartRateVariabilityMillis;
}
