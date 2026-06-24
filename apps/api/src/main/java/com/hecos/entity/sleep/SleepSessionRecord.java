package com.hecos.entity.sleep;

import com.hecos.entity.base.IntervalHealthRecord;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "sleep_session_records")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SleepSessionRecord extends IntervalHealthRecord {

    private String title;

    private String notes;

    @JsonManagedReference
    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SleepStage> stages;
}
