package com.hecos.entity.cycle;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menstruation_period_records")
@Getter @Setter
@NoArgsConstructor
public class MenstruationPeriodRecord extends IntervalHealthRecord {
}
