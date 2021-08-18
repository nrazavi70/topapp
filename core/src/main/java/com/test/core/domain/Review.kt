package com.test.core.domain

data class Review(
    val id: Int,
    val userId: Int,
    val restaurantId: Int,
    val rate: Int,
    val review: String,
    val response: String?,
    val userFirstName: String? = "",
    val userLastName: String? = ""
)