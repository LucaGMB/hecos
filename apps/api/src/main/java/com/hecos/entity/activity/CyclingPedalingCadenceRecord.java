package com.hecos.entity.activity;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "cycling_pedaling_cadence_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CyclingPedalingCadenceRecord extends IntervalHealthRecord {

    @JsonManagedReference
    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CyclingPedalingCadenceSample> samples;
}
