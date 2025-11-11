package model

open class BenchPress:Exercise(
    id="001",
    name = "Bench press",
    category=Goal.Diet.name,
    description = "Bench press",
    level=1.0,
    tags=listOf(exerciseTags.Chest),
    location = listOf(Place.Gym)
)

