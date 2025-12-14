package com.example.health.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.health.data.repository.ExerciseRepository
import com.example.health.domain.usecase.*

/**
 * ExerciseViewModel을 생성하는 Factory
 * - ViewModel에 의존성을 주입하기 위해 필요
 * - Factory 패턴 적용
 */
class ExerciseViewModelFactory(
    private val repository: ExerciseRepository
) : ViewModelProvider.Factory {

    /**
     * ViewModel 생성 메서드
     * - ViewModelProvider가 자동으로 호출
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // ExerciseViewModel 타입인지 확인
        if (modelClass.isAssignableFrom(ExerciseViewModel::class.java)) {
            // Use Case들을 생성하고 ViewModel에 전달
            @Suppress("UNCHECKED_CAST")
            return ExerciseViewModel(
                searchUseCase = SearchExercisesUseCase(repository),
                filterUseCase = FilterExercisesUseCase(repository),
                recommendUseCase = GetRecommendedExercisesUseCase(repository),
                getByIdUseCase = GetExerciseByIdUseCase(repository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}