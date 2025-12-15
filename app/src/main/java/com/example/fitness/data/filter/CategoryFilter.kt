package com.example.fitness.data.filter

import com.example.fitness.model.Exercise
import com.example.fitness.model.ExerciseCategory

/**
 * 카테고리로 필터링
 */
class CategoryFilter(
    private val category: ExerciseCategory
) : ExerciseFilter {
    override fun matches(exercise: Exercise): Boolean {
        return exercise.category == category
    }
}