package model

class RoutineExercise(
    val exerciseId: String?,
    val targetSets: Int,
    val targetReps: Int,
    val targetWeight: Double
)

class Routine(
    val id: Int           // 루틴의 고유번호. 코드
    var name: String?               // '루틴'의 이름 -> '가슴 루틴' 이런거
    val exercises: List<RoutineExercise>?   // 포함된 운동들
)

val chestRoutine = Routine(
    id = 1,
    "가슴 루틴",
    exercises = listOf(
        RoutineExercise(
            exerciseId = "ex001",   // 벤치 프레스
            targetSets = 3,
            targetReps = 10,
            targetWeight = 60.0
        ),
        RoutineExercise(
            exerciseId = "ex002",   // 덤벨 프레스
            targetSets = 3,
            targetReps = 12,
            targetWeight = 20.0
        )
    )
)



