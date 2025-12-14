package com.example.health.data.repository

import com.example.health.data.model.Exercise
import com.example.health.data.model.ExerciseCategory
import com.example.health.data.model.DifficultyLevel
import com.example.health.data.filter.ExerciseFilter

/**
 * Repository 인터페이스
 * - 데이터 접근을 추상화
 * - 의존성 역전 원칙(DIP): 구체적 구현이 아닌 인터페이스에 의존
 */
interface ExerciseRepository {
    fun getAllExercises(): List<Exercise>
    fun getExerciseById(id: String): Exercise?
    fun searchExercises(filter: ExerciseFilter): List<Exercise>
    fun addExercise(exercise: Exercise)
    fun getExercisesByCategory(category: ExerciseCategory): List<Exercise>
    fun getExercisesByDifficulty(difficulty: DifficultyLevel): List<Exercise>
}

/**
 * Repository 구현체
 * - 실제 프로젝트에서는 Room DB, Firebase 등을 사용
 * - 지금은 메모리에 데이터 저장
 */
class ExerciseRepositoryImpl : ExerciseRepository {

    // private val로 외부 접근 차단 (캡슐화)
    private val exercises = mutableListOf<Exercise>()

    init {
        // 초기화 블록에서 샘플 데이터 로드
        loadSampleData()
    }

    override fun getAllExercises(): List<Exercise> {
        // toList()로 방어적 복사 - 외부에서 리스트 변경 불가
        return exercises.toList()
    }

    override fun getExerciseById(id: String): Exercise? {
        // find: 조건에 맞는 첫 번째 요소 반환 (없으면 null)
        return exercises.find { it.id == id }
    }

    override fun searchExercises(filter: ExerciseFilter): List<Exercise> {
        // filter 람다: 조건에 맞는 요소들만 반환
        return exercises.filter { filter.matches(it) }
    }

    override fun addExercise(exercise: Exercise) {
        // 중복 체크 후 추가
        if (exercises.none { it.id == exercise.id }) {
            exercises.add(exercise)
        }
    }

    override fun getExercisesByCategory(category: ExerciseCategory): List<Exercise> {
        return exercises.filter { it.category == category }
    }

    override fun getExercisesByDifficulty(difficulty: DifficultyLevel): List<Exercise> {
        return exercises.filter { it.difficulty == difficulty }
    }

    /**
     * 샘플 데이터 로드
     * - private: 클래스 내부에서만 사용
     */
    private fun loadSampleData() {
        val sampleExercises = listOf(
            Exercise(
                id = "ex001",
                name = "상체 스트레칭",
                category = ExerciseCategory.STRETCHING,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 60,
                caloriesPerSession = 1320,
                targetMuscles = listOf("어깨", "팔", "가슴"),
                description = "상체 근육을 풀어주는 기본 스트레칭"
            ),
            Exercise(
                id = "ex002",
                name = "전신 스트레칭",
                category = ExerciseCategory.STRETCHING,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 45,
                caloriesPerSession = 1450,
                targetMuscles = listOf("전신"),
                description = "몸 전체를 풀어주는 스트레칭"
            ),
            Exercise(
                id = "ex003",
                name = "팔굽혀펴기",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 12,
                caloriesPerSession = 120,
                targetMuscles = listOf("가슴", "팔", "코어"),
                description = "기본적인 상체 근력 운동"
            ),
            Exercise(
                id = "ex004",
                name = "스쿼트",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 15,
                caloriesPerSession = 150,
                targetMuscles = listOf("대퇴사두근", "햄스트링", "둔근"),
                description = "하체 근력 강화의 기본 운동"
            ),
            Exercise(
                id = "ex005",
                name = "플랭크",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 10,
                caloriesPerSession = 80,
                targetMuscles = listOf("코어", "복근"),
                description = "코어 강화를 위한 기본 운동"
            ),
            Exercise(
                id = "ex006",
                name = "런지",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 20,
                caloriesPerSession = 180,
                targetMuscles = listOf("대퇴사두근", "둔근"),
                description = "균형감각과 하체 근력을 함께 키우는 운동"
            ),
            Exercise(
                id = "ex007",
                name = "버피",
                category = ExerciseCategory.CARDIO,
                difficulty = DifficultyLevel.ADVANCED,
                durationMinutes = 15,
                caloriesPerSession = 200,
                targetMuscles = listOf("전신"),
                description = "전신 유산소 운동"
            ),
            Exercise(
                id = "ex008",
                name = "요가 기본",
                category = ExerciseCategory.FLEXIBILITY,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 30,
                caloriesPerSession = 100,
                targetMuscles = listOf("전신"),
                description = "유연성 향상을 위한 기본 요가"
            )
        )

        exercises.addAll(sampleExercises)
    }
}