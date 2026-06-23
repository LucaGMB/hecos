package com.hecos.entity.activity;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "activity_intensity_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ActivityIntensityRecord extends IntervalHealthRecord {

    @Column(nullable = false)
    private Integer activityIntensityType;
}
