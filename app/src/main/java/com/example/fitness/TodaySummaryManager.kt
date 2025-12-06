package com.example.fitness

import com.example.fitness.data.WorkoutRepository
import java.util.Calendar

class TodaySummaryManager(
    private val workoutRepository: WorkoutRepository
) {

    suspend fun getTodaySummary(): TodaySummary {
        val today = getTodayRange()

        val logs = workoutRepository.getWorkoutLogs(
            startDate = today.first,
            endDate = today.second
        )

        val totalVolume = logs.sumOf { log ->
            log.sets.sumOf { set ->
                set.weight * set.reps
            }
        }

        return TodaySummary(
            workoutCount = logs.size,
            totalVolume = totalVolume,
            totalSets = logs.sumOf { it.sets.size }
        )
    }

    private fun getTodayRange(): Pair<Long, Long> {
        val calendar = Calendar.getInstance()

        // 오늘 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        // 오늘 23:59:59
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val endOfDay = calendar.timeInMillis

        return Pair(startOfDay, endOfDay)
    }
}

data class TodaySummary(
    val workoutCount: Int,
    val totalVolume: Double,
    val totalSets: Int
)