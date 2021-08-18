package com.test.topapp.presentation.user.register

import androidx.lifecycle.viewModelScope
import com.test.core.domain.User
import com.test.core.interactor.*
import com.test.topapp.framework.TopAppViewModel
import com.test.topapp.utils.Event
import com.test.topapp.utils.modalalert.ModalAlertModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterActivityViewModel(
    private val getLocalUserUC: GetLocalUserUC,
    private val getRemoteUserUC: GetRemoteUserUC,
    private val userEditUC: UserEditUC,
    private val userDeleteUC: UserDeleteUC,
    private val registerUC: RegisterUC
) : TopAppViewModel() {
    private var isEdit = false
    private var admin: User? = null
    private var selectedUser: User? = null

    private suspend fun register(user: User) {
        setEvent(Event.Info(setLoading, true))
        registerUC(user)
            .onSuccess { setEvent(Event.Info(routeToHome)) }
            .onFailure { setEvent(Event.Error(setErrorAlert, it)) }
        setEvent(Event.Info(setLoading, false))
    }

    private suspend fun update(user: User) {
        setEvent(Event.Info(setLoading, true))
        userEditUC(admin!! to user)
            .onSuccess { setEvent(Event.Info(routeToAdmin)) }
            .onFailure { setEvent(Event.Error(setErrorAlert, it)) }
        setEvent(Event.Info(setLoading, false))
    }

    private suspend fun deleteUser() {
        setEvent(Event.Info(setLoading, true))
        userDeleteUC(admin!! to selectedUser!!)
            .onSuccess { setEvent(Event.Info(routeToAdmin)) }
            .onFailure { setEvent(Event.Error(setErrorAlert, it)) }
        setEvent(Event.Info(setLoading, false))
    }

    private fun validEmail(email: String): Result<Boolean> {
        if (email.isBlank())
            return Result.failure(Exception("Email can not empty!"))
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return Result.failure(Exception("Please insert valid email"))
        return Result.success(true)
    }

    private fun validPassword(password: String): Result<Boolean> {
        if (password.isBlank())
            return Result.failure(Exception("Password can not empty!"))
        return Result.success(true)
    }

    private suspend fun requestSave(user: User) {
        validEmail(user.email).onFailure {
            setEvent(Event.Error(setEmailInputError, it))
            return
        }
        if (!isEdit) validPassword(user.password).onFailure {
            setEvent(Event.Error(setPasswordInputError, it))
            return
        }
        viewModelScope.launch { if (isEdit) update(user) else register(user) }
    }

    private suspend fun deleteUserRequest() = setEvent(
        Event.Info(
            createModalAlert,
            ModalAlertModel("Delete warning", "Are you sure to delete this user?") {
                viewModelScope.launch { deleteUser() }
            }
        )
    )

    private suspend fun fetchUser(userId: Int) {
        setEvent(Event.Info(setLoading, true))
        getRemoteUserUC(admin!! to User(userId, ""))
            .onSuccess {
                selectedUser = it
                setEvent(Event.Info(userLoaded, it))
            }
            .onFailure {
                setEvent(Event.Error(setErrorAlert, it))
                delay(3000)
                setEvent(Event.Info(routeToAdmin))
            }
        setEvent(Event.Info(setLoading, false))
    }

    suspend fun start(adminId: Int, userId: Int) {
        if (adminId >= 0) getLocalUserUC(User(adminId, "")).onSuccess {
            admin = it
            fetchUser(userId)
            isEdit = true
        } else setEvent(Event.Info(userLoaded, User(0, "", 0, "", "")))
    }

    override suspend fun viewEventListener(event: Event) {
        when (event) {
            is Event.Info -> when (event.type) {
                requestRouteToLoginActivity -> setEvent(Event.Info(routeToLoginActivity))
                requestSaveUser -> requestSave(event.data as User)
                requestDeleteUser -> deleteUserRequest()
            }
        }
    }

    companion object {
        const val setEmailInputError = 1
        const val setPasswordInputError = 2
        const val requestRouteToLoginActivity = 3
        const val routeToLoginActivity = 4
        const val requestSaveUser = 5
        const val setLoading = 6
        const val setErrorAlert = 7
        const val routeToHome = 8
        const val routeToAdmin = 9
        const val userLoaded = 10
        const val requestDeleteUser = 11
        const val createModalAlert = 12
    }
}