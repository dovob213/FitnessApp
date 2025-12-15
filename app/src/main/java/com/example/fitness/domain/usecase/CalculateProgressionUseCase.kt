package com.example.fitness.domain.usecase

import com.example.fitness.model.ExerciseEffect
import com.example.fitness.model.DifficultyLevel
import com.example.fitness.data.repository.ExerciseRepository
import com.example.fitness.domain.algorithm.ProgressionAlgorithm

/**
 * 운동 진행 증가량 계산 UseCase
 * - 승준님에게 전달할 기능
 */
class CalculateProgressionUseCase(
    private val repository: ExerciseRepository,
    private val algorithm: ProgressionAlgorithm
) {
    /**
     * 특정 운동의 증가량 계산
     */
    operator fun invoke(
        exerciseId: String,
        repetitions: Int,
        userLevel: DifficultyLevel
    ): ExerciseEffect? {

        val effect = repository.getExerciseEffect(exerciseId) ?: return null

        return algorithm.calculateProgression(effect, repetitions, userLevel)
    }

    /**
     * 여러 운동의 총 증가량 계산
     */
    fun calculateTotalProgression(
        exerciseIds: List<String>,
        repetitions: Int,
        userLevel: DifficultyLevel
    ): ExerciseEffect {

        val totalEffect = repository.calculateTotalEffect(exerciseIds)

        return algorithm.calculateProgression(totalEffect, repetitions, userLevel)
    }
}