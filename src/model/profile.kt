package model

object userProfile {

    var height : Double? = null; private set
    var weight : Double? = null; private set
    var age : Int? = null; private set
    var level : Int? = null; private set
    var name : String? = null; private set
    var place : List<Place>? = null; private set
    var goal : Goal? = null; private set

    object update{
        fun height(height: Double?) = height?.let { userProfile.height = it }
        fun weight(weight: Double?) = weight?.let { userProfile.weight = it }
        fun age(age: Int?) = age?.let { userProfile.age = it }
        fun level(level: Int?) = level?.let { userProfile.level = it }
        fun name(name: String?) = name?.let { userProfile.name = it }
        fun place(place: List<Place>?) = place?.let { userProfile.place = it}
        fun goal(goal: Goal?) = goal?.let { userProfile.goal = it }
        }

}
