package com.example.health.data.filter

import com.example.health.data.model.Exercise
import com.example.health.data.model.ExerciseCategory

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