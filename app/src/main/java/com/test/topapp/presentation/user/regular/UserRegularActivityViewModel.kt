package com.test.topapp.presentation.user.regular

import com.test.core.domain.User
import com.test.core.interactor.GetUserRestaurantsUC
import com.test.core.interactor.GetLocalUserUC
import com.test.topapp.framework.TopAppViewModel
import com.test.topapp.presentation.restaurant.list.RestaurantListAdapter
import com.test.topapp.utils.AppendPagination
import com.test.topapp.utils.Event

class UserRegularActivityViewModel(
    private val getLocalUserUC: GetLocalUserUC,
    private val getUserRestaurantsUC: GetUserRestaurantsUC
) : TopAppViewModel() {
    var user: User? = null
    val restaurants =
        AppendPagination(getUserRestaurantsUC::invoke) { setEvent(Event.Error(setErrorAlert, it)) }

    suspend fun start(userId: Int) = getLocalUserUC(User(userId, "")).onSuccess {
        user = it
        restaurants.setFirstPair(it).fetch()
        setEvent(Event.Info(userLoaded))
    }

    override suspend fun viewEventListener(event: Event) {
        when (event) {
            is Event.Info -> when (event.type) {
                requestFetchNewRestaurant -> restaurants.fetch()
                RestaurantListAdapter.restaurantClicked ->
                    setEvent(Event.Info(routeToRestaurant, event.data))
            }
        }
    }

    companion object {
        const val userLoaded = 1
        const val routeToRestaurant = 2
        const val setErrorAlert = 3
        const val requestFetchNewRestaurant = 4
    }
}