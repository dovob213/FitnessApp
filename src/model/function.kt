package model

import kotlin.math.abs

//코틀린 내장기능 사용버전

fun distanceFilter(list: List<Exercise>,level: Double, distance: Double)=list.filter {it-> abs(it.level-level) <= distance}

fun levelSort(list: List<Exercise>,level:Double) = list.sortedBy { it -> abs(it.level - level) }

fun levelSort(list: List<Exercise>,level: Double, distance: Double): List<Exercise> {
    val filteredList = distanceFilter(list,level,distance)
    return levelSort(filteredList,level)
}

fun categoryFilter(list: List<Exercise>,category:String):List<Exercise> = list.filter {it-> it.category==category}

fun tagFilter(tag: String?): List<Exercise>? {
    if(tag==null) return null
    val input = tag.trim().lowercase()
    if(input.isEmpty()) return emptyList()
    val tagMatchCheck = (fun (checkTag: exerciseTags) = input.contains(checkTag.exerciseTag) || checkTag.exerciseTag.lowercase().contains(input))
    val exerciseTagFilter = (fun (exercise: Exercise) = exercise.tags.any(tagMatchCheck))
    return exerciseDB.getExerciseData().filter(exerciseTagFilter)
}
