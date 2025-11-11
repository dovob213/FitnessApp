package model

enum class exerciseTags(val exerciseTag: String) {
    Chest("가슴운동"),

}

enum class Place(val location: String) {
    Home("실내"),
    Gym("헬스장"),
    Outdoor("실외")
}

enum class Goal(val purpose: String) {
    Diet("다이어트"),
}