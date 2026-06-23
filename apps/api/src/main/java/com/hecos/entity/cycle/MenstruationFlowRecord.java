package com.hecos.entity.cycle;

import com.hecos.entity.base.InstantHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menstruation_flow_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MenstruationFlowRecord extends InstantHealthRecord {

    private Integer flow;
}
