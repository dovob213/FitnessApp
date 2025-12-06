package com.example.fitness.model

// 운동 몇 키로 몇 개 몇 세트 했는지 기록하기


// '1세트'의 정보
data class ExerciseSet(
    val weight: Double,   // 무게(kg)
    val reps: Int,         // 횟수(개)
    val completed: Boolean = true
)

data class WorkoutLog(
    val id: String = "",          // 고유번호. 코드
    val exerciseId: String,       // 운동 종목 코드
    val date: Long,             // 날짜(밀리초로 구하기?)
    val sets: List<ExerciseSet>,   // 세트 목록
    val memo: String = ""       // 비고란
)


// 사용하는 예시.
// 무게, 횟수, 세트 수 등의 ExerciseSet 변경은 추후 아래와 같이 구현할 수 있을듯?

val todayBenchPress = WorkoutLog(
    id = "log001",
    exerciseId = "ex001",
    date = System.currentTimeMillis(),
    sets = listOf(
        ExerciseSet(weight = 60.0, reps = 10),
        ExerciseSet(weight = 65.0, reps = 8),
        ExerciseSet(weight = 70.0, reps = 6)
    ),
    memo = "임시 텍스트 ( \" ex) 오늘 컨디션이 좋았다 \" ) "
)

