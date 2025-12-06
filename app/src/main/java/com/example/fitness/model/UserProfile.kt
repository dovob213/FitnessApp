package com.example.fitness.model

data class UserProfile(
    val userId: String = "",
    var name: String = "",
    var age: Int = 0,
    var height: Double = 0.0,  // cm
    var weight: Double = 0.0,  // kg
    var level: Int = 0,
    var place: List<String> = emptyList(),
    var goal: String = ""
) {
    fun calculateBMI(): Double {
        if (height <= 0 || weight <= 0) return 0.0
        val heightInMeters = height / 100.0
        return weight / (heightInMeters * heightInMeters)
    }

    fun getBMICategory(): String {
        val bmi = calculateBMI()
        return when {
            bmi < 18.5 -> "저체중"
            bmi < 23.0 -> "정상"
            bmi < 25.0 -> "과체중"
            bmi < 30.0 -> "비만"
            else -> "고도비만"
        }
    }
}