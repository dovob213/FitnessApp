package com.example.fitness.data

import com.example.fitness.model.Exercise


// 운동 정보 '가져오는' 규칙으로, 실제 구현은 나중에 파이어베이스로 구현 예정이요

interface ExerciseRepository {
    suspend fun getExercise(id: String): Exercise?

    suspend fun getAllExercises(): List<Exercise>

    suspend fun saveExercise(exercise: Exercise)
}


// 'suspend'로 나중에 파이어베이스 구현 시, 인터넷에서 데이터를 가져올 수 있게 할 수 있다네여

