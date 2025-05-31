package com.example.vmush.model

data class KumbungModel(
    val name: String,
    val temperature: String,
    val humidity: String,
    val growth: String = "Normal",  // Dummy data for now
    val isActive: Boolean = true    // Dummy data for now
)
