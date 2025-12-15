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
    val exerciseId: String = "",       // 운동 종목 코드
    val date: Long = 0L,             // 날짜(밀리초로 구하기?)
    val sets: List<ExerciseSet> = emptyList(),   // 세트 목록
    val memo: String = "",       // 비고란
    val durationSeconds: Long = 0L  // 운동 시간 (초)
)


data class RoutineExercise(
    val exercise: Exercise,
    var targetSets: Int = 3,
    var targetReps: Int = 10,
    var targetWeight: Double = 0.0
)


data class Routine(
    val id: String = "",
    var name: String = "",
    val exercises: MutableList<RoutineExercise> = mutableListOf()
) {
    fun addExercise(exercise: Exercise, sets: Int, reps: Int, weight: Double) {
        exercises.add(RoutineExercise(exercise, sets, reps, weight))
    }
}


data class WeeklyPreset(
    val id: String,
    var name: String,
    val weeklySchedule: MutableMap<WeekDay, MutableList<Routine>> = mutableMapOf()
) {
    fun getRoutineDay(day: WeekDay): List<Routine> {
        return weeklySchedule[day] ?: emptyList()
    }

    fun addRoutine(day: WeekDay, routine: Routine) {
        val routinesForDay = weeklySchedule.getOrPut(day) { mutableListOf() }
        routinesForDay.add(routine)
    }
}