package com.example.health.data.recommendation

import com.example.health.data.model.Exercise
import com.example.health.data.model.DifficultyLevel

/**
 * 사용자 레벨 기반 추천
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