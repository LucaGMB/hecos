package com.hecos.mobile.data.health

import androidx.health.connect.client.records.*
import kotlin.reflect.KClass

val SLUG_TO_RECORD_TYPE: Map<String, KClass<out Record>> = linkedMapOf(
    "steps" to StepsRecord::class,
    "distance" to DistanceRecord::class,
    "total-calories-burned" to TotalCaloriesBurnedRecord::class,
    "active-calories-burned" to ActiveCaloriesBurnedRecord::class,
    "exercise-session" to ExerciseSessionRecord::class,
    "floors-climbed" to FloorsClimbedRecord::class,
    "elevation-gained" to ElevationGainedRecord::class,
    "wheelchair-pushes" to WheelchairPushesRecord::class,
    "vo2-max" to Vo2MaxRecord::class,

    "heart-rate" to HeartRateRecord::class,
    "resting-heart-rate" to RestingHeartRateRecord::class,
    "heart-rate-variability" to HeartRateVariabilityRmssdRecord::class,
    "blood-pressure" to BloodPressureRecord::class,
    "blood-glucose" to BloodGlucoseRecord::class,
    "oxygen-saturation" to OxygenSaturationRecord::class,
    "respiratory-rate" to RespiratoryRateRecord::class,
    "body-temperature" to BodyTemperatureRecord::class,
    "basal-body-temperature" to BasalBodyTemperatureRecord::class,

    "weight" to WeightRecord::class,
    "height" to HeightRecord::class,
    "body-fat" to BodyFatRecord::class,
    "body-water-mass" to BodyWaterMassRecord::class,
    "bone-mass" to BoneMassRecord::class,
    "lean-body-mass" to LeanBodyMassRecord::class,
    "basal-metabolic-rate" to BasalMetabolicRateRecord::class,

    "sleep-session" to SleepSessionRecord::class,

    "nutrition" to NutritionRecord::class,
    "hydration" to HydrationRecord::class,

    "menstruation-period" to MenstruationPeriodRecord::class,
    "menstruation-flow" to MenstruationFlowRecord::class,
    "cervical-mucus" to CervicalMucusRecord::class,
    "ovulation-test" to OvulationTestRecord::class,
    "sexual-activity" to SexualActivityRecord::class,
    "intermenstrual-bleeding" to IntermenstrualBleedingRecord::class,
)

val ALL_RECORD_TYPES: Set<KClass<out Record>> = SLUG_TO_RECORD_TYPE.values.toSet()
