package com.test.topapp.presentation.review.manage

import androidx.lifecycle.viewModelScope
import com.test.core.domain.Review
import com.test.core.domain.User
import com.test.core.interactor.*
import com.test.topapp.framework.TopAppViewModel
import com.test.topapp.utils.Event
import com.test.topapp.utils.modalalert.ModalAlertModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ReviewManageActivityViewModel(
    private val getLocalUserUC: GetLocalUserUC,
    private val getReviewUC: GetReviewUC,
    private val createReviewUC: CreateReviewUC,
    private val reviewEditUC: ReviewEditUC,
    private val reviewDeleteUC: ReviewDeleteUC
) : TopAppViewModel() {
    private var isEdit = false
    private var user: User? = null
    private var selectedReview: Review? = null

    private suspend fun create(review: Review) {
        setEvent(Event.Info(setLoading, true))
        createReviewUC(user!! to review)
            .onSuccess { setEvent(Event.Info(routeToRestaurant)) }
            .onFailure { setEvent(Event.Error(setErrorAlert, it)) }
        setEvent(Event.Info(setLoading, false))
    }

    private suspend fun update(review: Review) {
        setEvent(Event.Info(setLoading, true))
        reviewEditUC(user!! to review)
            .onSuccess { setEvent(Event.Info(routeToRestaurant)) }
            .onFailure { setEvent(Event.Error(setErrorAlert, it)) }
        setEvent(Event.Info(setLoading, false))
    }

    private suspend fun delete() {
        setEvent(Event.Info(setLoading, true))
        reviewDeleteUC(user!! to selectedReview!!)
            .onSuccess { setEvent(Event.Info(routeToRestaurant)) }
            .onFailure { setEvent(Event.Error(setErrorAlert, it)) }
        setEvent(Event.Info(setLoading, false))
    }

    private suspend fun saveReview(review: Review) {
        if (review.review.isBlank()) {
            setEvent(Event.Error(setMessageInputError, Exception("Review can not empty")))
            return
        }
        viewModelScope.launch { if (isEdit) update(review) else create(review) }
    }

    private suspend fun fetchReview(reviewId: Int) {
        setEvent(Event.Info(setLoading, true))
        getReviewUC(user!! to Review(reviewId, -1, -1, 0, "", null))
            .onSuccess {
                selectedReview = it
                setEvent(Event.Info(reviewReady, it))
            }
            .onFailure {
                setEvent(Event.Error(setErrorAlert, it))
                delay(3000)
                setEvent(Event.Info(routeToRestaurant))
            }
        setEvent(Event.Info(setLoading, false))
    }

    suspend fun start(userId: Int, reviewId: Int, restaurantId: Int) =
        getLocalUserUC(User(userId, "")).onSuccess {
            user = it
            if (reviewId >= 0) {
                fetchReview(reviewId)
                isEdit = true
            } else setEvent(
                Event.Info(
                    reviewReady,
                    Review(0, userId, restaurantId, 1, "", null)
                )
            )
        }

    private suspend fun changeRate(input: Pair<Review, Int>) {
        val rev = input.first
        setEvent(
            Event.Info(
                reviewReady,
                Review(rev.id, rev.userId, rev.restaurantId, input.second, rev.review, rev.response)
            )
        )
    }

    private suspend fun deleteReviewRequest() = setEvent(
        Event.Info(
            createModalAlert,
            ModalAlertModel("Delete warning", "Are you sure to delete this review?") {
                viewModelScope.launch { delete() }
            }
        )
    )

    override suspend fun viewEventListener(event: Event) {
        when (event) {
            is Event.Info -> when (event.type) {
                requestChangeRate -> changeRate(event.data as Pair<Review, Int>)
                requestSaveReview -> saveReview(event.data as Review)
                requestDeleteReview -> deleteReviewRequest()
            }
        }
    }

    companion object {
        const val requestChangeRate = 1
        const val requestDeleteReview = 2
        const val requestSaveReview = 3
        const val setErrorAlert = 4
        const val setLoading = 5
        const val setMessageInputError = 6
        const val routeToRestaurant = 7
        const val reviewReady = 8
        const val createModalAlert = 9
    }
}