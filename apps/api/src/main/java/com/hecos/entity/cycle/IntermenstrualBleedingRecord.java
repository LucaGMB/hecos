package com.hecos.entity.cycle;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "intermenstrual_bleeding_records")
@Getter @Setter
@NoArgsConstructor
public class IntermenstrualBleedingRecord extends InstantHealthRecord {
}
