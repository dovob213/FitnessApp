package com.example.fitness.data.filter

import com.example.fitness.model.Exercise

/**
 * 여러 필터를 AND 조건으로 결합
 * - Composite 패턴 활용
 */
class CompositeFilter(
    private val filters: List<ExerciseFilter>
) : ExerciseFilter {
    override fun matches(exercise: Exercise): Boolean {
        // 모든 필터가 true를 반환해야 함
        return filters.all { it.matches(exercise) }
    }
}