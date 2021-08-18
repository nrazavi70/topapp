package com.test.core.interactor

import com.test.core.data.repositories.ReviewRepo
import com.test.core.domain.Restaurant
import com.test.core.domain.Review
import com.test.core.domain.User
import com.test.core.interactor.base.BaseUseCase

class GetRestaurantReviewsUC(reviewRepo: ReviewRepo) :
    BaseUseCase<Pair<Pair<User, Restaurant>, Int>, List<Review>, ReviewRepo>(reviewRepo) {
    override suspend fun execute(parameter: Pair<Pair<User, Restaurant>, Int>) =
        repo.getRestaurantReviews(parameter.first.first, parameter.first.second, parameter.second)
}