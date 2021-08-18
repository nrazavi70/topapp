package com.test.topapp.framework.remote.response.converter

import com.test.core.domain.Review
import com.test.topapp.framework.remote.response.ReviewResponse

fun ReviewResponse.toDomain() = Review(
    this.id,
    this.uId,
    this.rId,
    this.rate,
    this.review,
    this.response,
    this.user?.firstname,
    this.user?.lastname
)