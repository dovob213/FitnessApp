package com.example.health.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.health.data.model.Exercise
import com.example.health.data.model.ExerciseCategory
import com.example.health.data.model.DifficultyLevel
import com.example.health.data.recommendation.*
import com.example.health.domain.usecase.*

class ExerciseViewModel(
    private val searchUseCase: SearchExercisesUseCase,
    private val filterUseCase: FilterExercisesUseCase,
    private val recommendUseCase: GetRecommendedExercisesUseCase,
    private val getByIdUseCase: GetExerciseByIdUseCase
) : ViewModel() {

    // LiveData
    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    private val _recommendedExercises = MutableLiveData<List<Exercise>>()
    val recommendedExercises: LiveData<List<Exercise>> = _recommendedExercises

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // 현재 필터 상태
    private var currentCategory: ExerciseCategory? = null
    private var currentDifficulty: DifficultyLevel? = null
    private var currentTargetMuscle: String? = null

    init {
        loadAllExercises()
    }

    fun loadAllExercises() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = filterUseCase(
                    category = currentCategory,
                    difficulty = currentDifficulty,
                    targetMuscle = currentTargetMuscle
                )
                _exercises.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = "운동 로드 실패: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchExercises(query: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = searchUseCase(query)
                _exercises.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = "검색 실패: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterByCategory(category: ExerciseCategory?) {
        currentCategory = category
        loadAllExercises()
    }

    fun filterByDifficulty(difficulty: DifficultyLevel?) {
        currentDifficulty = difficulty
        loadAllExercises()
    }

    fun filterByTargetMuscle(muscle: String?) {
        currentTargetMuscle = muscle
        loadAllExercises()
    }

    fun loadRecommendationsByLevel(userLevel: DifficultyLevel, limit: Int = 4) {
        viewModelScope.launch {
            try {
                val strategy = LevelBasedRecommendation(userLevel)
                val result = recommendUseCase(strategy, limit)
                _recommendedExercises.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = "추천 로드 실패: ${e.message}"
            }
        }
    }

    fun loadRecommendationsByCategory(category: ExerciseCategory, limit: Int = 4) {
        viewModelScope.launch {
            try {
                val strategy = CategoryBasedRecommendation(category)
                val result = recommendUseCase(strategy, limit)
                _recommendedExercises.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = "추천 로드 실패: ${e.message}"
            }
        }
    }

    fun loadRecommendationsByCalories(minCalories: Int, limit: Int = 4) {
        viewModelScope.launch {
            try {
                val strategy = CalorieBasedRecommendation(minCalories)
                val result = recommendUseCase(strategy, limit)
                _recommendedExercises.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = "추천 로드 실패: ${e.message}"
            }
        }
    }

    fun loadRecommendationsByDuration(maxDuration: Int, limit: Int = 4) {
        viewModelScope.launch {
            try {
                val strategy = DurationBasedRecommendation(maxDuration)
                val result = recommendUseCase(strategy, limit)
                _recommendedExercises.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = "추천 로드 실패: ${e.message}"
            }
        }
    }

    // ⭐ 새로 추가: 스마트 추천 (기록 + 시간 기반)
    /**
     * 운동 기록 기반 스마트 추천
     * - 승준님에게서 받은 userHistory 사용
     */
    fun loadSmartRecommendations(
        userLevel: DifficultyLevel,
        userHistory: List<String>,  // 승준님에게서 받을 데이터
        exerciseHistory: List<ExerciseHistory> = emptyList(),  // 승준님에게서 받을 데이터
        limit: Int = 4
    ) {
        viewModelScope.launch {
            try {
                val levelStrategy = LevelBasedRecommendation(userLevel)
                val historyStrategy = HistoryBasedRecommendation(userHistory)
                val timeStrategy = TimeBasedRecommendation(exerciseHistory)

                val hybridStrategy = HybridRecommendation(
                    levelStrategy = levelStrategy,
                    historyStrategy = historyStrategy,
                    timeStrategy = timeStrategy
                )

                val result = recommendUseCase(hybridStrategy, limit)
                _recommendedExercises.value = result
                _error.value = null
            } catch (e: Exception) {
                _error.value = "스마트 추천 로드 실패: ${e.message}"
            }
        }
    }

    fun resetFilters() {
        currentCategory = null
        currentDifficulty = null
        currentTargetMuscle = null
        loadAllExercises()
    }

    fun getExerciseById(id: String): Exercise? {
        return getByIdUseCase(id)
    }
}