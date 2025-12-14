package com.example.health.domain.usecase

import com.example.health.data.model.Exercise
import com.example.health.data.recommendation.RecommendationStrategy
import com.example.health.data.repository.ExerciseRepository

/**
 * 운동 추천 Use Case
 */
class GetRecommendedExercisesUseCase(
    private val repository: ExerciseRepository
) {
    operator fun invoke(
        strategy: RecommendationStrategy,
        limit: Int = 4
    ): List<Exercise> {
        val allExercises = repository.getAllExercises()
        return strategy.recommend(allExercises, limit)
    }
}