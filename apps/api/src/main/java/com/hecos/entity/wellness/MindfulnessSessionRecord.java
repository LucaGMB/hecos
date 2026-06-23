package com.hecos.entity.wellness;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mindfulness_session_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MindfulnessSessionRecord extends IntervalHealthRecord {

    @Column(nullable = false)
    private Integer mindfulnessSessionType;

    private String title;

    private String notes;
}
