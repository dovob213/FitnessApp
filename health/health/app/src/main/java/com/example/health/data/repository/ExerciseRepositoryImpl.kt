package com.example.health.data.repository

import com.example.health.data.model.Exercise
import com.example.health.data.model.ExerciseCategory
import com.example.health.data.model.DifficultyLevel
import com.example.health.data.filter.ExerciseFilter

class ExerciseRepositoryImpl : ExerciseRepository {
    
    private val exercises = mutableListOf<Exercise>()
    
    init {
        loadSampleData()
    }
    
    // ... 다른 메서드들 ...
    
    /**
     * 여기에 운동 정보 추가!
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
                name = "목 스트레칭",
                category = ExerciseCategory.STRETCHING,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 10,
                caloriesPerSession = 30,
                targetMuscles = listOf("목", "어깨"),
                description = "장시간 앉아있을 때 경직된 목 근육 풀기"
            ),
            
            Exercise(
                id = "ex004",
                name = "하체 스트레칭",
                category = ExerciseCategory.STRETCHING,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 20,
                caloriesPerSession = 80,
                targetMuscles = listOf("햄스트링", "종아리", "엉덩이"),
                description = "하체 유연성을 높이는 스트레칭"
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
                description = "기본적인 상체 근력 운동"
            ),
            
            Exercise(
                id = "ex006",
                name = "변형 팔굽혀펴기",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.ADVANCED,
                durationMinutes = 15,
                caloriesPerSession = 180,
                targetMuscles = listOf("가슴", "삼두근", "어깨"),
                description = "다양한 각도의 팔굽혀펴기 응용 동작"
            ),
            
            Exercise(
                id = "ex007",
                name = "스쿼트",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 15,
                caloriesPerSession = 150,
                targetMuscles = listOf("대퇴사두근", "햄스트링", "둔근"),
                description = "하체 근력 강화의 기본 운동"
            ),
            
            Exercise(
                id = "ex008",
                name = "점프 스쿼트",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.ADVANCED,
                durationMinutes = 20,
                caloriesPerSession = 250,
                targetMuscles = listOf("대퇴사두근", "둔근", "종아리"),
                description = "폭발적인 하체 파워를 기르는 운동"
            ),
            
            Exercise(
                id = "ex009",
                name = "플랭크",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 10,
                caloriesPerSession = 80,
                targetMuscles = listOf("코어", "복근"),
                description = "코어 강화를 위한 기본 운동"
            ),
            
            Exercise(
                id = "ex010",
                name = "사이드 플랭크",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 15,
                caloriesPerSession = 100,
                targetMuscles = listOf("복사근", "코어"),
                description = "옆구리와 코어 강화 운동"
            ),
            
            Exercise(
                id = "ex011",
                name = "런지",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 20,
                caloriesPerSession = 180,
                targetMuscles = listOf("대퇴사두근", "둔근"),
                description = "균형감각과 하체 근력을 함께 키우는 운동"
            ),
            
            Exercise(
                id = "ex012",
                name = "덤벨 컬",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 15,
                caloriesPerSession = 90,
                targetMuscles = listOf("이두근", "전완근"),
                description = "팔 근력 강화 운동"
            ),
            
            Exercise(
                id = "ex013",
                name = "데드리프트",
                category = ExerciseCategory.STRENGTH,
                difficulty = DifficultyLevel.ADVANCED,
                durationMinutes = 25,
                caloriesPerSession = 220,
                targetMuscles = listOf("햄스트링", "등", "코어"),
                description = "전신 근력 강화의 핵심 운동"
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
                description = "전신 유산소 운동"
            ),
            
            Exercise(
                id = "ex015",
                name = "제자리 뛰기",
                category = ExerciseCategory.CARDIO,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 20,
                caloriesPerSession = 150,
                targetMuscles = listOf("하체", "심폐지구력"),
                description = "집에서 쉽게 할 수 있는 유산소 운동"
            ),
            
            Exercise(
                id = "ex016",
                name = "마운틴 클라이머",
                category = ExerciseCategory.CARDIO,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 15,
                caloriesPerSession = 180,
                targetMuscles = listOf("코어", "어깨", "하체"),
                description = "심박수를 높이는 전신 운동"
            ),
            
            Exercise(
                id = "ex017",
                name = "점핑잭",
                category = ExerciseCategory.CARDIO,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 15,
                caloriesPerSession = 120,
                targetMuscles = listOf("전신", "심폐지구력"),
                description = "몸을 깨우는 워밍업 유산소 운동"
            ),
            
            Exercise(
                id = "ex018",
                name = "하이니",
                category = ExerciseCategory.CARDIO,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 20,
                caloriesPerSession = 200,
                targetMuscles = listOf("대퇴사두근", "심폐지구력"),
                description = "무릎을 높이 올리며 달리기"
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
                description = "유연성 향상을 위한 기본 요가"
            ),
            
            Exercise(
                id = "ex020",
                name = "다운독 자세",
                category = ExerciseCategory.FLEXIBILITY,
                difficulty = DifficultyLevel.BEGINNER,
                durationMinutes = 10,
                caloriesPerSession = 40,
                targetMuscles = listOf("햄스트링", "종아리", "어깨"),
                description = "요가의 기본 자세로 전신 스트레칭"
            ),
            
            Exercise(
                id = "ex021",
                name = "비둘기 자세",
                category = ExerciseCategory.FLEXIBILITY,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 15,
                caloriesPerSession = 60,
                targetMuscles = listOf("엉덩이", "고관절"),
                description = "고관절 유연성을 높이는 요가 자세"
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
                description = "기본적인 균형 감각 훈련"
            ),
            
            Exercise(
                id = "ex023",
                name = "트리 자세",
                category = ExerciseCategory.BALANCE,
                difficulty = DifficultyLevel.INTERMEDIATE,
                durationMinutes = 15,
                caloriesPerSession = 50,
                targetMuscles = listOf("코어", "다리", "균형감각"),
                description = "요가의 나무 자세로 균형 감각 향상"
            ),
            
            Exercise(
                id = "ex024",
                name = "보수볼 스쿼트",
                category = ExerciseCategory.BALANCE,
                difficulty = DifficultyLevel.ADVANCED,
                durationMinutes = 20,
                caloriesPerSession = 140,
                targetMuscles = listOf("하체", "코어", "균형감각"),
                description = "불안정한 보수볼 위에서 스쿼트"
            )
        )
        
        exercises.addAll(sampleExercises)
    }
    
    // ... 나머지 Repository 메서드들 ...
}
