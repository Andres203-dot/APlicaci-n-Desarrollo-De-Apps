package com.smartlens.actividad3.data.models

data class FormData(
    val id: String,
    val title: String,
    val details: String,
    val timestamp: Long = System.currentTimeMillis()
)
