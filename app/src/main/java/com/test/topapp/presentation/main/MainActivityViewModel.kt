package com.test.topapp.presentation.main

import com.test.core.domain.User
import com.test.core.interactor.GetLocalUsersUC
import com.test.topapp.framework.TopAppViewModel
import com.test.topapp.presentation.user.list.UserListAdapter
import com.test.topapp.utils.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivityViewModel(private val getLocalUsersUC: GetLocalUsersUC) : TopAppViewModel() {
    var users = MutableStateFlow(listOf<User>())

    private suspend fun loadUsers() = getLocalUsersUC(Unit).onSuccess {
        users.emit(it)
    }

    suspend fun start() {
        loadUsers()
        setEvent(Event.Info(usersLoaded))
    }

    private suspend fun userRouteHandle(user: User) {
        when (user.role) {
            0 -> setEvent(Event.Info(routeToUserRegular, user))
            1 -> setEvent(Event.Info(routeToUserOwner, user))
            2 -> setEvent(Event.Info(routeToUserAdmin, user))
        }
    }

    override suspend fun viewEventListener(event: Event) {
        when (event) {
            is Event.Info -> when (event.type) {
                requestRouteToLogin -> setEvent(Event.Info(routeToLogin))
                UserListAdapter.userClicked -> userRouteHandle(event.data as User)
            }
        }
    }

    companion object {
        const val requestRouteToLogin = 1
        const val routeToLogin = 2
        const val usersLoaded = 3
        const val routeToUserRegular = 4
        const val routeToUserOwner = 5
        const val routeToUserAdmin = 6
    }
}