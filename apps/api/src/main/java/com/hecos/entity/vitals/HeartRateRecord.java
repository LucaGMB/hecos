package com.hecos.entity.vitals;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "heart_rate_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HeartRateRecord extends IntervalHealthRecord {

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HeartRateSample> samples;
}
