package com.test.topapp.presentation.restaurant.manage

import androidx.lifecycle.viewModelScope
import com.test.core.domain.Restaurant
import com.test.core.domain.User
import com.test.core.interactor.*
import com.test.topapp.framework.TopAppViewModel
import com.test.topapp.utils.Event
import com.test.topapp.utils.modalalert.ModalAlertModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RestaurantManageActivityViewModel(
    private val getLocalUserUC: GetLocalUserUC,
    private val getRestaurantUC: GetRestaurantUC,
    private val createRestaurantUC: CreateRestaurantUC,
    private val restaurantEditUC: RestaurantEditUC,
    private val restaurantDeleteUC: RestaurantDeleteUC
) : TopAppViewModel() {
    private var isEdit = false
    private var restaurant: Restaurant? = null
    private var user: User? = null

    private suspend fun createRestaurant(restaurant: Restaurant) {
        setEvent(Event.Info(setLoading, true))
        createRestaurantUC(user!! to restaurant)
            .onSuccess { setEvent(Event.Info(routeToOwner)) }
            .onFailure { setEvent(Event.Error(setErrorAlert, it)) }
        setEvent(Event.Info(setLoading, false))
    }

    private suspend fun updateRestaurant(restaurant: Restaurant) {
        setEvent(Event.Info(setLoading, true))
        restaurantEditUC(user!! to restaurant)
            .onSuccess { setEvent(Event.Info(routeToRestaurant)) }
            .onFailure { setEvent(Event.Error(setErrorAlert, it)) }
        setEvent(Event.Info(setLoading, false))
    }

    private suspend fun deleteRestaurant() {
        setEvent(Event.Info(setLoading, true))
        restaurantDeleteUC(user!! to restaurant!!)
            .onSuccess { setEvent(Event.Info(routeToRestaurant)) }
            .onFailure { setEvent(Event.Error(setErrorAlert, it)) }
        setEvent(Event.Info(setLoading, false))
    }

    private suspend fun saveRestaurant(restaurant: Restaurant) {
        if (restaurant.name.isBlank()) {
            setEvent(Event.Error(setNameInputError, Exception("Restaurant name can not empty!")))
            return
        }
        if (restaurant.address.isBlank()) {
            setEvent(
                Event.Error(setAddressInputError, Exception("Restaurant address can not empty!"))
            )
            return
        }
        viewModelScope.launch {
            if (isEdit) updateRestaurant(restaurant) else createRestaurant(restaurant)
        }
    }

    private suspend fun fetchRestaurant(restaurantId: Int) {
        setEvent(Event.Info(setLoading, true))
        getRestaurantUC(user!! to Restaurant(restaurantId, user!!.id, ""))
            .onSuccess {
                restaurant = it
                setEvent(Event.Info(restaurantReady, it))
            }
            .onFailure {
                setEvent(Event.Error(setErrorAlert, it))
                delay(3000)
                setEvent(Event.Info(routeToRestaurant))
            }
        setEvent(Event.Info(setLoading, false))
    }

    suspend fun start(userId: Int, restaurantId: Int) = getLocalUserUC(User(userId, "")).onSuccess {
        user = it
        if (restaurantId >= 0) {
            fetchRestaurant(restaurantId)
            isEdit = true
        } else setEvent(Event.Info(restaurantReady, Restaurant(0, userId, "")))
    }

    private suspend fun deleteRestaurantRequest() = setEvent(
        Event.Info(
            createModalAlert,
            ModalAlertModel("Delete warning", "Are you sure to delete this restaurant?") {
                viewModelScope.launch { deleteRestaurant() }
            }
        )
    )

    override suspend fun viewEventListener(event: Event) {
        when (event) {
            is Event.Info -> when (event.type) {
                requestSaveRestaurant -> saveRestaurant(event.data as Restaurant)
                requestDeleteRestaurant -> deleteRestaurantRequest()
            }
        }
    }

    companion object {
        const val setNameInputError = 1
        const val setAddressInputError = 2
        const val setLoading = 3
        const val requestSaveRestaurant = 4
        const val routeToOwner = 5
        const val setErrorAlert = 6
        const val restaurantReady = 7
        const val routeToRestaurant = 8
        const val requestDeleteRestaurant = 9
        const val createModalAlert = 10
    }
}