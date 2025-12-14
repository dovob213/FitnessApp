package com.example.health.data.filter

import com.example.health.data.model.Exercise
import com.example.health.data.model.DifficultyLevel

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