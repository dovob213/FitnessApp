package com.example.health.data.filter

import com.example.health.data.model.Exercise
import com.example.health.data.model.ExerciseCategory
import com.example.health.data.model.DifficultyLevel

/**
 * 필터 인터페이스
 * - 전략 패턴의 Strategy 역할
 * - 다양한 필터링 방법을 통일된 인터페이스로 제공
 */
interface ExerciseFilter {
    fun matches(exercise: Exercise): Boolean
}

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