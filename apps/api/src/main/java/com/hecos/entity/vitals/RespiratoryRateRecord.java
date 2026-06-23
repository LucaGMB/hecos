package com.hecos.entity.vitals;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "respiratory_rate_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RespiratoryRateRecord extends InstantHealthRecord {

    @Column(nullable = false)
    private Double rateBreathsPerMin;
}
