package com.example.fitness.data.filter

import com.example.fitness.model.Exercise

/**
 * 타겟 근육으로 필터링
 */
class TargetMuscleFilter(
    private val targetMuscle: String
) : ExerciseFilter {
    override fun matches(exercise: Exercise): Boolean {
        return exercise.targetMuscles.any {
            it.lowercase().contains(targetMuscle.lowercase())
        }
    }
}