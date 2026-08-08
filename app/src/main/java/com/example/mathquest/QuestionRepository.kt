package com.example.mathquest

class QuestionRepository(
    private val dao: QuestionDao
) {

    suspend fun getQuestions(): List<Question> {
        return dao.getAllQuestions()
    }

    suspend fun addQuestion(question: Question) {
        dao.insertQuestion(question)
    }
}