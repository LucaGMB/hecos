package com.hecos.entity.activity;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vo2_max_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Vo2MaxRecord extends InstantHealthRecord {

    @Column(nullable = false)
    private Double vo2MlPerMinKg;

    private Integer measurementMethod;
}
