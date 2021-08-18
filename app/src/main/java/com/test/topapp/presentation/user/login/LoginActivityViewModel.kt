package com.test.topapp.presentation.user.login

import androidx.lifecycle.viewModelScope
import com.test.core.domain.User
import com.test.core.interactor.LoginUC
import com.test.topapp.framework.TopAppViewModel
import com.test.topapp.utils.Event
import kotlinx.coroutines.launch

class LoginActivityViewModel(private val loginUC: LoginUC) : TopAppViewModel() {
    private suspend fun login(email: String, password: String) {
        setEvent(Event.Info(setLoading, true))
        loginUC(User(-1, email = email, password = password))
            .onSuccess { setEvent(Event.Info(routeToHome)) }
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

    private suspend fun requestLogin(input: Pair<String, String>) {
        validEmail(input.first).onFailure {
            setEvent(Event.Error(setEmailInputError, it))
            return
        }
        validPassword(input.second).onFailure {
            setEvent(Event.Error(setPasswordInputError, it))
            return
        }
        viewModelScope.launch { login(input.first, input.second) }
    }

    override suspend fun viewEventListener(event: Event) {
        when (event) {
            is Event.Info -> when (event.type) {
                requestRouteToRegisterActivity -> setEvent(Event.Info(routeToRegisterActivity))
                requestLoginUser -> requestLogin(event.data as Pair<String, String>)
            }
        }
    }

    companion object {
        const val setEmailInputError = 1
        const val setPasswordInputError = 2
        const val requestRouteToRegisterActivity = 3
        const val routeToRegisterActivity = 4
        const val requestLoginUser = 5
        const val setLoading = 6
        const val setErrorAlert = 7
        const val routeToHome = 8
    }
}