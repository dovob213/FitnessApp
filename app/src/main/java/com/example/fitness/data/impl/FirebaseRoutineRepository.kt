package com.example.fitness.data.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.yourteam.fitnessapp.data.RoutineRepository
import com.yourteam.fitnessapp.model.Routine
import kotlinx.coroutines.tasks.await

class FirebaseRoutineRepository : RoutineRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("routines")

    override suspend fun getRoutine(id: String): Routine? {
        return try {
            collection.document(id)
                .get()
                .await()
                .toObject(Routine::class.java)
        } catch (e: Exception) {
            println("Firebase Error: ${e.message}")
            null
        }
    }

    override suspend fun getAllRoutines(): List<Routine> {
        return try {
            collection
                .get()
                .await()
                .toObjects(Routine::class.java)
        } catch (e: Exception) {
            println("Firebase Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun saveRoutine(routine: Routine) {
        try {
            collection.document(routine.id)
                .set(routine)
                .await()
            println("Firebase: Routine saved - ${routine.id}")
        } catch (e: Exception) {
            println("Firebase Error: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteRoutine(id: String) {
        try {
            collection.document(id)
                .delete()
                .await()
            println("Firebase: Routine deleted - $id")
        } catch (e: Exception) {
            println("Firebase Error: ${e.message}")
            throw e
        }
    }
}