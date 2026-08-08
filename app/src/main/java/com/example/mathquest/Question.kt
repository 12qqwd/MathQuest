package com.example.mathquest

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val question: String,

    val answer: Int,

    val option1: Int,

    val option2: Int,

    val option3: Int,

    val option4: Int
)