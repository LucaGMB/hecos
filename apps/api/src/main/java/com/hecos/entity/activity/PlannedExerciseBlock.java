package com.hecos.entity.activity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private PlannedExerciseSessionRecord record;

    private Integer blockOrder;

    private Integer repetitions;

    private String description;
}
