package com.hecos.mobile.data.health

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.*
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.time.Instant
import java.time.temporal.ChronoUnit

class HealthConnectReader(context: Context) {

    private val client = HealthConnectClient.getOrCreate(context)

    companion object {
        fun isAvailable(context: Context): Boolean {
            return HealthConnectClient.getSdkStatus(context) == HealthConnectClient.SDK_AVAILABLE
        }

        val PERMISSIONS = buildSet {
            ALL_RECORD_TYPES.forEach { add(HealthPermission.getReadPermission(it)) }
        }
    }

    suspend fun hasAllPermissions(): Boolean {
        val granted = client.permissionController.getGrantedPermissions()
        return granted.containsAll(PERMISSIONS)
    }

    suspend fun readAll(): Map<String, JsonArray> {
        val results = mutableMapOf<String, JsonArray>()
        val timeRange = TimeRangeFilter.between(
            Instant.now().minus(365, ChronoUnit.DAYS),
            Instant.now()
        )

        for ((slug, recordType) in SLUG_TO_RECORD_TYPE) {
            try {
                val records = readRecords(recordType, timeRange)
                if (records.size() > 0) {
                    results[slug] = records
                }
            } catch (_: Exception) {
            }
        }

        return results
    }

    private suspend fun <T : Record> readRecords(
        recordType: kotlin.reflect.KClass<T>,
        timeRange: TimeRangeFilter
    ): JsonArray {
        val response = client.readRecords(
            ReadRecordsRequest(
                recordType = recordType,
                timeRangeFilter = timeRange
            )
        )
        val array = JsonArray()
        for (record in response.records) {
            array.add(recordToJson(record))
        }
        return array
    }

    private fun recordToJson(record: Record): JsonObject {
        val json = JsonObject()
        json.addProperty("healthConnectId", record.metadata.id)
        json.addProperty("sourceApp", record.metadata.dataOrigin.packageName)

        when (record) {
            is StepsRecord -> {
                json.addProperty("startTime", record.startTime.toString())
                json.addProperty("startZoneOffset", record.startZoneOffset?.toString())
                json.addProperty("endTime", record.endTime.toString())
                json.addProperty("endZoneOffset", record.endZoneOffset?.toString())
                json.addProperty("count", record.count)
            }
            is DistanceRecord -> {
                json.addProperty("startTime", record.startTime.toString())
                json.addProperty("startZoneOffset", record.startZoneOffset?.toString())
                json.addProperty("endTime", record.endTime.toString())
                json.addProperty("endZoneOffset", record.endZoneOffset?.toString())
                json.addProperty("distanceMeters", record.distance.inMeters)
            }
            is TotalCaloriesBurnedRecord -> {
                json.addProperty("startTime", record.startTime.toString())
                json.addProperty("startZoneOffset", record.startZoneOffset?.toString())
                json.addProperty("endTime", record.endTime.toString())
                json.addProperty("endZoneOffset", record.endZoneOffset?.toString())
                json.addProperty("energyKcal", record.energy.inKilocalories)
            }
            is ActiveCaloriesBurnedRecord -> {
                json.addProperty("startTime", record.startTime.toString())
                json.addProperty("startZoneOffset", record.startZoneOffset?.toString())
                json.addProperty("endTime", record.endTime.toString())
                json.addProperty("endZoneOffset", record.endZoneOffset?.toString())
                json.addProperty("energyKcal", record.energy.inKilocalories)
            }
            is HeartRateRecord -> {
                json.addProperty("startTime", record.startTime.toString())
                json.addProperty("startZoneOffset", record.startZoneOffset?.toString())
                json.addProperty("endTime", record.endTime.toString())
                json.addProperty("endZoneOffset", record.endZoneOffset?.toString())
                val samplesArray = JsonArray()
                record.samples.forEach { sample ->
                    val sampleJson = JsonObject()
                    sampleJson.addProperty("time", sample.time.toString())
                    sampleJson.addProperty("beatsPerMinute", sample.beatsPerMinute)
                    samplesArray.add(sampleJson)
                }
                json.add("samples", samplesArray)
            }
            is RestingHeartRateRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("beatsPerMinute", record.beatsPerMinute)
            }
            is HeartRateVariabilityRmssdRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("heartRateVariabilityMillis", record.heartRateVariabilityMillis)
            }
            is BloodPressureRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("systolicMmHg", record.systolic.inMillimetersOfMercury)
                json.addProperty("diastolicMmHg", record.diastolic.inMillimetersOfMercury)
                json.addProperty("bodyPosition", record.bodyPosition)
                json.addProperty("measurementLocation", record.measurementLocation)
            }
            is BloodGlucoseRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("levelMmolPerL", record.level.inMillimolesPerLiter)
                json.addProperty("specimenSource", record.specimenSource)
                json.addProperty("mealType", record.mealType)
                json.addProperty("relationToMeal", record.relationToMeal)
            }
            is OxygenSaturationRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("percentage", record.percentage.value)
            }
            is RespiratoryRateRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("rate", record.rate)
            }
            is BodyTemperatureRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("temperatureCelsius", record.temperature.inCelsius)
                json.addProperty("measurementLocation", record.measurementLocation)
            }
            is WeightRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("weightKg", record.weight.inKilograms)
            }
            is HeightRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("heightMeters", record.height.inMeters)
            }
            is BodyFatRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("percentage", record.percentage.value)
            }
            is LeanBodyMassRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("massKg", record.mass.inKilograms)
            }
            is BoneMassRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("massKg", record.mass.inKilograms)
            }
            is BodyWaterMassRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("massKg", record.mass.inKilograms)
            }
            is BasalMetabolicRateRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("basalMetabolicRateKcalPerDay", record.basalMetabolicRate.inKilocaloriesPerDay)
            }
            is Vo2MaxRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("vo2MillilitersPerMinuteKilogram", record.vo2MillilitersPerMinuteKilogram)
                json.addProperty("measurementMethod", record.measurementMethod)
            }
            is SleepSessionRecord -> {
                json.addProperty("startTime", record.startTime.toString())
                json.addProperty("startZoneOffset", record.startZoneOffset?.toString())
                json.addProperty("endTime", record.endTime.toString())
                json.addProperty("endZoneOffset", record.endZoneOffset?.toString())
                json.addProperty("title", record.title)
                json.addProperty("notes", record.notes)
                val stagesArray = JsonArray()
                record.stages.forEach { stage ->
                    val stageJson = JsonObject()
                    stageJson.addProperty("startTime", stage.startTime.toString())
                    stageJson.addProperty("endTime", stage.endTime.toString())
                    stageJson.addProperty("stage", stage.stage)
                    stagesArray.add(stageJson)
                }
                json.add("stages", stagesArray)
            }
            is ExerciseSessionRecord -> {
                json.addProperty("startTime", record.startTime.toString())
                json.addProperty("startZoneOffset", record.startZoneOffset?.toString())
                json.addProperty("endTime", record.endTime.toString())
                json.addProperty("endZoneOffset", record.endZoneOffset?.toString())
                json.addProperty("exerciseType", record.exerciseType)
                json.addProperty("title", record.title)
                json.addProperty("notes", record.notes)
                val lapsArray = JsonArray()
                record.laps.forEach { lap ->
                    val lapJson = JsonObject()
                    lapJson.addProperty("startTime", lap.startTime.toString())
                    lapJson.addProperty("endTime", lap.endTime.toString())
                    lapJson.addProperty("lengthMeters", lap.length?.inMeters)
                    lapsArray.add(lapJson)
                }
                json.add("laps", lapsArray)
                val segmentsArray = JsonArray()
                record.segments.forEach { segment ->
                    val segmentJson = JsonObject()
                    segmentJson.addProperty("startTime", segment.startTime.toString())
                    segmentJson.addProperty("endTime", segment.endTime.toString())
                    segmentJson.addProperty("segmentType", segment.segmentType)
                    segmentJson.addProperty("repetitions", segment.repetitions)
                    segmentsArray.add(segmentJson)
                }
                json.add("segments", segmentsArray)
            }
            is FloorsClimbedRecord -> {
                json.addProperty("startTime", record.startTime.toString())
                json.addProperty("startZoneOffset", record.startZoneOffset?.toString())
                json.addProperty("endTime", record.endTime.toString())
                json.addProperty("endZoneOffset", record.endZoneOffset?.toString())
                json.addProperty("floors", record.floors)
            }
            is ElevationGainedRecord -> {
                json.addProperty("startTime", record.startTime.toString())
                json.addProperty("startZoneOffset", record.startZoneOffset?.toString())
                json.addProperty("endTime", record.endTime.toString())
                json.addProperty("endZoneOffset", record.endZoneOffset?.toString())
                json.addProperty("elevationMeters", record.elevation.inMeters)
            }
            is WheelchairPushesRecord -> {
                json.addProperty("startTime", record.startTime.toString())
                json.addProperty("startZoneOffset", record.startZoneOffset?.toString())
                json.addProperty("endTime", record.endTime.toString())
                json.addProperty("endZoneOffset", record.endZoneOffset?.toString())
                json.addProperty("count", record.count)
            }
            is NutritionRecord -> {
                json.addProperty("startTime", record.startTime.toString())
                json.addProperty("startZoneOffset", record.startZoneOffset?.toString())
                json.addProperty("endTime", record.endTime.toString())
                json.addProperty("endZoneOffset", record.endZoneOffset?.toString())
                json.addProperty("name", record.name)
                json.addProperty("mealType", record.mealType)
                record.energy?.let { json.addProperty("energyKcal", it.inKilocalories) }
                record.protein?.let { json.addProperty("proteinGrams", it.inGrams) }
                record.totalCarbohydrate?.let { json.addProperty("totalCarbohydrateGrams", it.inGrams) }
                record.totalFat?.let { json.addProperty("totalFatGrams", it.inGrams) }
                record.dietaryFiber?.let { json.addProperty("dietaryFiberGrams", it.inGrams) }
                record.sugar?.let { json.addProperty("sugarGrams", it.inGrams) }
                record.sodium?.let { json.addProperty("sodiumGrams", it.inGrams) }
            }
            is HydrationRecord -> {
                json.addProperty("startTime", record.startTime.toString())
                json.addProperty("startZoneOffset", record.startZoneOffset?.toString())
                json.addProperty("endTime", record.endTime.toString())
                json.addProperty("endZoneOffset", record.endZoneOffset?.toString())
                json.addProperty("volumeLiters", record.volume.inLiters)
            }
            is CervicalMucusRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("appearance", record.appearance)
                json.addProperty("sensation", record.sensation)
            }
            is MenstruationFlowRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("flow", record.flow)
            }
            is MenstruationPeriodRecord -> {
                json.addProperty("startTime", record.startTime.toString())
                json.addProperty("startZoneOffset", record.startZoneOffset?.toString())
                json.addProperty("endTime", record.endTime.toString())
                json.addProperty("endZoneOffset", record.endZoneOffset?.toString())
            }
            is OvulationTestRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("result", record.result)
            }
            is SexualActivityRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("protectionUsed", record.protectionUsed)
            }
            is IntermenstrualBleedingRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
            }
            is BasalBodyTemperatureRecord -> {
                json.addProperty("time", record.time.toString())
                json.addProperty("zoneOffset", record.zoneOffset?.toString())
                json.addProperty("temperatureCelsius", record.temperature.inCelsius)
                json.addProperty("measurementLocation", record.measurementLocation)
            }
            else -> {}
        }

        return json
    }
}
