package com.example.fitness.data.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.example.fitness.data.ExerciseRepository
import com.example.fitness.model.Exercise
import kotlinx.coroutines.tasks.await

class FirebaseExerciseRepository : ExerciseRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("exercises")

    override suspend fun getExercise(id: String): Exercise? {
        return try {
            collection.document(id)
                .get()
                .await()
                .toObject(Exercise::class.java)
        } catch (e: Exception) {
            println("Firebase Error: ${e.message}")
            null
        }
    }

    override suspend fun getAllExercises(): List<Exercise> {
        return try {
            collection
                .get()
                .await()
                .toObjects(Exercise::class.java)
        } catch (e: Exception) {
            println("Firebase Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun saveExercise(exercise: Exercise) {
        try {
            collection.document(exercise.id)
                .set(exercise)
                .await()
            println("Firebase: Exercise saved - ${exercise.id}")
        } catch (e: Exception) {
            println("Firebase Error: ${e.message}")
            throw e
        }
    }
}