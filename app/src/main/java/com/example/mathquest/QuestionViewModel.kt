package com.example.mathquest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuestionViewModel(
    private val repository: QuestionRepository
) : ViewModel() {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())

    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    fun loadQuestions() {
        viewModelScope.launch {

            var loadedQuestions = repository.getQuestions()

            if (loadedQuestions.isEmpty()) {

                val initialQuestions = listOf(

                    Question(
                        question = "What is 3 + 4?",
                        answer = 7,
                        option1 = 5,
                        option2 = 6,
                        option3 = 7,
                        option4 = 8
                    ),

                    Question(
                        question = "What is 5 + 6?",
                        answer = 11,
                        option1 = 9,
                        option2 = 10,
                        option3 = 11,
                        option4 = 12
                    ),

                    Question(
                        question = "What is 10 - 3?",
                        answer = 7,
                        option1 = 5,
                        option2 = 6,
                        option3 = 7,
                        option4 = 8
                    ),

                    Question(
                        question = "What is 4 × 3?",
                        answer = 12,
                        option1 = 9,
                        option2 = 10,
                        option3 = 12,
                        option4 = 14
                    ),

                    Question(
                        question = "What is 20 ÷ 4?",
                        answer = 5,
                        option1 = 3,
                        option2 = 4,
                        option3 = 5,
                        option4 = 6
                    )
                )

                initialQuestions.forEach { question ->
                    repository.addQuestion(question)
                }

                loadedQuestions = repository.getQuestions()
            }

            _questions.value = loadedQuestions
        }
    }
}