package com.test.topapp.presentation.restaurant.page

import com.test.core.domain.Restaurant
import com.test.core.domain.Review
import com.test.core.domain.User
import com.test.core.interactor.GetLocalUserUC
import com.test.core.interactor.GetRestaurantReviewsUC
import com.test.core.interactor.GetRestaurantUC
import com.test.topapp.framework.TopAppViewModel
import com.test.topapp.presentation.review.list.ReviewListAdapter
import com.test.topapp.utils.AppendPagination
import com.test.topapp.utils.Event

class RestaurantPageActivityViewModel(
    private val getLocalUserUC: GetLocalUserUC,
    private val getRestaurantUC: GetRestaurantUC,
    getRestaurantReviewsUC: GetRestaurantReviewsUC
) : TopAppViewModel() {
    var user: User? = null
    var restaurant: Restaurant? = null
    val reviews = AppendPagination(getRestaurantReviewsUC::invoke) {
        setEvent(Event.Error(setErrorAlert, it))
    }

    private suspend fun fetchRestaurant(user: User, restaurant: Restaurant) =
        getRestaurantUC(user to restaurant)
            .onSuccess {
                this.restaurant = it
                setEvent(Event.Info(restaurantReady))
            }
            .onFailure { setEvent(Event.Error(setErrorAlert, it)) }

    suspend fun start(userId: Int, restaurantId: Int) = getLocalUserUC(User(userId, "")).onSuccess {
        user = it
        val restaurant = Restaurant(restaurantId, userId, "")
        fetchRestaurant(it, restaurant)
        reviews.setFirstPair(it to restaurant).fetch()
    }

    override suspend fun viewEventListener(event: Event) {
        when (event) {
            is Event.Info -> when (event.type) {
                requestFetchNewReviews -> reviews.fetch()
                requestAction -> actionHandle()
                ReviewListAdapter.reviewClicked -> if (user!!.role == 1)
                    setEvent(Event.Info(routeToResponseReview, event.data))
                else if (user!!.role == 2)
                    setEvent(Event.Info(routeToManageReview, (event.data as Review).id))
            }
        }
    }

    private suspend fun actionHandle() = when (user?.role) {
        1, 2 -> setEvent(Event.Info(routeToManageRestaurant))
        else -> setEvent(Event.Info(routeToManageReview, -1))
    }

    companion object {
        const val setErrorAlert = 1
        const val restaurantReady = 2
        const val routeToResponseReview = 3
        const val requestAction = 4
        const val routeToManageReview = 5
        const val requestFetchNewReviews = 6
        const val routeToManageRestaurant = 7
        const val routeToHome = 8
    }
}