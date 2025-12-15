package com.example.fitness

import kotlin.math.abs

object RecommendationManager {

    fun recommend(
        allExercises: List<Exercise>,
        userHistory: List<WorkoutLog>,
        userProfile: UserProfile
    ): List<Exercise> {
        val recentIds = userHistory.map { it.exerciseId }
        val userLevel = userProfile.level ?: 1

        return allExercises.sortedByDescending { exercise ->
            var score = 0

            if (exercise.difficulty.level == userLevel) score += 10
            else if (abs(exercise.difficulty.level - userLevel) == 1) score += 5


            if (!recentIds.contains(exercise.id)) score += 20

            score
        }
    }

    fun recommendByDuration(exercises: List<Exercise>, maxMinutes: Int): List<Exercise> {
        return exercises
            .filter { it.durationMinutes <= maxMinutes }
            .sortedByDescending { it.effect.caloriesBurned }
    }

    fun recommendByCalories(exercises: List<Exercise>, minCalories: Int): List<Exercise> {
        return exercises
            .filter { it.effect.caloriesBurned >= minCalories }
            .sortedByDescending { it.effect.caloriesBurned }
    }

    fun recommendByCategory(exercises: List<Exercise>, category: ExerciseCategory): List<Exercise> {
        return exercises
            .filter { it.category == category }
            .sortedBy { it.difficulty.level }
    }
}