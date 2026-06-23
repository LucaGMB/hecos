package com.hecos.entity.activity;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "elevation_gained_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ElevationGainedRecord extends IntervalHealthRecord {

    @Column(nullable = false)
    private Double elevationMeters;
}
