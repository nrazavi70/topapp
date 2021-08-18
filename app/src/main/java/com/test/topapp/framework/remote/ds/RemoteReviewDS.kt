package com.test.topapp.framework.remote.ds

import com.test.core.data.datasource.ReviewDS
import com.test.core.domain.Restaurant
import com.test.core.domain.Review
import com.test.core.domain.User
import com.test.topapp.framework.remote.api.ReviewService
import com.test.topapp.framework.remote.response.converter.toDomain
import com.test.topapp.framework.remote.util.handleError

class RemoteReviewDS(private val reviewService: ReviewService) : ReviewDS {
    override suspend fun addReview(user: User, review: Review) = try {
        Result.success(
            reviewService.create(
                user.id,
                user.token,
                review.rate,
                review.review,
                review.restaurantId
            ).toDomain()
        )
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    override suspend fun getAll(user: User, page: Int) = try {
        Result.success(
            reviewService.getReviews(user.id, user.token, page).map { it.toDomain() }
        )
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    override suspend fun getReview(user: User, review: Review) = try {
        Result.success(reviewService.getReview(user.id, user.token, review.id).toDomain())
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    override suspend fun getPendingReviews(user: User, page: Int) = try {
        Result.success(
            reviewService.getPendingReviews(user.id, user.token, page).map { it.toDomain() }
        )
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    override suspend fun getRestaurantReviews(user: User, restaurant: Restaurant, page: Int) = try {
        Result.success(
            reviewService.getRestaurantReviews(user.id, user.token, restaurant.id, page)
                .map { it.toDomain() }
        )
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    override suspend fun addResponseReview(user: User, review: Review) = try {
        reviewService.responseReview(user.id, user.token, review.id, review.response!!)
        Result.success(true)
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    override suspend fun deleteReview(user: User, review: Review) = try {
        reviewService.deleteReview(user.id, user.token, review.id)
        Result.success(true)
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }

    override suspend fun updateReview(user: User, review: Review) = try {
        reviewService.updateReview(
            user.id,
            user.token,
            review.id,
            review.response,
            review.rate,
            review.review
        )
        Result.success(true)
    } catch (e: Throwable) {
        Result.failure(handleError(e))
    }
}