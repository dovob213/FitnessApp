package com.example.health.data.recommendation

import com.example.health.data.model.Exercise

/**
 * 시간 기반 추천
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