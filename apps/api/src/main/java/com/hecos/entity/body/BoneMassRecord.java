package com.hecos.entity.body;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bone_mass_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BoneMassRecord extends InstantHealthRecord {

    @Column(nullable = false)
    private Double massKg;
}
