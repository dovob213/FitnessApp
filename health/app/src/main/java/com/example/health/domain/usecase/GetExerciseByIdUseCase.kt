package com.example.health.domain.usecase

import com.example.health.data.model.Exercise
import com.example.health.data.repository.ExerciseRepository

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