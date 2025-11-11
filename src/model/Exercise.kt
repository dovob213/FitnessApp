package model

open class Exercise(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val level: Double,
    val tags: List<exerciseTags>,
    val location: List<Place>
)

object exerciseDB{
    private val exerciseData = arrayListOf<Exercise>(
        BenchPress(),
    )
    fun getExerciseData(): List<Exercise> = exerciseData.toList()

    fun userUpdate(exerciseData: Exercise) {
        this.exerciseData.add(exerciseData)
    }
}
