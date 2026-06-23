package com.hecos.entity.cycle;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "basal_body_temperature_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BasalBodyTemperatureRecord extends InstantHealthRecord {

    @Column(nullable = false)
    private Double temperatureCelsius;

    private Integer measurementLocation;
}
