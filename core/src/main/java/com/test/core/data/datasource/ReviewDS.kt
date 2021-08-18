package com.test.core.data.datasource

import com.test.core.domain.Restaurant
import com.test.core.domain.Review
import com.test.core.domain.User

interface ReviewDS {
    suspend fun addReview(user: User, review: Review): Result<Review>
    suspend fun getAll(user: User, page: Int): Result<List<Review>>
    suspend fun getReview(user: User, review: Review): Result<Review>
    suspend fun getPendingReviews(user: User, page: Int): Result<List<Review>>
    suspend fun getRestaurantReviews(user: User, restaurant: Restaurant, page: Int): Result<List<Review>>
    suspend fun deleteReview(user: User, review: Review): Result<Boolean>
    suspend fun updateReview(user: User, review: Review): Result<Boolean>
    suspend fun addResponseReview(user: User, review: Review): Result<Boolean>
}