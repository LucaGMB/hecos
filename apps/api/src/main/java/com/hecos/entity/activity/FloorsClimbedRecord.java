package com.hecos.entity.activity;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "floors_climbed_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FloorsClimbedRecord extends IntervalHealthRecord {

    @Column(nullable = false)
    private Double floors;
}
