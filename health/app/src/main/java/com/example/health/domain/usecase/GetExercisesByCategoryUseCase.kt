package com.example.health.domain.usecase

import com.example.health.data.model.Exercise
import com.example.health.data.model.ExerciseCategory
import com.example.health.data.repository.ExerciseRepository

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