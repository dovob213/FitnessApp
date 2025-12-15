package com.example.fitness.data.filter

import com.example.fitness.model.Exercise

/**
 * 검색어로 필터링
 */
class SearchQueryFilter(
    private val query: String
) : ExerciseFilter {
    override fun matches(exercise: Exercise): Boolean {
        return exercise.matchesSearchQuery(query)
    }
}