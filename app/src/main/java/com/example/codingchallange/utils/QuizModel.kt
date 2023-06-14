package com.example.codingchallange.utils

data class QuizModel(
    val questionText: String = "",
    val image: String = "",
    val alternatives: MutableList<String>,
    val correctAnswerIndex: Int = 0,
    val type: String? = null,
    val score: Int
)
