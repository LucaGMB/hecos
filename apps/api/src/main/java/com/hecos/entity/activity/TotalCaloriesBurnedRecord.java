package com.hecos.entity.activity;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "total_calories_burned_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TotalCaloriesBurnedRecord extends IntervalHealthRecord {

    @Column(nullable = false)
    private Double energyKcal;
}
