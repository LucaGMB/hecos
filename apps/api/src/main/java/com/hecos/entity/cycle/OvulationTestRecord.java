package com.hecos.entity.cycle;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ovulation_test_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class OvulationTestRecord extends InstantHealthRecord {

    @Column(nullable = false)
    private Integer result;
}
