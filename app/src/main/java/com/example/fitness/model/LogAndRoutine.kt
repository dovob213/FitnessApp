package com.example.fitness.model


data class ExerciseSet(
    val weight: Double,   // 무게 (kg)
    val reps: Int,        // 횟수
    val completed: Boolean = true
)


data class WorkoutLog(
    val logId: String = "",
    val exerciseId: String,       // 어떤 운동을 했는지 (Exercise.id 참조)
    val date: Long,               // 날짜 (Timestamp)
    val sets: List<ExerciseSet>,  // 수행한 세트들
    val memo: String = ""
)


data class RoutineExercise(
    val exercise: Exercise,
    var targetSets: Int = 3,
    var targetReps: Int = 10,
    var targetWeight: Double = 0.0
)


data class Routine(
    val id: String,
    var name: String,
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