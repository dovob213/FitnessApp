package com.example.fitness.domain.algorithm

import com.example.fitness.model.ExerciseEffect
import com.example.fitness.model.DifficultyLevel

/**
 * 운동 진행에 따른 증가량 계산 알고리즘
 * - 승준님의 분석 파트에서 사용
 */
interface ProgressionAlgorithm {
    /**
     * 운동 효과를 기반으로 실제 증가량 계산
     * @param effect 운동의 기본 효과
     * @param repetitions 반복 횟수
     * @param userLevel 사용자 레벨
     * @return 실제 증가량
     */
    fun calculateProgression(
        effect: ExerciseEffect,
        repetitions: Int,
        userLevel: DifficultyLevel
    ): ExerciseEffect
}

/**
 * 기본 증가량 알고리즘 구현
 * - 초급자는 빨리 증가
 * - 고급자는 천천히 증가
 * - 반복할수록 한계효용 체감
 */
class DefaultProgressionAlgorithm : ProgressionAlgorithm {

    override fun calculateProgression(
        effect: ExerciseEffect,
        repetitions: Int,
        userLevel: DifficultyLevel
    ): ExerciseEffect {

        // 레벨에 따른 증가 배율
        val levelMultiplier = when (userLevel) {
            DifficultyLevel.BEGINNER -> 1.5      // 초급: 1.5배 빠른 성장
            DifficultyLevel.INTERMEDIATE -> 1.0   // 중급: 기본 성장
            DifficultyLevel.ADVANCED -> 0.7       // 고급: 0.7배 느린 성장
        }

        // 반복 횟수에 따른 감소 (한계효용 체감)
        // 많이 할수록 효과가 줄어듦
        val repetitionFactor = 1.0 - (repetitions * 0.01).coerceAtMost(0.5)

        val finalMultiplier = levelMultiplier * repetitionFactor

        return ExerciseEffect(
            muscleGrowth = (effect.muscleGrowth * finalMultiplier).toInt(),
            enduranceGain = (effect.enduranceGain * finalMultiplier).toInt(),
            flexibilityGain = (effect.flexibilityGain * finalMultiplier).toInt(),
            caloriesBurned = effect.caloriesBurned // 칼로리는 고정
        )
    }
}

/**
 * 선형 증가 알고리즘
 * - 단순하게 반복수만큼 비례해서 증가
 */
class LinearProgressionAlgorithm : ProgressionAlgorithm {

    override fun calculateProgression(
        effect: ExerciseEffect,
        repetitions: Int,
        userLevel: DifficultyLevel
    ): ExerciseEffect {
        return ExerciseEffect(
            muscleGrowth = effect.muscleGrowth * repetitions,
            enduranceGain = effect.enduranceGain * repetitions,
            flexibilityGain = effect.flexibilityGain * repetitions,
            caloriesBurned = effect.caloriesBurned * repetitions
        )
    }
}