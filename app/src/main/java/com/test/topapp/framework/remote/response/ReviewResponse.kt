package com.test.topapp.framework.remote.response

data class ReviewResponse(
    val id: Int,
    val uId: Int,
    val rId: Int,
    val rate: Int,
    val review: String,
    val response: String?,
    val createdAt: String,
    val user: UserSafeResponse?
)