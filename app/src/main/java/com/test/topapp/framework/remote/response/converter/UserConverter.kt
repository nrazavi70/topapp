package com.test.topapp.framework.remote.response.converter

import com.test.core.domain.User
import com.test.topapp.framework.remote.response.UserResponse

fun UserResponse.toDomain() = User(
    this.id,
    this.email,
    when (this.role) {
        "OWNER" -> 1
        "ADMIN" -> 2
        else -> 0
    },
    this.firstname,
    this.lastname,
    this.token ?: ""
)