package com.test.topapp.presentation.user.admin

import com.test.core.domain.User
import com.test.core.interactor.GetRemoteUsersUC
import com.test.core.interactor.GetUserRestaurantsUC
import com.test.core.interactor.GetLocalUserUC
import com.test.topapp.framework.TopAppViewModel
import com.test.topapp.presentation.restaurant.list.RestaurantListAdapter
import com.test.topapp.presentation.user.list.UserListAdapter
import com.test.topapp.utils.AppendPagination
import com.test.topapp.utils.Event

class UserAdminActivityViewModel(
    private val getLocalUserUC: GetLocalUserUC,
    private val getRemoteUsersUC: GetRemoteUsersUC,
    private val getUserRestaurantsUC: GetUserRestaurantsUC
) : TopAppViewModel() {
    var user: User? = null
    val users =
        AppendPagination(getRemoteUsersUC::invoke) { setEvent(Event.Error(setErrorAlert, it)) }
    val restaurants =
        AppendPagination(getUserRestaurantsUC::invoke) { setEvent(Event.Error(setErrorAlert, it)) }

    suspend fun start(userId: Int) = getLocalUserUC(User(userId, "")).onSuccess {
        user = it
        users.setFirstPair(it).fetch()
        restaurants.setFirstPair(it).fetch()
        setEvent(Event.Info(userLoaded))
    }

    override suspend fun viewEventListener(event: Event) {
        when (event) {
            is Event.Info -> when (event.type) {
                requestShowUsers -> setEvent(Event.Info(showUsers))
                requestShowRestaurants -> setEvent(Event.Info(showRestaurants))
                requestFetchNewUsers -> users.fetch()
                requestFetchNewRestaurants -> restaurants.fetch()
                RestaurantListAdapter.restaurantClicked ->
                    setEvent(Event.Info(routeToRestaurant, event.data))
                UserListAdapter.userClicked ->
                    setEvent(Event.Info(routeToUser, event.data))
            }
        }
    }

    companion object {
        const val requestShowUsers = 1
        const val requestShowRestaurants = 2
        const val showUsers = 3
        const val showRestaurants = 4
        const val setErrorAlert = 5
        const val userLoaded = 6
        const val requestFetchNewUsers = 7
        const val requestFetchNewRestaurants = 8
        const val routeToRestaurant = 9
        const val routeToUser = 10
    }
}