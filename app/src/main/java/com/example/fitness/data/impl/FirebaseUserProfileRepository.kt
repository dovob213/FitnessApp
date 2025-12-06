package com.example.fitness.data.impl


import com.google.firebase.firestore.FirebaseFirestore
import com.yourteam.fitnessapp.data.UserProfileRepository
import com.yourteam.fitnessapp.model.UserProfile
import kotlinx.coroutines.tasks.await

class FirebaseUserProfileRepository : UserProfileRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("user_profiles")

    override suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            collection.document(userId)
                .get()
                .await()
                .toObject(UserProfile::class.java)
        } catch (e: Exception) {
            println("Firebase Error: ${e.message}")
            null
        }
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        try {
            collection.document(profile.id)
                .set(profile)
                .await()
            println("Firebase: Profile saved - ${profile.id}")
        } catch (e: Exception) {
            println("Firebase Error: ${e.message}")
            throw e
        }
    }

    override suspend fun updateUserProfile(profile: UserProfile) {
        try {
            collection.document(profile.id)
                .set(profile)
                .await()
            println("Firebase: Profile updated - ${profile.id}")
        } catch (e: Exception) {
            println("Firebase Error: ${e.message}")
            throw e
        }
    }
}