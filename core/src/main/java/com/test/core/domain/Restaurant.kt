package com.test.core.domain

data class Restaurant(
    val id: Int,
    val ownerId: Int,
    val name: String,
    val locLAT: Float = 0.0F,
    val locLNG: Float = 0.0F,
    val address: String = "",
    val averageRate: Float = 0.0F,
    val reviewCount: Int = 0,
    val lowestRev: List<Review> = listOf(),
    val highestRev: List<Review> = listOf()
)