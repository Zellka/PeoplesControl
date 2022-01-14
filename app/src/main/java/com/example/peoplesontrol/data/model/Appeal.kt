package com.example.peoplesontrol.data.model

data class Appeal(
    val id: Long,
    val user: String,
    val nameProblem: String,
    val address: String,
    val latitude: String,
    val longitude: String,
    val status: String,
    val media: String,
    val date: String,
    val description: String,
    val rating: Long,
    val numAppeal: Long
)