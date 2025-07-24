// Question.kt
package com.ssj.kidzaidssj

data class HygieneQuestion(
    val questionText: String,
    val questionImageRes: Int,
    val correctOption: Int,
    val correctAnswerImageRes: Int,
    val options: List<String> // List of options for the question
)

data class SafetyQuestion(
    val questionText: String,
    val questionImageRes: Int,
    val correctOption: Int,
    val correctAnswerImageRes: Int,
    val options: List<String> // List of options for the question
)

data class NosebleedsQuestion(
    val questionText: String,
    val questionImageRes: Int,
    val correctOption: Int,
    val correctAnswerImageRes: Int,
    val options: List<String> // List of options for the question
)

data class BurnsQuestion(
    val questionText: String,
    val questionImageRes: Int,
    val correctOption: Int,
    val correctAnswerImageRes: Int,
    val options: List<String> // List of options for the question
)

data class SprainQuestion(
    val questionText: String,
    val questionImageRes: Int,
    val correctOption: Int,
    val correctAnswerImageRes: Int,
    val options: List<String> // List of options for the question
)

data class SplintersQuestion(
    val questionText: String,
    val questionImageRes: Int,
    val correctOption: Int,
    val correctAnswerImageRes: Int,
    val options: List<String> // List of options for the question
)

data class InsectQuestion(
    val questionText: String,
    val questionImageRes: Int,
    val correctOption: Int,
    val correctAnswerImageRes: Int,
    val options: List<String> // List of options for the question
)