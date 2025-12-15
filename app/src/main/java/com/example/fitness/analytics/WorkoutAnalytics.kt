package com.example.fitness.analytics

import com.example.fitness.model.*
import com.example.fitness.data.repository.ExerciseRepositoryImpl
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale

enum class AnalysisPeriod {
    WEEKLY, MONTHLY
}

data class AnalysisResult(
    val period: AnalysisPeriod,
    val totalTimeMinutes: Int,
    val totalCalories: Int,
    val workoutDaysCount: Int,
    val dailyCompletionRate: Map<String, Int>,
    val muscleDistribution: Map<String, Int>
)

// 2. 분석 매니저
object WorkoutAnalyticsManager {

    private val repository = ExerciseRepositoryImpl()

    fun analyze(
        logs: List<WorkoutLog>,
        period: AnalysisPeriod,
        preset: WeeklyPreset?
    ): AnalysisResult {

        val filteredLogs = filterLogsByPeriod(logs, period)

        // 2. 기본 통계 (시간, 칼로리) - 기존 로직 유지
        var totalTime = 0
        var totalCalories = 0
        val muscleCountMap = mutableMapOf<String, Int>()

        filteredLogs.forEach { log ->
            val exercise = repository.getExerciseById(log.exerciseId)
            if (exercise != null) {
                totalTime += exercise.durationMinutes
                totalCalories += exercise.effect.caloriesBurned

                val bodyPart = mapTargetMuscleToBodyPart(exercise.targetMuscles)
                muscleCountMap[bodyPart] = muscleCountMap.getOrDefault(bodyPart, 0) + 1
            }
        }

        // 3. 운동 일수
        val workoutDaysCount = filteredLogs.map {
            SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date(it.date))
        }.distinct().count()

        // 4. 부위별 비중
        val totalExercisesCount = filteredLogs.size
        val muscleDistribution = if (totalExercisesCount > 0) {
            muscleCountMap.mapValues { (_, count) ->
                (count.toDouble() / totalExercisesCount * 100).toInt()
            }
        } else {
            emptyMap()
        }

        // 5. [핵심] 요일별 진행률 계산 로직 호출
        val dailyRates = calculateDailyCompletion(filteredLogs, period, preset)

        return AnalysisResult(
            period = period,
            totalTimeMinutes = totalTime,
            totalCalories = totalCalories,
            workoutDaysCount = workoutDaysCount,
            dailyCompletionRate = dailyRates, // 변경된 데이터 할당
            muscleDistribution = muscleDistribution
        )
    }

    // --- [내부 로직] ---

    /**
     * 요일별로 (수행한 세트 수 / 목표 세트 수) * 100 을 계산합니다.
     */
    private fun calculateDailyCompletion(
        logs: List<WorkoutLog>,
        period: AnalysisPeriod,
        preset: WeeklyPreset?
    ): Map<String, Int> {
        val ratesMap = mutableMapOf<String, Int>()
        val calendar = Calendar.getInstance()

        // 주간 분석일 경우 월~일 루프
        if (period == AnalysisPeriod.WEEKLY) {
            val weekDays = WeekDay.values() // MON, TUE ... SUN

            weekDays.forEach { weekDay ->
                // 1. 해당 요일의 '목표량' 계산 (Target)
                // 프리셋에서 해당 요일에 배정된 루틴들을 가져옴
                val targetRoutines = preset?.getRoutineDay(weekDay) ?: emptyList()

                // 해당 요일의 총 목표 세트 수 합산
                // (Routine -> RoutineExercise -> targetSets)
                var totalTargetSets = 0
                targetRoutines.forEach { routine -> routine.exercises.forEach { targetItem ->
                        totalTargetSets += targetItem.targetSets
                    }
                }

                // 2. 해당 요일의 '수행량' 계산 (Actual)
                // 로그들 중에서 해당 요일에 해당하는 것만 필터링
                // (실제로는 날짜 매칭 로직이 더 정교해야 하지만, 개념적으로 작성)
                val logsForDay = logs.filter { log ->
                    calendar.timeInMillis = log.date
                    // Calendar의 요일 상수(Calendar.MONDAY 등)와 WeekDay Enum을 매핑하는 함수 필요
                    isSameDay(calendar, weekDay)
                }

                // 수행한 총 세트 수 합산
                val totalPerformedSets = logsForDay.sumOf { it.sets.size }

                // 3. 퍼센트 계산
                val percentage = if (totalTargetSets > 0) {
                    val calc = (totalPerformedSets.toDouble() / totalTargetSets * 100).toInt()
                    // 100%를 넘으면 100으로 고정할지, 120%로 보여줄지는 선택 (여기선 100 제한)
                    if (calc > 100) 100 else calc
                } else {
                    // 목표가 없는데 운동을 했다면? -> 추가 운동으로 간주하거나 0 처리
                    if (totalPerformedSets > 0) 100 else 0
                }

                // 결과 맵에 저장 (Key: "MON", Value: 85)
                ratesMap[weekDay.name] = percentage
            }
        }

        // 월간 분석 로직은 날짜별(1일~31일)로 확장 가능
        return ratesMap
    }

    // Calendar의 요일과 우리의 WeekDay Enum이 같은지 확인하는 헬퍼 함수
    private fun isSameDay(cal: Calendar, weekDay: WeekDay): Boolean {
        val calDay = cal.get(Calendar.DAY_OF_WEEK) // 1:Sun, 2:Mon, ... 7:Sat
        return when (weekDay) {
            WeekDay.SUN -> calDay == Calendar.SUNDAY
            WeekDay.MON -> calDay == Calendar.MONDAY
            WeekDay.TUE -> calDay == Calendar.TUESDAY
            WeekDay.WED -> calDay == Calendar.WEDNESDAY
            WeekDay.THU -> calDay == Calendar.THURSDAY
            WeekDay.FRI -> calDay == Calendar.FRIDAY
            WeekDay.SAT -> calDay == Calendar.SATURDAY
        }
    }

    // (기존 filterLogsByPeriod, mapTargetMuscleToBodyPart 함수들은 동일하므로 생략)
    private fun filterLogsByPeriod(logs: List<WorkoutLog>, period: AnalysisPeriod): List<WorkoutLog> {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis

        // 이번 주/달의 시작 지점 계산
        when (period) {
            AnalysisPeriod.WEEKLY -> {
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
            }
            AnalysisPeriod.MONTHLY -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
            }
        }
        val startTime = calendar.timeInMillis

        // 해당 기간 내의 로그만 반환
        return logs.filter { it.date in startTime..currentTime }
    }

    private fun mapTargetMuscleToBodyPart(targetMuscles: List<String>): String {
        // 데이터에 있는 타겟 부위 문자열을 기반으로 분류
        val mainTarget = targetMuscles.firstOrNull() ?: "기타"

        return when {
            mainTarget.contains("가슴") || mainTarget.contains("어깨") || mainTarget.contains("팔") || mainTarget.contains(
                "등"
            ) -> "상체"

            mainTarget.contains("하체") || mainTarget.contains("다리") || mainTarget.contains("허벅지") || mainTarget.contains(
                "종아리"
            ) -> "하체"

            mainTarget.contains("복근") || mainTarget.contains("코어") -> "코어"
            mainTarget.contains("전신") -> "전신"
            else -> "기타"
        }
    }
}