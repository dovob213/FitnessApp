package com.example.fitness.data.recommendation

import com.example.fitness.model.Exercise

/**
 * 칼로리 소모량 기반 추천
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