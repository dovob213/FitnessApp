package com.example.fitness.profile

import com.example.fitness.data.UserProfileRepository
import com.example.fitness.model.UserProfile

class ProfileManager(
    private val profileRepository: UserProfileRepository
) {

    suspend fun getProfile(userId: String): UserProfile? {
        return profileRepository.getUserProfile(userId)
    }

    suspend fun saveProfile(profile: UserProfile) {
        profileRepository.saveUserProfile(profile)
    }

    suspend fun updateProfile(profile: UserProfile) {
        profileRepository.updateUserProfile(profile)
    }

    fun calculateBMI(heightCm: Double, weightKg: Double): Double {
        if (heightCm <= 0 || weightKg <= 0) return 0.0
        val heightM = heightCm / 100.0
        return weightKg / (heightM * heightM)
    }

    fun getIdealWeight(heightCm: Double): Double {
        if (heightCm <= 0) return 0.0
        val heightM = heightCm / 100.0

        // 표준체중 = (키(m) x 키(m)) x 22
        return heightM * heightM * 22.0
    }
}