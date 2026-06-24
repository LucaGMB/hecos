package com.hecos.config;

import com.hecos.entity.activity.*;
import com.hecos.entity.body.*;
import com.hecos.entity.cycle.*;
import com.hecos.entity.nutrition.*;
import com.hecos.entity.sleep.*;
import com.hecos.entity.vitals.*;
import com.hecos.entity.wellness.*;
import com.hecos.entity.base.BaseHealthRecord;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
public class HealthRecordTypeRegistry {

    private final Map<String, Class<? extends BaseHealthRecord>> slugToClass = new LinkedHashMap<>();
    private final Map<Class<? extends BaseHealthRecord>, String> classToSlug = new LinkedHashMap<>();

    public HealthRecordTypeRegistry() {
        register("steps", StepsRecord.class);
        register("steps-cadence", StepsCadenceRecord.class);
        register("distance", DistanceRecord.class);
        register("elevation-gained", ElevationGainedRecord.class);
        register("floors-climbed", FloorsClimbedRecord.class);
        register("active-calories-burned", ActiveCaloriesBurnedRecord.class);
        register("total-calories-burned", TotalCaloriesBurnedRecord.class);
        register("exercise-session", ExerciseSessionRecord.class);
        register("planned-exercise-session", PlannedExerciseSessionRecord.class);
        register("cycling-pedaling-cadence", CyclingPedalingCadenceRecord.class);
        register("power", PowerRecord.class);
        register("speed", SpeedRecord.class);
        register("vo2-max", Vo2MaxRecord.class);
        register("wheelchair-pushes", WheelchairPushesRecord.class);
        register("activity-intensity", ActivityIntensityRecord.class);

        register("weight", WeightRecord.class);
        register("height", HeightRecord.class);
        register("body-fat", BodyFatRecord.class);
        register("body-water-mass", BodyWaterMassRecord.class);
        register("bone-mass", BoneMassRecord.class);
        register("lean-body-mass", LeanBodyMassRecord.class);
        register("basal-metabolic-rate", BasalMetabolicRateRecord.class);

        register("heart-rate", HeartRateRecord.class);
        register("resting-heart-rate", RestingHeartRateRecord.class);
        register("heart-rate-variability", HeartRateVariabilityRmssdRecord.class);
        register("blood-pressure", BloodPressureRecord.class);
        register("blood-glucose", BloodGlucoseRecord.class);
        register("oxygen-saturation", OxygenSaturationRecord.class);
        register("respiratory-rate", RespiratoryRateRecord.class);
        register("body-temperature", BodyTemperatureRecord.class);
        register("skin-temperature", SkinTemperatureRecord.class);

        register("sleep-session", SleepSessionRecord.class);

        register("nutrition", NutritionRecord.class);
        register("hydration", HydrationRecord.class);

        register("menstruation-period", MenstruationPeriodRecord.class);
        register("menstruation-flow", MenstruationFlowRecord.class);
        register("cervical-mucus", CervicalMucusRecord.class);
        register("ovulation-test", OvulationTestRecord.class);
        register("sexual-activity", SexualActivityRecord.class);
        register("intermenstrual-bleeding", IntermenstrualBleedingRecord.class);
        register("basal-body-temperature", BasalBodyTemperatureRecord.class);

        register("mindfulness-session", MindfulnessSessionRecord.class);
    }

    private void register(String slug, Class<? extends BaseHealthRecord> clazz) {
        slugToClass.put(slug, clazz);
        classToSlug.put(clazz, slug);
    }

    public Optional<Class<? extends BaseHealthRecord>> getClass(String slug) {
        return Optional.ofNullable(slugToClass.get(slug));
    }

    public Optional<String> getSlug(Class<? extends BaseHealthRecord> clazz) {
        return Optional.ofNullable(classToSlug.get(clazz));
    }

    public Set<String> getAllSlugs() {
        return slugToClass.keySet();
    }
}
