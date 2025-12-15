package com.example.fitness.domain.usecase

import com.example.fitness.model.Exercise
import com.example.fitness.data.recommendation.RecommendationStrategy
import com.example.fitness.data.repository.ExerciseRepository

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