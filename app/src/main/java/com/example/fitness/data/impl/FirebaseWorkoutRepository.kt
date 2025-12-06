package com.yourteam.fitnessapp.data.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yourteam.fitnessapp.data.WorkoutRepository
import com.yourteam.fitnessapp.model.WorkoutLog
import kotlinx.coroutines.tasks.await

class FirebaseWorkoutRepository : WorkoutRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("workout_logs")

    override suspend fun getWorkoutLog(id: String): WorkoutLog? {
        return try {
            collection.document(id)
                .get()
                .await()
                .toObject(WorkoutLog::class.java)
        } catch (e: Exception) {
            println("Firebase Error: ${e.message}")
            null
        }
    }

    override suspend fun getWorkoutLogs(
        startDate: Long,
        endDate: Long
    ): List<WorkoutLog> {
        return try {
            collection
                .whereGreaterThanOrEqualTo("date", startDate)
                .whereLessThanOrEqualTo("date", endDate)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects(WorkoutLog::class.java)
        } catch (e: Exception) {
            println("Firebase Error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun saveWorkoutLog(log: WorkoutLog) {
        try {
            collection.document(log.id)
                .set(log)
                .await()
            println("Firebase: WorkoutLog saved - ${log.id}")
        } catch (e: Exception) {
            println("Firebase Error: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteWorkoutLog(id: String) {
        try {
            collection.document(id)
                .delete()
                .await()
            println("Firebase: WorkoutLog deleted - $id")
        } catch (e: Exception) {
            println("Firebase Error: ${e.message}")
            throw e
        }
    }
}