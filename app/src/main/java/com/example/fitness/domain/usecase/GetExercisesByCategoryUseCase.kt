package com.example.fitness.domain.usecase

import com.example.fitness.model.Exercise
import com.example.fitness.model.ExerciseCategory
import com.example.fitness.data.repository.ExerciseRepository

/**
 * 카테고리별 운동 조회 Use Case
 */
class GetExercisesByCategoryUseCase(
    private val repository: ExerciseRepository
) {
    operator fun invoke(category: ExerciseCategory): List<Exercise> {
        return repository.getExercisesByCategory(category)
    }
}