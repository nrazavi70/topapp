package com.test.topapp.presentation.user.owner

import com.test.core.domain.User
import com.test.core.interactor.GetPendingReviewsUC
import com.test.core.interactor.GetUserRestaurantsUC
import com.test.core.interactor.GetLocalUserUC
import com.test.topapp.framework.TopAppViewModel
import com.test.topapp.presentation.restaurant.list.RestaurantListAdapter
import com.test.topapp.utils.AppendPagination
import com.test.topapp.utils.Event

class UserOwnerActivityViewModel(
    private val getLocalUserUC: GetLocalUserUC,
    private val getUserRestaurantsUC: GetUserRestaurantsUC,
    private val getPendingReviewsUC: GetPendingReviewsUC
) : TopAppViewModel() {
    var user: User? = null
    var restaurants =
        AppendPagination(getUserRestaurantsUC::invoke) { setEvent(Event.Error(setErrorAlert, it)) }
    var reviews =
        AppendPagination(getPendingReviewsUC::invoke) { setEvent(Event.Error(setErrorAlert, it)) }

    suspend fun start(userId: Int) = getLocalUserUC(User(userId, "")).onSuccess {
        user = it
        restaurants.setFirstPair(it).fetch()
        reviews.setFirstPair(it).fetch()
        setEvent(Event.Info(userLoaded))
    }

    override suspend fun viewEventListener(event: Event) {
        when (event) {
            is Event.Info -> when (event.type) {
                requestShowRestaurant -> setEvent(Event.Info(showRestaurant))
                requestShowReviews -> setEvent(Event.Info(showReviews))
                requestRouteToManageRestaurant -> setEvent(Event.Info(routeToManageRestaurant))
                requestFetchNewRestaurant -> restaurants.fetch()
                requestFetchNewReview -> reviews.fetch()
                RestaurantListAdapter.restaurantClicked ->
                    setEvent(Event.Info(routeToRestaurant, event.data))
            }
        }
    }

    companion object {
        const val requestShowRestaurant = 1
        const val requestShowReviews = 2
        const val showRestaurant = 3
        const val showReviews = 4
        const val requestRouteToManageRestaurant = 5
        const val routeToManageRestaurant = 6
        const val setErrorAlert = 7
        const val userLoaded = 8
        const val requestFetchNewRestaurant = 9
        const val routeToRestaurant = 10
        const val requestFetchNewReview = 11
    }
}