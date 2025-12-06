package com.example.fitness.data

import com.example.fitness.model.Routine

interface RoutineRepository {
    // 루틴 1개 가져오기
    suspend fun getRoutine(id: String): Routine?
    // 모든 루틴 가져오기
    suspend fun getAllRoutines(): List<Routine>

    // 루틴 저장 및 삭제
    suspend fun saveRoutine(routine: Routine)
    suspend fun deleteRoutine(id: String)

}
