package com.example.health.data.repository

import com.example.health.data.model.Exercise
import com.example.health.data.model.ExerciseCategory
import com.example.health.data.model.DifficultyLevel
import com.example.health.data.model.ExerciseEffect
import com.example.health.data.filter.ExerciseFilter

/**
 * Repository 인터페이스
 * - 데이터 접근을 추상화
 * - 의존성 역전 원칙(DIP): 구체적 구현이 아닌 인터페이스에 의존
 */
interface ExerciseRepository {
    fun getAllExercises(): List<Exercise>
    fun getExerciseById(id: String): Exercise?
    fun searchExercises(filter: ExerciseFilter): List<Exercise>
    fun addExercise(exercise: Exercise)
    fun getExercisesByCategory(category: ExerciseCategory): List<Exercise>
    fun getExercisesByDifficulty(difficulty: DifficultyLevel): List<Exercise>

    // ⭐ 승준님 분석 파트를 위한 메서드 추가
    /**
     * 운동 ID로 효과 데이터 가져오기
     * 승준님의 분석/루틴 파트에서 사용
     */
    fun getExerciseEffect(exerciseId: String): ExerciseEffect?

    /**
     * 여러 운동의 총 효과 계산
     */
    fun calculateTotalEffect(exerciseIds: List<String>): ExerciseEffect
}