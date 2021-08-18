package com.test.topapp.presentation.review.response

import androidx.lifecycle.viewModelScope
import com.test.core.domain.Review
import com.test.core.domain.User
import com.test.core.interactor.GetLocalUserUC
import com.test.core.interactor.GetReviewUC
import com.test.core.interactor.ResponseReviewUC
import com.test.topapp.framework.TopAppViewModel
import com.test.topapp.utils.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ReviewResponseActivityViewModel(
    private val getLocalUserUC: GetLocalUserUC,
    private val getReviewUC: GetReviewUC,
    private val responseReviewUC: ResponseReviewUC
) : TopAppViewModel() {
    private var user: User? = null
    var review: Review? = null

    private suspend fun response(review: Review) {
        setEvent(Event.Info(setLoading, true))
        responseReviewUC(user!! to review)
            .onSuccess { setEvent(Event.Info(backToRestaurant)) }
            .onFailure { setEvent(Event.Error(setErrorAlert, it)) }
        setEvent(Event.Info(setLoading, false))
    }

    private suspend fun responseReview(review: Review) {
        if (review.response.isNullOrBlank()) {
            setEvent(
                Event.Error(setResponseInputError, Exception("Response to review can not empty"))
            )
            return
        }
        viewModelScope.launch { response(review) }
    }

    suspend fun start(userId: Int, reviewId: Int, restaurantId: Int) =
        getLocalUserUC(User(userId, "")).onSuccess {
            user = it
            setEvent(Event.Info(setLoading, true))
            getReviewUC(it to Review(reviewId, userId, restaurantId, 0, "", null))
                .onSuccess { rev ->
                    review = rev
                    setEvent(Event.Info(reviewReady))
                }.onFailure { e ->
                    setEvent(Event.Error(setErrorAlert, e))
                    delay(3000)
                    setEvent(Event.Info(routeToRestaurant))
                }
            setEvent(Event.Info(setLoading, false))
        }

    override suspend fun viewEventListener(event: Event) {
        when (event) {
            is Event.Info -> when (event.type) {
                requestResponseReview -> responseReview(event.data as Review)
            }
        }
    }

    companion object {
        const val reviewReady = 1
        const val requestResponseReview = 2
        const val setResponseInputError = 3
        const val setLoading = 4
        const val setErrorAlert = 5
        const val backToRestaurant = 6
        const val routeToRestaurant = 7
    }
}