package com.hecos.entity.vitals;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resting_heart_rate_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RestingHeartRateRecord extends InstantHealthRecord {

    @Column(nullable = false)
    private Long beatsPerMinute;
}
