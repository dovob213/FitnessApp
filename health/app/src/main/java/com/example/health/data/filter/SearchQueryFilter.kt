package com.example.health.data.filter

import com.example.health.data.model.Exercise

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