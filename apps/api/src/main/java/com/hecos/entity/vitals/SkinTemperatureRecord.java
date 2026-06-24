package com.hecos.entity.vitals;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "skin_temperature_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SkinTemperatureRecord extends IntervalHealthRecord {

    private Double baselineCelsius;

    private Integer measurementLocation;

    @JsonManagedReference
    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SkinTemperatureDelta> deltas;
}
