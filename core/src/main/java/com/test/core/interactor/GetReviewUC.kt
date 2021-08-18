package com.test.core.interactor

import com.test.core.data.repositories.ReviewRepo
import com.test.core.domain.Review
import com.test.core.domain.User
import com.test.core.interactor.base.BaseUseCase

class GetReviewUC(reviewRepo: ReviewRepo) :
    BaseUseCase<Pair<User, Review>, Review, ReviewRepo>(reviewRepo) {
    override suspend fun execute(parameter: Pair<User, Review>) =
        repo.getReview(parameter.first, parameter.second)
}