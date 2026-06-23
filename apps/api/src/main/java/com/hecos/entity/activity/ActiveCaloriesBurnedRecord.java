package com.hecos.entity.activity;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "active_calories_burned_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ActiveCaloriesBurnedRecord extends IntervalHealthRecord {

    @Column(nullable = false)
    private Double energyKcal;
}
