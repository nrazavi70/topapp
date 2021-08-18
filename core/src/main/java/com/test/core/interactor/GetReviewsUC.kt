package com.test.core.interactor

import com.test.core.data.repositories.ReviewRepo
import com.test.core.domain.Review
import com.test.core.domain.User
import com.test.core.interactor.base.BaseUseCase

class GetReviewsUC(reviewRepo: ReviewRepo) :
    BaseUseCase<Pair<User, Int>, List<Review>, ReviewRepo>(reviewRepo) {
    override suspend fun execute(parameter: Pair<User, Int>) =
        repo.getAll(parameter.first, parameter.second)
}