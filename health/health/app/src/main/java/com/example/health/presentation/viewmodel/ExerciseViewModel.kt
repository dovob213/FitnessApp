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

/**
 * 운동 검색/추천 화면의 ViewModel
 * - UI 상태 관리
 * - 비즈니스 로직과 UI 분리
 */
class ExerciseViewModel(
    private val searchUseCase: SearchExercisesUseCase,
    private val filterUseCase: FilterExercisesUseCase,
    private val recommendUseCase: GetRecommendedExercisesUseCase,
    private val getByIdUseCase: GetExerciseByIdUseCase
) : ViewModel() {

    // ===== LiveData 설정 =====
    // MutableLiveData: ViewModel 내부에서 수정 가능
    // LiveData: 외부에서는 읽기만 가능 (캡슐화)

    private val _exercises = MutableLiveData<List<Exercise>>()
    val exercises: LiveData<List<Exercise>> = _exercises

    private val _recommendedExercises = MutableLiveData<List<Exercise>>()
    val recommendedExercises: LiveData<List<Exercise>> = _recommendedExercises

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // ===== 현재 필터 상태 (private로 캡슐화) =====
    private var currentCategory: ExerciseCategory? = null
    private var currentDifficulty: DifficultyLevel? = null
    private var currentTargetMuscle: String? = null

    init {
        // ViewModel 생성 시 자동으로 모든 운동 로드
        loadAllExercises()
    }

    /**
     * 모든 운동 로드
     * - viewModelScope.launch: 코루틴으로 비동기 처리
     */
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

    /**
     * 검색어로 운동 검색
     */
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

    /**
     * 카테고리로 필터링
     */
    fun filterByCategory(category: ExerciseCategory?) {
        currentCategory = category
        loadAllExercises()
    }

    /**
     * 난이도로 필터링
     */
    fun filterByDifficulty(difficulty: DifficultyLevel?) {
        currentDifficulty = difficulty
        loadAllExercises()
    }

    /**
     * 타겟 근육으로 필터링
     */
    fun filterByTargetMuscle(muscle: String?) {
        currentTargetMuscle = muscle
        loadAllExercises()
    }

    /**
     * 사용자 레벨에 맞는 추천 운동 로드
     */
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

    /**
     * 카테고리 기반 추천
     */
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

    /**
     * 칼로리 기반 추천
     */
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

    /**
     * 시간 기반 추천
     */
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

    /**
     * 모든 필터 초기화
     */
    fun resetFilters() {
        currentCategory = null
        currentDifficulty = null
        currentTargetMuscle = null
        loadAllExercises()
    }

    /**
     * ID로 특정 운동 조회
     */
    fun getExerciseById(id: String): Exercise? {
        return getByIdUseCase(id)
    }
}