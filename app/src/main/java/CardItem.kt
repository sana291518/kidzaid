package com.ssj.kidzaidssj


data class CardItem(
    val title: String,
    val content: String,
    val options: List<String>? = null, // ✅ Add multiple-choice options (for quizzes)
    val correctAnswerIndex: Int? = null // ✅ Store correct answer index (for quizzes)
)


