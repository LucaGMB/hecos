package com.hecos.entity.vitals;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blood_glucose_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BloodGlucoseRecord extends InstantHealthRecord {

    @Column(nullable = false)
    private Double levelMmolPerL;

    private Integer specimenSource;

    private Integer mealType;

    private Integer relationToMeal;
}
