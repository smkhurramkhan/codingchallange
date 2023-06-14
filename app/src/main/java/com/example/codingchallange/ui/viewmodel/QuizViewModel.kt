package com.example.codingchallange.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.codingchallange.ui.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    val quiz = repository.getQuiz()
}