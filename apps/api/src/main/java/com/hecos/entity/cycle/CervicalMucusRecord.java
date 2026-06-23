package com.hecos.entity.cycle;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cervical_mucus_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CervicalMucusRecord extends InstantHealthRecord {

    private Integer appearance;

    private Integer sensation;
}
