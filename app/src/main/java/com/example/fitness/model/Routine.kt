package com.example.fitness.model

data class RoutineExercise(
    val exercise: Exercise,
    var targetSets: Int,
    var targetReps: Int,
    var targetWeight: Double
)

data class Routine(
    val id: String = "",
    var name: String = "",
    val exercises: MutableList<RoutineExercise> = mutableListOf()
) {
    fun addExercise(exercise: Exercise, sets: Int, reps: Int, weight: Double) {
        val newRoutineItem = RoutineExercise(exercise, sets, reps, weight)
        exercises.add(newRoutineItem)
    }
}