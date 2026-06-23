package com.hecos.entity.nutrition;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hydration_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HydrationRecord extends IntervalHealthRecord {

    @Column(nullable = false)
    private Double volumeLiters;
}
