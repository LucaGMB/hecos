package com.hecos.entity.activity;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "distance_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DistanceRecord extends IntervalHealthRecord {

    @Column(nullable = false)
    private Double distanceMeters;
}
