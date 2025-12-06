package com.example.fitness.data

import com.example.fitness.model.WorkoutLog

//운동 기록 '저장 및 불러오기' 규칙

interface WorkoutRepository {
    suspend fun getWorkoutLog(id: String): WorkoutLog?

    suspend fun getWorkoutLogs(     // 시작 - 종료일 가져와서, 기간별 기록 가져올 수 있게
        startDate: Long,
        endDate: Long
    ): List<WorkoutLog>

    // 기록 저장 및 삭제
    suspend fun saveWorkoutLog(log: WorkoutLog)
    suspend fun deleteWorkoutLog(id: String)

}

