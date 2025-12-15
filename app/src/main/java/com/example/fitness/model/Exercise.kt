package com.example.fitness.model


//
//import com.google.firebase.firestore.PropertyName
//
////운동 기록 '저장 및 불러오기' 규칙
//
//@PropertyName("name") var myName: String? = null
//@PropertyName("name") var myListStringg: List<String> = listOf("a", "b")
//@PropertyName("name") var myEXXXX:Exercise? = null

data class Exercise(
    var id: String = "",
    var name: String = "",
    var category: String = "",
    var description: String = "",
    var level: Double = 0.0,
    var tags: List<String> = emptyList(),
    var location: String = "",
    var imageUrl: String = ""  // 운동 사진 URL
)
