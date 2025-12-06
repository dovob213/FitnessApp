package com.example.fitness.data.impl

import com.example.fitness.data.WorkoutRepository
import com.example.fitness.model.WorkoutLog
import com.example.fitness.model.ExerciseSet


/* 파이어베이스에 실제 데이터 입력 구현 이전에,
    가라로 만들어 둔 샘플 데이터 파일입니다. 개발 때 테스트용. 나중에 지울 듯
 */


class MockWorkoutRepository : WorkoutRepository {

    private val workoutLogs = mutableListOf<WorkoutLog>()   // 앱 끄면 사라지게

    // 테스트용 가라 데이터입니다. 나중에 파이어베이스로 구현할 예정
    init {
        // 전날 운동 기록
        workoutLogs.add(
            WorkoutLog(
                id = "log001",
                exerciseId = "ex001",
                sets = listOf(
                    ExerciseSet(weight = 70.0, reps = 10, completed = true),
                    ExerciseSet(weight = 70.0, reps = 10, completed = true),
                    ExerciseSet(weight = 70.0, reps = 8, completed = true)
                ),
                date = System.currentTimeMillis() - 86400000, // 어제
                memo = "컨디션 좋았음"
            )
        )
        // 오늘 운동 기록
        workoutLogs.add(
            WorkoutLog(
                id = "log002",
                exerciseId = "ex002",
                sets = listOf(
                    ExerciseSet(weight = 100.0, reps = 8, completed = true),
                    ExerciseSet(weight = 100.0, reps = 8, completed = true),
                    ExerciseSet(weight = 100.0, reps = 6, completed = true)
                ),
                date = System.currentTimeMillis(),
                memo = "스쿼트 무거워짐"
            )
        )
    }

    // 기록 1개 가져오기
    override suspend fun getWorkoutLog(id: String): WorkoutLog? {
        return workoutLogs.find { it.id == id }
    }

    // 기간별 기록 가져오기
    override suspend fun getWorkoutLogs(
        startDate: Long,
        endDate: Long
    ): List<WorkoutLog> {
        return workoutLogs.filter { log ->
            log.date in startDate..endDate
        }.sortedByDescending { it.date } // 최신순 정렬
    }

    // 기록 저장하기
    override suspend fun saveWorkoutLog(log: WorkoutLog) {
        // 같은 ID가 있으면 업데이트, 없으면 추가
        val index = workoutLogs.indexOfFirst { it.id == log.id }
        if (index != -1) {
            workoutLogs[index] = log
            println("Mock: 운동 기록 업데이트 - ${log.id}")
        } else {
            workoutLogs.add(log)
            println("Mock: 운동 기록 추가 - ${log.id}")
        }
    }

    // 기록 삭제
    override suspend fun deleteWorkoutLog(id: String) {
        workoutLogs.removeIf { it.id == id }
        println("Mock: 운동 기록 삭제 - $id")
    }
}