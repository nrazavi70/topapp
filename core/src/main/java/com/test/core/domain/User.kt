package com.test.core.domain

typealias Role = Int

data class User(
    val id: Int,
    val email: String,
    val role: Role = 0,
    val firstName: String? = null,
    val lastName: String? = null,
    val token: String = "",
    val password: String = ""
)
