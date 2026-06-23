package com.hecos.entity.vitals;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "oxygen_saturation_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class OxygenSaturationRecord extends InstantHealthRecord {

    @Column(nullable = false)
    private Double percentage;
}
