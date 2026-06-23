package com.hecos.entity.vitals;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blood_pressure_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BloodPressureRecord extends InstantHealthRecord {

    @Column(nullable = false)
    private Double systolicMmHg;

    @Column(nullable = false)
    private Double diastolicMmHg;

    private Integer bodyPosition;

    private Integer measurementLocation;
}
