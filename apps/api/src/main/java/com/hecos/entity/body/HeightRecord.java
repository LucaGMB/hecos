package com.hecos.entity.body;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "height_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class HeightRecord extends InstantHealthRecord {

    @Column(nullable = false)
    private Double heightMeters;
}
