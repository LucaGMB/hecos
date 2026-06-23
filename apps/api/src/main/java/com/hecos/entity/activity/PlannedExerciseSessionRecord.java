package com.hecos.entity.activity;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "planned_exercise_session_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PlannedExerciseSessionRecord extends IntervalHealthRecord {

    @Column(nullable = false)
    private Integer exerciseType;

    @Column(nullable = false)
    private Boolean hasExplicitTime;

    private String completedExerciseSessionId;

    private String title;

    private String notes;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlannedExerciseBlock> blocks;
}
