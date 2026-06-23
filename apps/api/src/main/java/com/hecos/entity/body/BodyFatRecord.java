package com.hecos.entity.body;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "body_fat_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BodyFatRecord extends InstantHealthRecord {

    @Column(nullable = false)
    private Double percentage;
}
