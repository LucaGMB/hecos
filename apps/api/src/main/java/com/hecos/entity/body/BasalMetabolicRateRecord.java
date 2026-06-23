package com.hecos.entity.body;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "basal_metabolic_rate_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BasalMetabolicRateRecord extends InstantHealthRecord {

    @Column(nullable = false)
    private Double basalMetabolicRateKcalPerDay;
}
