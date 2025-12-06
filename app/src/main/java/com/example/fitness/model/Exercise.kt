package com.example.fitness.model

data class Exercise(
    val id: String = "",
    val name: String = "",
    val category: String = "",
    val description: String = "",
    val level: Double = 0.0,
    val tags: List<String> = emptyList(),
    val location: String = ""
)