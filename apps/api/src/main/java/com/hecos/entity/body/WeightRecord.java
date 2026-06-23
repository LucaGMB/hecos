package com.hecos.entity.body;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "weight_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class WeightRecord extends InstantHealthRecord {

    @Column(nullable = false)
    private Double weightKg;
}
