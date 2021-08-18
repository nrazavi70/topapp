package com.test.topapp.framework.remote.response

data class UserResponse(
    val id: Int,
    val email: String,
    val role: String,
    val firstname: String,
    val lastname: String,
    val password: String,
    val creationAt: String,
    val enabled: Boolean,
    val token: String?
)