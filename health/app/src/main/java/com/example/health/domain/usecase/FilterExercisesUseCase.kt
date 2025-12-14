package com.example.health.domain.usecase

import com.example.health.data.model.Exercise
import com.example.health.data.model.ExerciseCategory
import com.example.health.data.model.DifficultyLevel
import com.example.health.data.filter.*
import com.example.health.data.recommendation.RecommendationStrategy
import com.example.health.data.repository.ExerciseRepository

/**
 * 운동 검색 Use Case
 * - 하나의 책임: 운동 검색 기능만 담당
 * - operator fun invoke(): 객체를 함수처럼 호출 가능
 */
class SearchExercisesUseCase(
    private val repository: ExerciseRepository
) {
    // invoke를 정의하면 객체를 함수처럼 사용 가능
    // 예: searchUseCase("팔굽혀펴기") 처럼 호출
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

/**
 * 운동 필터링 Use Case
 * - 복합 필터링 로직 처리
 */
class FilterExercisesUseCase(
    private val repository: ExerciseRepository
) {
    operator fun invoke(
        category: ExerciseCategory? = null,
        difficulty: DifficultyLevel? = null,
        targetMuscle: String? = null
    ): List<Exercise> {
        // 필터 리스트 생성
        val filters = mutableListOf<ExerciseFilter>()

        // null이 아닌 경우만 필터 추가 (?.let 사용)
        category?.let { filters.add(CategoryFilter(it)) }
        difficulty?.let { filters.add(DifficultyFilter(it)) }
        targetMuscle?.let { filters.add(TargetMuscleFilter(it)) }

        return when {
            filters.isEmpty() -> repository.getAllExercises()
            else -> {
                val compositeFilter = CompositeFilter(filters)
                repository.searchExercises(compositeFilter)
            }
        }
    }
}

/**
 * 운동 추천 Use Case
 * - 다양한 추천 전략 적용
 */
class GetRecommendedExercisesUseCase(
    private val repository: ExerciseRepository
) {
    operator fun invoke(
        strategy: RecommendationStrategy,
        limit: Int = 4
    ): List<Exercise> {
        val allExercises = repository.getAllExercises()
        return strategy.recommend(allExercises, limit)
    }
}

/**
 * ID로 운동 조회 Use Case
 * - 단순하지만 독립적인 Use Case로 분리
 */
class GetExerciseByIdUseCase(
    private val repository: ExerciseRepository
) {
    operator fun invoke(id: String): Exercise? {
        return repository.getExerciseById(id)
    }
}

/**
 * 카테고리별 운동 조회 Use Case
 */
class GetExercisesByCategoryUseCase(
    private val repository: ExerciseRepository
) {
    operator fun invoke(category: ExerciseCategory): List<Exercise> {
        return repository.getExercisesByCategory(category)
    }
}

/**
 * 난이도별 운동 조회 Use Case
 */
class GetExercisesByDifficultyUseCase(
    private val repository: ExerciseRepository
) {
    operator fun invoke(difficulty: DifficultyLevel): List<Exercise> {
        return repository.getExercisesByDifficulty(difficulty)
    }
}