package com.example.fitness.data.recommendation

import com.example.fitness.model.Exercise
import com.example.fitness.model.ExerciseCategory

/**
 * 운동 기록 기반 추천
 * - 사용자가 자주 한 카테고리 우선 추천
 * - 오래 안 한 카테고리도 추천 (균형)
 */
class HistoryBasedRecommendation(
    private val userHistory: List<String>, // 사용자가 한 운동 ID 리스트
    private val balanceRatio: Double = 0.3  // 30%는 안 한 카테고리에서
) : RecommendationStrategy {

    override fun recommend(exercises: List<Exercise>, limit: Int): List<Exercise> {

        // 자주 한 카테고리 찾기
        val categoryCount = mutableMapOf<ExerciseCategory, Int>()
        exercises.forEach { exercise ->
            if (userHistory.contains(exercise.id)) {
                categoryCount[exercise.category] =
                    categoryCount.getOrDefault(exercise.category, 0) + 1
            }
        }

        // 가장 많이 한 카테고리
        val favoriteCategory = categoryCount.maxByOrNull { it.value }?.key

        // 추천할 개수 계산
        val favoriteCount = (limit * (1 - balanceRatio)).toInt()
        val balanceCount = limit - favoriteCount

        val recommendations = mutableListOf<Exercise>()

        // 1. 자주 한 카테고리에서 추천
        favoriteCategory?.let { category ->
            exercises
                .filter { it.category == category && !userHistory.contains(it.id) }
                .take(favoriteCount)
                .let { recommendations.addAll(it) }
        }

        // 2. 안 한 카테고리에서 추천 (균형)
        exercises
            .filter { it.category != favoriteCategory && !userHistory.contains(it.id) }
            .take(balanceCount)
            .let { recommendations.addAll(it) }

        return recommendations.take(limit)
    }
}

/**
 * 운동 기록 데이터
 */
data class ExerciseHistory(
    val exerciseId: String,
    val lastPerformedDate: Long  // timestamp
)

/**
 * 시간 기반 추천
 * - 오래된 운동 우선 추천
 */
class TimeBasedRecommendation(
    private val history: List<ExerciseHistory>,
    private val daysSinceLastWorkout: Int = 7  // 7일 이상 안 한 운동 추천
) : RecommendationStrategy {

    override fun recommend(exercises: List<Exercise>, limit: Int): List<Exercise> {

        val currentTime = System.currentTimeMillis()
        val daysInMillis = daysSinceLastWorkout * 24 * 60 * 60 * 1000L

        // 오래 안 한 운동 찾기
        val oldExercises = exercises.filter { exercise ->
            val lastPerformed = history.find { it.exerciseId == exercise.id }

            if (lastPerformed == null) {
                // 한 번도 안 한 운동 - 우선 추천
                true
            } else {
                // 오래 안 한 운동
                currentTime - lastPerformed.lastPerformedDate > daysInMillis
            }
        }

        return oldExercises.take(limit)
    }
}

/**
 * 복합 추천 전략
 * - 레벨 + 기록 + 시간을 모두 고려
 */
class HybridRecommendation(
    private val levelStrategy: RecommendationStrategy,
    private val historyStrategy: RecommendationStrategy,
    private val timeStrategy: RecommendationStrategy,
    private val levelWeight: Double = 0.5,
    private val historyWeight: Double = 0.3,
    private val timeWeight: Double = 0.2
) : RecommendationStrategy {

    override fun recommend(exercises: List<Exercise>, limit: Int): List<Exercise> {

        val levelCount = (limit * levelWeight).toInt()
        val historyCount = (limit * historyWeight).toInt()
        val timeCount = limit - levelCount - historyCount

        val recommendations = mutableSetOf<Exercise>()

        // 레벨 기반 추천
        recommendations.addAll(levelStrategy.recommend(exercises, levelCount))

        // 기록 기반 추천
        recommendations.addAll(historyStrategy.recommend(exercises, historyCount))

        // 시간 기반 추천
        recommendations.addAll(timeStrategy.recommend(exercises, timeCount))

        return recommendations.take(limit)
    }
}