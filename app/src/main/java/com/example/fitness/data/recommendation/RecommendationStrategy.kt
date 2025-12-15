package com.example.fitness.data.recommendation

import com.example.fitness.model.Exercise
import com.example.fitness.model.ExerciseCategory
import com.example.fitness.model.DifficultyLevel

/**
 * 추천 전략 인터페이스
 * - 다양한 추천 알고리즘을 통일된 방법으로 사용
 */
interface RecommendationStrategy {
    fun recommend(exercises: List<Exercise>, limit: Int): List<Exercise>
}

/**
 * 사용자 레벨 기반 추천
 * - 사용자 레벨에 맞는 운동 추천
 */
class LevelBasedRecommendation(
    private val userLevel: DifficultyLevel
) : RecommendationStrategy {

    override fun recommend(exercises: List<Exercise>, limit: Int): List<Exercise> {
        return exercises
            .filter { it.isRecommendedFor(userLevel) }
            .sortedBy { it.difficulty.level }
            .take(limit)
    }
}

/**
 * 카테고리 기반 추천
 * - 특정 카테고리의 운동 추천
 */
class CategoryBasedRecommendation(
    private val preferredCategory: ExerciseCategory
) : RecommendationStrategy {

    override fun recommend(exercises: List<Exercise>, limit: Int): List<Exercise> {
        return exercises
            .filter { it.category == preferredCategory }
            .take(limit)
    }
}

/**
 * 칼로리 소모량 기반 추천
 * - 칼로리를 많이 소모하는 운동 추천
 */
class CalorieBasedRecommendation(
    private val minCalories: Int
) : RecommendationStrategy {

    override fun recommend(exercises: List<Exercise>, limit: Int): List<Exercise> {
        return exercises
            .filter { it.caloriesPerSession >= minCalories }
            .sortedByDescending { it.caloriesPerSession }
            .take(limit)
    }
}

/**
 * 시간 기반 추천
 * - 제한된 시간 내에 할 수 있는 운동 추천
 */
class DurationBasedRecommendation(
    private val maxDuration: Int
) : RecommendationStrategy {

    override fun recommend(exercises: List<Exercise>, limit: Int): List<Exercise> {
        return exercises
            .filter { it.durationMinutes <= maxDuration }
            .sortedBy { it.durationMinutes }
            .take(limit)
    }
}