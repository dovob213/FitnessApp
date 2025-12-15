package com.example.fitness.data.repository

import com.example.fitness.model.Exercise
import com.example.fitness.model.ExerciseCategory
import com.example.fitness.model.DifficultyLevel
import com.example.fitness.model.ExerciseEffect
import com.example.fitness.data.filter.ExerciseFilter

class ExerciseRepositoryImpl : ExerciseRepository {

    private val exercises = mutableListOf<Exercise>()

    init {
        loadSampleData()
    }

    override fun getAllExercises(): List<Exercise> {
        return exercises.toList()
    }

    override fun getExerciseById(id: String): Exercise? {
        return exercises.find { it.id == id }
    }

    override fun searchExercises(filter: ExerciseFilter): List<Exercise> {
        return exercises.filter { filter.matches(it) }
    }

    override fun addExercise(exercise: Exercise) {
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

    // ⭐ 새로 추가된 메서드들
    override fun getExerciseEffect(exerciseId: String): ExerciseEffect? {
        return exercises.find { it.id == exerciseId }?.effect
    }

    override fun calculateTotalEffect(exerciseIds: List<String>): ExerciseEffect {
        val effects = exerciseIds.mapNotNull { getExerciseEffect(it) }

        return ExerciseEffect(
            muscleGrowth = effects.sumOf { it.muscleGrowth },
            enduranceGain = effects.sumOf { it.enduranceGain },
            flexibilityGain = effects.sumOf { it.flexibilityGain },
            caloriesBurned = effects.sumOf { it.caloriesBurned }
        )
    }

    /**
     * 샘플 데이터 로드
     * ⭐ 모든 운동에 effect 추가됨
     */
    private fun loadSampleData() {
        val sampleExercises = listOf(

            // ========== 스트레칭 운동 ==========
            Exercise(
                id = "ex001",
                name = "상체 스트레칭",
                category = ExerciseCategory.STRETCHING,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 60,
                caloriesPerSession = 1320,
                targetMuscles = listOf("어깨", "팔", "가슴"),
                description = "상체 근육을 풀어주는 기본 스트레칭",
                effect = ExerciseEffect(
                    muscleGrowth = 10,
                    enduranceGain = 20,
                    flexibilityGain = 80,
                    caloriesBurned = 1320
                )
            ),

            Exercise(
                id = "ex002",
                name = "전신 스트레칭",
                category = ExerciseCategory.STRETCHING,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 45,
                caloriesPerSession = 1450,
                targetMuscles = listOf("전신"),
                description = "몸 전체를 풀어주는 스트레칭",
                effect = ExerciseEffect(
                    muscleGrowth = 15,
                    enduranceGain = 25,
                    flexibilityGain = 90,
                    caloriesBurned = 1450
                )
            ),

            Exercise(
                id = "ex003",
                name = "목 스트레칭",
                category = ExerciseCategory.STRETCHING,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 10,
                caloriesPerSession = 30,
                targetMuscles = listOf("목", "어깨"),
                description = "장시간 앉아있을 때 경직된 목 근육 풀기",
                effect = ExerciseEffect(
                    muscleGrowth = 5,
                    enduranceGain = 10,
                    flexibilityGain = 60,
                    caloriesBurned = 30
                )
            ),

            Exercise(
                id = "ex004",
                name = "하체 스트레칭",
                category = ExerciseCategory.STRETCHING,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 20,
                caloriesPerSession = 80,
                targetMuscles = listOf("햄스트링", "종아리", "엉덩이"),
                description = "하체 유연성을 높이는 스트레칭",
                effect = ExerciseEffect(
                    muscleGrowth = 8,
                    enduranceGain = 15,
                    flexibilityGain = 75,
                    caloriesBurned = 80
                )
            ),

            // ========== 근력 운동 ==========
            Exercise(
                id = "ex005",
                name = "팔굽혀펴기",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 12,
                caloriesPerSession = 120,
                targetMuscles = listOf("가슴", "팔", "코어"),
                description = "기본적인 상체 근력 운동",
                effect = ExerciseEffect(
                    muscleGrowth = 70,
                    enduranceGain = 30,
                    flexibilityGain = 10,
                    caloriesBurned = 120
                )
            ),

            Exercise(
                id = "ex006",
                name = "변형 팔굽혀펴기",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.ADVANCED,
                durationMinutes = 15,
                caloriesPerSession = 180,
                targetMuscles = listOf("가슴", "삼두근", "어깨"),
                description = "다양한 각도의 팔굽혀펴기 응용 동작",
                effect = ExerciseEffect(
                    muscleGrowth = 85,
                    enduranceGain = 35,
                    flexibilityGain = 15,
                    caloriesBurned = 180
                )
            ),

            Exercise(
                id = "ex007",
                name = "스쿼트",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 15,
                caloriesPerSession = 150,
                targetMuscles = listOf("대퇴사두근", "햄스트링", "둔근"),
                description = "하체 근력 강화의 기본 운동",
                effect = ExerciseEffect(
                    muscleGrowth = 75,
                    enduranceGain = 40,
                    flexibilityGain = 20,
                    caloriesBurned = 150
                )
            ),

            Exercise(
                id = "ex008",
                name = "점프 스쿼트",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.ADVANCED,
                durationMinutes = 20,
                caloriesPerSession = 250,
                targetMuscles = listOf("대퇴사두근", "둔근", "종아리"),
                description = "폭발적인 하체 파워를 기르는 운동",
                effect = ExerciseEffect(
                    muscleGrowth = 80,
                    enduranceGain = 60,
                    flexibilityGain = 25,
                    caloriesBurned = 250
                )
            ),

            Exercise(
                id = "ex009",
                name = "플랭크",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 10,
                caloriesPerSession = 80,
                targetMuscles = listOf("코어", "복근"),
                description = "코어 강화를 위한 기본 운동",
                effect = ExerciseEffect(
                    muscleGrowth = 50,
                    enduranceGain = 40,
                    flexibilityGain = 15,
                    caloriesBurned = 80
                )
            ),

            Exercise(
                id = "ex010",
                name = "사이드 플랭크",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 15,
                caloriesPerSession = 100,
                targetMuscles = listOf("복사근", "코어"),
                description = "옆구리와 코어 강화 운동",
                effect = ExerciseEffect(
                    muscleGrowth = 60,
                    enduranceGain = 45,
                    flexibilityGain = 20,
                    caloriesBurned = 100
                )
            ),

            Exercise(
                id = "ex011",
                name = "런지",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 20,
                caloriesPerSession = 180,
                targetMuscles = listOf("대퇴사두근", "둔근"),
                description = "균형감각과 하체 근력을 함께 키우는 운동",
                effect = ExerciseEffect(
                    muscleGrowth = 65,
                    enduranceGain = 50,
                    flexibilityGain = 30,
                    caloriesBurned = 180
                )
            ),

            Exercise(
                id = "ex012",
                name = "덤벨 컬",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 15,
                caloriesPerSession = 90,
                targetMuscles = listOf("이두근", "전완근"),
                description = "팔 근력 강화 운동",
                effect = ExerciseEffect(
                    muscleGrowth = 55,
                    enduranceGain = 25,
                    flexibilityGain = 10,
                    caloriesBurned = 90
                )
            ),

            Exercise(
                id = "ex013",
                name = "데드리프트",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.ADVANCED,
                durationMinutes = 25,
                caloriesPerSession = 220,
                targetMuscles = listOf("햄스트링", "등", "코어"),
                description = "전신 근력 강화의 핵심 운동",
                effect = ExerciseEffect(
                    muscleGrowth = 90,
                    enduranceGain = 50,
                    flexibilityGain = 25,
                    caloriesBurned = 220
                )
            ),

            // ========== 유산소 운동 ==========
            Exercise(
                id = "ex014",
                name = "버피",
                category = ExerciseCategory.CARDIO,
                difficulty = DifficultyLevel.ADVANCED,
                durationMinutes = 15,
                caloriesPerSession = 200,
                targetMuscles = listOf("전신"),
                description = "전신 유산소 운동",
                effect = ExerciseEffect(
                    muscleGrowth = 30,
                    enduranceGain = 80,
                    flexibilityGain = 20,
                    caloriesBurned = 200
                )
            ),

            Exercise(
                id = "ex015",
                name = "제자리 뛰기",
                category = ExerciseCategory.CARDIO,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 20,
                caloriesPerSession = 150,
                targetMuscles = listOf("하체", "심폐지구력"),
                description = "집에서 쉽게 할 수 있는 유산소 운동",
                effect = ExerciseEffect(
                    muscleGrowth = 20,
                    enduranceGain = 70,
                    flexibilityGain = 15,
                    caloriesBurned = 150
                )
            ),

            Exercise(
                id = "ex016",
                name = "마운틴 클라이머",
                category = ExerciseCategory.CARDIO,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 15,
                caloriesPerSession = 180,
                targetMuscles = listOf("코어", "어깨", "하체"),
                description = "심박수를 높이는 전신 운동",
                effect = ExerciseEffect(
                    muscleGrowth = 35,
                    enduranceGain = 75,
                    flexibilityGain = 25,
                    caloriesBurned = 180
                )
            ),

            Exercise(
                id = "ex017",
                name = "점핑잭",
                category = ExerciseCategory.CARDIO,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 15,
                caloriesPerSession = 120,
                targetMuscles = listOf("전신", "심폐지구력"),
                description = "몸을 깨우는 워밍업 유산소 운동",
                effect = ExerciseEffect(
                    muscleGrowth = 15,
                    enduranceGain = 65,
                    flexibilityGain = 20,
                    caloriesBurned = 120
                )
            ),

            Exercise(
                id = "ex018",
                name = "하이니",
                category = ExerciseCategory.CARDIO,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 20,
                caloriesPerSession = 200,
                targetMuscles = listOf("대퇴사두근", "심폐지구력"),
                description = "무릎을 높이 올리며 달리기",
                effect = ExerciseEffect(
                    muscleGrowth = 25,
                    enduranceGain = 85,
                    flexibilityGain = 30,
                    caloriesBurned = 200
                )
            ),

            // ========== 유연성 운동 ==========
            Exercise(
                id = "ex019",
                name = "요가 기본",
                category = ExerciseCategory.FLEXIBILITY,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 30,
                caloriesPerSession = 100,
                targetMuscles = listOf("전신"),
                description = "유연성 향상을 위한 기본 요가",
                effect = ExerciseEffect(
                    muscleGrowth = 15,
                    enduranceGain = 30,
                    flexibilityGain = 85,
                    caloriesBurned = 100
                )
            ),

            Exercise(
                id = "ex020",
                name = "다운독 자세",
                category = ExerciseCategory.FLEXIBILITY,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 10,
                caloriesPerSession = 40,
                targetMuscles = listOf("햄스트링", "종아리", "어깨"),
                description = "요가의 기본 자세로 전신 스트레칭",
                effect = ExerciseEffect(
                    muscleGrowth = 10,
                    enduranceGain = 20,
                    flexibilityGain = 70,
                    caloriesBurned = 40
                )
            ),

            Exercise(
                id = "ex021",
                name = "비둘기 자세",
                category = ExerciseCategory.FLEXIBILITY,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 15,
                caloriesPerSession = 60,
                targetMuscles = listOf("엉덩이", "고관절"),
                description = "고관절 유연성을 높이는 요가 자세",
                effect = ExerciseEffect(
                    muscleGrowth = 12,
                    enduranceGain = 25,
                    flexibilityGain = 80,
                    caloriesBurned = 60
                )
            ),

            // ========== 밸런스 운동 ==========
            Exercise(
                id = "ex022",
                name = "한발 서기",
                category = ExerciseCategory.BALANCE,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 10,
                caloriesPerSession = 30,
                targetMuscles = listOf("코어", "발목", "균형감각"),
                description = "기본적인 균형 감각 훈련",
                effect = ExerciseEffect(
                    muscleGrowth = 20,
                    enduranceGain = 25,
                    flexibilityGain = 40,
                    caloriesBurned = 30
                )
            ),

            Exercise(
                id = "ex023",
                name = "트리 자세",
                category = ExerciseCategory.BALANCE,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 15,
                caloriesPerSession = 50,
                targetMuscles = listOf("코어", "다리", "균형감각"),
                description = "요가의 나무 자세로 균형 감각 향상",
                effect = ExerciseEffect(
                    muscleGrowth = 30,
                    enduranceGain = 35,
                    flexibilityGain = 55,
                    caloriesBurned = 50
                )
            ),

            Exercise(
                id = "ex024",
                name = "보수볼 스쿼트",
                category = ExerciseCategory.BALANCE,
                difficulty = DifficultyLevel.ADVANCED,
                durationMinutes = 20,
                caloriesPerSession = 140,
                targetMuscles = listOf("하체", "코어", "균형감각"),
                description = "불안정한 보수볼 위에서 스쿼트",
                effect = ExerciseEffect(
                    muscleGrowth = 60,
                    enduranceGain = 50,
                    flexibilityGain = 40,
                    caloriesBurned = 140
                )
            )
        )

        exercises.addAll(sampleExercises)
    }
}