package com.example.health.data.model

/**
 * 운동 효과 데이터 클래스
 * - 승준님의 분석 파트에서 사용할 데이터
 */
data class ExerciseEffect(
    val muscleGrowth: Int,        // 근육 증가량 (0-100)
    val enduranceGain: Int,        // 지구력 증가량 (0-100)
    val flexibilityGain: Int,      // 유연성 증가량 (0-100)
    val caloriesBurned: Int        // 칼로리 소모량
)

/**
 * 운동 카테고리를 나타내는 열거형
 */
enum class ExerciseCategory(val displayName: String) {
    STRETCHING("스트레칭"),
    STRENGTH("근력"),
    CARDIO("유산소"),
    BALANCE("밸런스"),
    FLEXIBILITY("유연성");

    companion object {
        fun fromString(value: String): ExerciseCategory? {
            return values().find { it.displayName == value }
        }
    }
}

/**
 * 운동 난이도를 나타내는 열거형
 */
enum class DifficultyLevel(val displayName: String, val level: Int) {
    BEGINNER("초급", 1),
    INTERMEDIATE("중급", 2),
    ADVANCED("고급", 3);

    companion object {
        fun fromString(value: String): DifficultyLevel? {
            return values().find { it.displayName == value }
        }
    }
}

/**
 * 운동 정보를 담는 데이터 클래스
 * 불변 객체로 설계하여 스레드 안전성 확보
 */
data class Exercise(
    val id: String,
    val name: String,
    val category: ExerciseCategory,
    val difficulty: DifficultyLevel,
    val durationMinutes: Int,
    val caloriesPerSession: Int,
    val targetMuscles: List<String>,
    val imageUrl: String = "",
    val description: String = "",
    val videoUrl: String? = null,

    // ⭐ 새로 추가: 운동 효과 데이터
    val effect: ExerciseEffect
) {
    /**
     * 검색어와 매칭되는지 확인하는 메서드
     */
    fun matchesSearchQuery(query: String): Boolean {
        if (query.isBlank()) return true

        val lowerQuery = query.lowercase()
        return name.lowercase().contains(lowerQuery) ||
                description.lowercase().contains(lowerQuery) ||
                category.displayName.lowercase().contains(lowerQuery) ||
                targetMuscles.any { it.lowercase().contains(lowerQuery) }
    }

    /**
     * 사용자 레벨에 추천 가능한지 확인
     */
    fun isRecommendedFor(userLevel: DifficultyLevel): Boolean {
        return difficulty.level <= userLevel.level + 1
    }

    /**
     * 사용자 레벨에 적합한지 확인
     */
    fun isSuitableFor(userLevel: DifficultyLevel): Boolean {
        return difficulty == userLevel
    }
}