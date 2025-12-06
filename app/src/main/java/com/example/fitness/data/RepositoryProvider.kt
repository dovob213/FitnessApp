package com.example.fitness.data

import com.example.fitness.data.impl.FirebaseExerciseRepository
import com.example.fitness.data.impl.FirebaseRoutineRepository
import com.example.fitness.data.impl.FirebaseUserProfileRepository
import com.example.fitness.data.impl.FirebaseWorkoutRepository
import com.example.fitness.data.impl.MockExerciseRepository
import com.example.fitness.data.impl.MockWorkoutRepository
import com.example.fitness.data.impl.MockRoutineRepository
import com.example.fitness.data.impl.MockUserProfileRepository

object RepositoryProvider {

    private var exerciseRepo: ExerciseRepository? = null
    private var workoutRepo: WorkoutRepository? = null
    private var routineRepo: RoutineRepository? = null
    private var profileRepo: UserProfileRepository? = null

    // Firebase로 초기화 (Phase 2)
    fun initialize() {
        exerciseRepo = FirebaseExerciseRepository()
        workoutRepo = FirebaseWorkoutRepository()
        routineRepo = FirebaseRoutineRepository()
        profileRepo = FirebaseUserProfileRepository()

        println("✅ Firebase Repositories initialized")
    }

    // Mock으로 초기화 (Phase 1)
    fun initializeWithMock() {
        // Mock 구현체들은 다른 팀원들이 만듦
        exerciseRepo = MockExerciseRepository()
        workoutRepo = MockWorkoutRepository()
        routineRepo = MockRoutineRepository()
        profileRepo = MockUserProfileRepository()

        println("✅ Mock Repositories initialized")
    }

    fun getExerciseRepository(): ExerciseRepository {
        return exerciseRepo ?: throw IllegalStateException("Repository not initialized")
    }

    fun getWorkoutRepository(): WorkoutRepository {
        return workoutRepo ?: throw IllegalStateException("Repository not initialized")
    }

    fun getRoutineRepository(): RoutineRepository {
        return routineRepo ?: throw IllegalStateException("Repository not initialized")
    }

    fun getUserProfileRepository(): UserProfileRepository {
        return profileRepo ?: throw IllegalStateException("Repository not initialized")
    }
}