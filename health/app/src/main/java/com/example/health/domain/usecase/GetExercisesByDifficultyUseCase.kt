package com.example.health.domain.usecase

import com.example.health.data.model.Exercise
import com.example.health.data.model.DifficultyLevel
import com.example.health.data.repository.ExerciseRepository

/**
 * 난이도별 운동 조회 Use Case
 */
class GetExercisesByDifficultyUseCase(
    private val repository: ExerciseRepository
) {
    operator fun invoke(difficulty: DifficultyLevel): List<Exercise> {
        return repository.getExercisesByDifficulty(difficulty)
    }
}