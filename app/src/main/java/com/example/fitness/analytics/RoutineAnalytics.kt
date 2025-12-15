package com.example.fitness.analytics

import com.example.fitness.model.*
import kotlin.math.roundToInt

// 1. 분석 결과 데이터 모델 (UI에 보여줄 최종 값들)
data class RoutineAnalysisResult(
    val totalTimeMinutes: Int,       // 총 예상 소요 시간
    val totalCalories: Int,          // 총 예상 소모 칼로리
    val muscleImpact: Map<String, Int> // 부위별 자극 비중 (예: "허벅지" -> 60, "코어" -> 40)
)

object RoutineAnalyticsManager {

    // 표준 기준값 (데이터베이스의 기본 수치는 보통 3세트 10회를 기준으로 가정)
    private const val STANDARD_SETS = 3.0
    private const val STANDARD_REPS = 10.0
    private const val STANDARD_VOLUME = STANDARD_SETS * STANDARD_REPS

    /**
     * [기능 1] 단일 루틴 분석
     * 루틴 내의 운동 목록과 사용자가 설정한 세트/횟수를 기반으로 계산합니다.
     * 사용자가 세트 수를 바꾸면 이 함수를 다시 호출해서 UI를 갱신하면 됩니다.
     */
    fun analyzeRoutine(routine: Routine): RoutineAnalysisResult {
        var totalTime = 0.0
        var totalCalories = 0.0
        val muscleScoreMap = mutableMapOf<String, Double>()

        // 루틴에 포함된 각 운동(RoutineExercise)을 순회
        for (item in routine.exercises) {
            val exercise = item.exercise

            // [핵심 로직] 사용자의 설정(세트, 횟수)에 따른 가중치 계산
            // 예: 기본 운동정보가 3세트 기준인데, 사용자가 5세트로 설정하면 효과도 증가
            val userVolume = item.targetSets * item.targetReps
            val intensityRatio = if (userVolume > 0) userVolume / STANDARD_VOLUME else 1.0

            // 1. 시간 및 칼로리 합산 (비례식 적용)
            totalTime += exercise.durationMinutes * intensityRatio
            totalCalories += exercise.effect.caloriesBurned * intensityRatio

            // 2. 부위별 자극 점수 누적
            // 예: 스쿼트(하체)를 많이 할수록 하체 점수가 높아짐
            exercise.targetMuscles.forEach { muscle ->
                val currentScore = muscleScoreMap.getOrDefault(muscle, 0.0)
                // 운동 효과(muscleGrowth)도 가중치에 반영
                muscleScoreMap[muscle] = currentScore + (exercise.effect.muscleGrowth * intensityRatio)
            }
        }

        // 3. 부위별 점수를 백분율(%)로 변환 (도넛 차트나 막대 그래프용)
        val muscleDistribution = calculateMusclePercentage(muscleScoreMap)

        return RoutineAnalysisResult(
            totalTimeMinutes = totalTime.roundToInt(),
            totalCalories = totalCalories.roundToInt(),
            muscleImpact = muscleDistribution
        )
    }

    /**
     * [기능 2] 주간 프리셋 분석
     * 월~일요일에 배정된 모든 루틴을 합산하여 주간 총계를 냅니다.
     */
    fun analyzeWeeklyPreset(preset: WeeklyPreset): RoutineAnalysisResult {
        var weeklyTime = 0
        var weeklyCalories = 0
        // 주간 전체의 부위별 누적 점수
        val weeklyMuscleScoreMap = mutableMapOf<String, Double>()

        // 1. 요일별 순회
        WeekDay.values().forEach { day ->
            val routinesOfDay = preset.getRoutineDay(day) //

            // 2. 하루치 루틴 순회
            routinesOfDay.forEach { routine ->
                // 위에서 만든 단일 루틴 분석 로직 재사용
                val routineResult = analyzeRoutine(routine)

                weeklyTime += routineResult.totalTimeMinutes
                weeklyCalories += routineResult.totalCalories

                // 부위별 점수 합산 (단순 % 합산이 아니라 원점수 재계산 필요)
                // 여기서는 간단하게 분석 결과를 역산해서 누적하는 방식을 예시로 듦
                routineResult.muscleImpact.forEach { (muscle, percent) ->
                    // percent는 상대값이므로, 절대적인 운동량(시간)을 곱해서 가중치 복원
                    val weight = percent * routineResult.totalTimeMinutes
                    weeklyMuscleScoreMap[muscle] = weeklyMuscleScoreMap.getOrDefault(muscle, 0.0) + weight
                }
            }
        }

        // 3. 주간 부위별 분포 계산
        val weeklyMuscleDistribution = calculateMusclePercentage(weeklyMuscleScoreMap)

        return RoutineAnalysisResult(
            totalTimeMinutes = weeklyTime,
            totalCalories = weeklyCalories,
            muscleImpact = weeklyMuscleDistribution
        )
    }

    // [내부 함수] 점수 맵을 입력받아 퍼센트(%) 맵으로 변환
    private fun calculateMusclePercentage(scoreMap: Map<String, Double>): Map<String, Int> {
        val totalScore = scoreMap.values.sum()
        if (totalScore == 0.0) return emptyMap()

        // 점수 높은 순으로 정렬해서 상위 5개 정도만 보여주거나 전체 반환
        return scoreMap.entries
            .sortedByDescending { it.value }
            .associate { (muscle, score) ->
                muscle to (score / totalScore * 100).roundToInt()
            }
    }
}