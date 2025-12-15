package com.example.fitness.domain.usecase

import com.example.fitness.model.Exercise
import com.example.fitness.data.repository.ExerciseRepository

/**
 * ID로 운동 조회 Use Case
 */
class GetExerciseByIdUseCase(
    private val repository: ExerciseRepository
) {
    operator fun invoke(id: String): Exercise? {
        return repository.getExerciseById(id)
    }
}