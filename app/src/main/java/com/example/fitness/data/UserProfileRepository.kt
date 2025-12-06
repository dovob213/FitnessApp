package com.example.fitness.data

import com.example.fitness.model.UserProfile

interface UserProfileRepository {
    suspend fun getUserProfile(userId: String): UserProfile?
    suspend fun saveUserProfile(profile: UserProfile)
    suspend fun updateUserProfile(profile: UserProfile)
}