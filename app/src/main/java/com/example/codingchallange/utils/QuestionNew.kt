package com.example.codingchallange.utils

import androidx.annotation.Keep

@Keep
data class QuestionNew(
    val correct_answers: List<Int>,
    val lable: String,
    val options: List<Option>
)