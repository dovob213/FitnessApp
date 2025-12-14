package com.example.health.domain.usecase

import com.example.health.data.model.Exercise
import com.example.health.data.filter.SearchQueryFilter
import com.example.health.data.repository.ExerciseRepository

/**
 * 운동 검색 Use Case
 */
class SearchExercisesUseCase(
    private val repository: ExerciseRepository
) {
    operator fun invoke(query: String): List<Exercise> {
        return when {
            query.isBlank() -> repository.getAllExercises()
            else -> {
                val filter = SearchQueryFilter(query)
                repository.searchExercises(filter)
            }
        }
    }
}