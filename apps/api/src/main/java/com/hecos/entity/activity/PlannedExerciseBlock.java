package com.hecos.entity.activity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "planned_exercise_blocks")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class PlannedExerciseBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private PlannedExerciseSessionRecord record;

    private Integer blockOrder;

    private Integer repetitions;

    private String description;
}
