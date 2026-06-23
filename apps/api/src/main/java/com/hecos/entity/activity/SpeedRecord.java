package com.hecos.entity.activity;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "speed_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SpeedRecord extends IntervalHealthRecord {

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpeedSample> samples;
}
