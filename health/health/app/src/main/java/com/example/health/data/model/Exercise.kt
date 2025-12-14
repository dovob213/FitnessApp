package com.example.health.data.model

/**
 * 운동 카테고리를 나타내는 열거형
 * - enum class: 정해진 상수 값들의 집합
 * - displayName: 사용자에게 보여줄 한글 이름
 */
enum class ExerciseCategory(val displayName: String) {
    STRETCHING("스트레칭"),
    STRENGTH("근력"),
    CARDIO("유산소"),
    BALANCE("밸런스"),
    FLEXIBILITY("유연성");

    companion object {
        // 문자열로부터 ExerciseCategory 찾기
        fun fromString(value: String): ExerciseCategory? {
            return values().find { it.displayName == value }
        }
    }
}

/**
 * 운동 난이도를 나타내는 열거형
 * - level: 난이도를 숫자로 표현 (1=초급, 2=중급, 3=고급)
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
 * - data class: 데이터를 저장하는 용도의 클래스
 * - val: 불변(immutable) 프로퍼티 -> 한번 생성되면 변경 불가
 * - 왜 불변? -> 스레드 안전성, 예측 가능한 동작
 */
data class Exercise(
    val id: String,                          // 고유 식별자
    val name: String,                        // 운동 이름
    val category: ExerciseCategory,          // 카테고리
    val difficulty: DifficultyLevel,         // 난이도
    val durationMinutes: Int,                // 소요 시간(분)
    val caloriesPerSession: Int,             // 소모 칼로리
    val targetMuscles: List<String>,         // 타겟 근육 리스트
    val imageUrl: String = "",               // 이미지 URL (기본값 빈 문자열)
    val description: String = "",            // 설명
    val videoUrl: String? = null             // 비디오 URL (nullable)
) {
    /**
     * 검색어와 매칭되는지 확인하는 메서드
     * - 비즈니스 로직을 데이터 클래스 안에 캡슐화
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
     * - 사용자 레벨보다 1단계 높은 운동까지 추천 가능
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