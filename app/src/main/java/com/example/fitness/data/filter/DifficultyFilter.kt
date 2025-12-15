package com.example.fitness.data.filter

import com.example.fitness.model.Exercise
import com.example.fitness.model.DifficultyLevel

/**
 * 난이도로 필터링
 */
class DifficultyFilter(
    private val difficulty: DifficultyLevel
) : ExerciseFilter {
    override fun matches(exercise: Exercise): Boolean {
        return exercise.difficulty == difficulty
    }
}