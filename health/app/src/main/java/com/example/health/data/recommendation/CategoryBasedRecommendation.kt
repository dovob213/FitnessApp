package com.example.health.data.recommendation

import com.example.health.data.model.Exercise
import com.example.health.data.model.ExerciseCategory

/**
 * 카테고리 기반 추천
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