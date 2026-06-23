package com.hecos.entity.activity;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "exercise_session_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ExerciseSessionRecord extends IntervalHealthRecord {

    @Column(nullable = false)
    private Integer exerciseType;

    private String title;

    private String notes;

    private String plannedExerciseSessionId;

    private Float rateOfPerceivedExertion;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseSegment> segments;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseLap> laps;

    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseRouteLocation> routeLocations;
}
