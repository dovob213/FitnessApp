package com.example.health.data.recommendation

import com.example.health.data.model.Exercise

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