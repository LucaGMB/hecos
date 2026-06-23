package com.hecos.entity.cycle;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sexual_activity_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SexualActivityRecord extends InstantHealthRecord {

    private Integer protectionUsed;
}
