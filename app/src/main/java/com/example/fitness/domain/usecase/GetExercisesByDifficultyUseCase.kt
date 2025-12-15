package com.example.fitness.domain.usecase

import com.example.fitness.model.Exercise
import com.example.fitness.model.DifficultyLevel
import com.example.fitness.data.repository.ExerciseRepository

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