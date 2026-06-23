package com.hecos.entity.vitals;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "body_temperature_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BodyTemperatureRecord extends InstantHealthRecord {

    @Column(nullable = false)
    private Double temperatureCelsius;

    private Integer measurementLocation;
}
