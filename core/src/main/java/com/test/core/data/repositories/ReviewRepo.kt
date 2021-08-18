package com.test.core.data.repositories

import com.test.core.data.datasource.ReviewDS
import com.test.core.domain.Restaurant
import com.test.core.domain.Review
import com.test.core.domain.User

class ReviewRepo(private val remoteReviewDS: ReviewDS) {
    suspend fun addReview(user: User, review: Review) = remoteReviewDS.addReview(user, review)

    suspend fun getAll(user: User, page: Int) = remoteReviewDS.getAll(user, page)

    suspend fun getPendingReviews(user: User, page: Int) =
        remoteReviewDS.getPendingReviews(user, page)

    suspend fun getRestaurantReviews(user: User, restaurant: Restaurant, page: Int) =
        remoteReviewDS.getRestaurantReviews(user, restaurant, page)

    suspend fun getReview(user: User, review: Review) = remoteReviewDS.getReview(user, review)

    suspend fun deleteReview(user: User, review: Review) = remoteReviewDS.deleteReview(user, review)

    suspend fun updateReview(user: User, review: Review) = remoteReviewDS.updateReview(user, review)

    suspend fun addResponseReview(user: User, review: Review) =
        remoteReviewDS.addResponseReview(user, review)
}