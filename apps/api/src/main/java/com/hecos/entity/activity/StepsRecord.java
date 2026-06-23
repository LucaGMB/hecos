package com.hecos.entity.activity;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "steps_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class StepsRecord extends IntervalHealthRecord {

    @Column(nullable = false)
    private Long count;
}
